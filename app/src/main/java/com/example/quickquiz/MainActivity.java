package com.example.quickquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView sub_heading;
    private RecyclerView recyclerView;
    private ArrayList<QuizModel> quizList;
    private QuizAdapter quizAdapter;
    private DatabaseReference databaseReference;
    ProgressBar question_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupFirebase();
        getDummyData();
    }

    private void initializeViews() {
        sub_heading = findViewById(R.id.sub_heading);
        recyclerView = findViewById(R.id.recyclerView);
        question_loading=findViewById(R.id.question_loading);

        sub_heading.setSelected(true);  // Ensure it starts scrolling
        sub_heading.requestFocus();

        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizAdapter);
    }

    private void setupFirebase() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference();  // Root reference
            Log.d(TAG, "Firebase setup successful");
        } catch (Exception e) {
            Log.e(TAG, "Firebase setup error: " + e.getMessage());
            showToast("Error setting up database connection");
        }
    }





    private void getDummyData() {

        // Show the loading spinner before starting the data fetch
        question_loading.setVisibility(View.VISIBLE);


        if (quizList.isEmpty()) {
            question_loading.setVisibility(View.VISIBLE);
        } else {
            question_loading.setVisibility(View.GONE);
        }


            if (databaseReference == null) {
                Log.e(TAG, "Database reference is null");
                showToast("Database not initialized");
                question_loading.setVisibility(View.GONE);
                return;
            }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Log.d(TAG, "Data snapshot received");
                        quizList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            QuizModel quiz = snapshot.getValue(QuizModel.class);
                            if (quiz != null) {
                                quizList.add(quiz);
                                Log.d(TAG, "Added quiz: " + quiz.getTitle());
                            } else {
                                Log.w(TAG, "Failed to parse quiz for key: " + snapshot.getKey());
                            }
                        }

                        // Hide the progress bar once data is loaded
                        question_loading.setVisibility(View.GONE);

                        quizAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Total quizzes loaded: " + quizList.size());

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing quiz data: " + e.getMessage(), e);
                        showToast("Error loading quizzes");

                        // Hide progress bar in case of error
                        question_loading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                    showToast("Failed to load quizzes: " + error.getMessage());


                    // Hide progress bar if there is a database error
                    question_loading.setVisibility(View.GONE);

                }
            });
        }

    private void showToast(String message) {
        if (!isFinishing()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}