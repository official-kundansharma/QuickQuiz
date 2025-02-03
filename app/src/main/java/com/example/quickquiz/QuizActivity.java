package com.example.quickquiz;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickquiz.databinding.ScoreDialogeBinding;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    // Declare UI elements and variables
    private TextView question_textView, question_Indicater_text, question_indicater_timer;
    private Button btn0, btn1, btn2, btn3, question_next;
    private LinearProgressIndicator qustion_progress_indicater;
    private long quizTimeInMillis;
    private List<QuestionModel> androidQuestions;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isAnswerSelected = false;

    //    Binding Decelration
    private ScoreDialogeBinding binding;

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge UI
        setContentView(R.layout.activity_quiz_acitivity);

        initializeViews(); // Initialize views
        setupQuizData(); // Set up quiz data and timer
        setupClickListeners(); // Set up button click listeners
        setupWindowInsets(); // Configure window insets for full-screen layout
    }

    private void initializeViews() {
        // Find views by ID
        question_textView = findViewById(R.id.question_textView);
        question_indicater_timer = findViewById(R.id.question_indicater_timer);
        question_Indicater_text = findViewById(R.id.question_Indicater_text);
        qustion_progress_indicater = findViewById(R.id.qustion_progress_indicater);
        question_next = findViewById(R.id.next_question);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
    }

    private void setupQuizData() {
        // Retrieve quiz data from intent
        androidQuestions = getIntent().getParcelableArrayListExtra("questions");
        if (androidQuestions == null) {
            Toast.makeText(this, "Error: No questions available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set the quiz title if provided
        String quizTitle = getIntent().getStringExtra("quiz_title");
        if (quizTitle != null) {
            setTitle(quizTitle);
        }

        // Set the quiz duration and initialize the timer
        quizTimeInMillis = getIntent().getLongExtra("quiz_time", 60000);
        timeLeftInMillis = quizTimeInMillis;

        // Initialize progress indicator
        qustion_progress_indicater.setMax(100);
        qustion_progress_indicater.setProgress(0);

        // Display the first question and start the timer
        displayQuestion(currentQuestionIndex);
        startTimer();
    }

    private void setupClickListeners() {




        // Handle clicks for the Next button
        question_next.setOnClickListener(v -> handleNextQuestion());

        // Handle clicks for answer buttons
        btn0.setOnClickListener(v -> handleOptionClick(btn0));
        btn1.setOnClickListener(v -> handleOptionClick(btn1));
        btn2.setOnClickListener(v -> handleOptionClick(btn2));
        btn3.setOnClickListener(v -> handleOptionClick(btn3));
    }

    private void handleNextQuestion() {


        if (!isAnswerSelected) {
            Toast.makeText(this, "Answer required to move next", Toast.LENGTH_SHORT).show();
            return; // Don't proceed if no answer is selected
        }

        // Reset answer selected flag for the next question
        isAnswerSelected = false;

        // Navigate to the next question or finish the quiz
        if (currentQuestionIndex < androidQuestions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }

        else {
            finishTest();
            Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayQuestion(int index) {
        // Display the current question and options
        QuestionModel question = androidQuestions.get(index);
        question_textView.setText(question.getQuestion());

        // Update the progress indicator
        int progress = (int) (((float) (currentQuestionIndex + 1) / androidQuestions.size()) * 100);
        qustion_progress_indicater.setProgress(progress);

        // Set options for the question
        List<String> options = question.getOptions();
        if (options.size() >= 4) {
            btn0.setText(options.get(0));
            btn1.setText(options.get(1));
            btn2.setText(options.get(2));
            btn3.setText(options.get(3));
        }

        // Reset button background colors for the new question
        resetButtonBackgrounds();

        // Update the question indicator text
        question_Indicater_text.setText(String.format("Question %d of %d",
                currentQuestionIndex + 1, androidQuestions.size()));


    }

    private void startTimer() {
        // Start the countdown timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                // Handle timer expiration
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                finishTest();

            }
        }.start();
    }

    private void updateTimerDisplay() {
        // Update the timer display with remaining time
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        question_indicater_timer.setText(timeFormatted);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleOptionClick(Button selectedButton) {
        // Change background color of the selected button
        resetButtonBackgrounds(); // Reset all button colors
        selectedButton.setBackgroundColor(getColor(R.color.orange)); // Highlight the selected button

        // Check if the selected answer is correct
        checkAnswer(selectedButton.getText().toString());

        // Mark that an answer has been selected
        isAnswerSelected = true;
    }

    private void resetButtonBackgrounds() {
        // Reset all button colors to a default color (e.g., gray)
        int defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        btn0.setBackgroundColor(defaultColor);
        btn1.setBackgroundColor(defaultColor);
        btn2.setBackgroundColor(defaultColor);
        btn3.setBackgroundColor(defaultColor);
    }

    private void checkAnswer(String selectedAnswer) {
        // Check if the selected answer is correct
        QuestionModel currentQuestion = androidQuestions.get(currentQuestionIndex);

        // Increment the correct or incorrect counter
        if (selectedAnswer.equals(currentQuestion.getCorrect())) {
            correctAnswers++;
            Log.d("QuizActivity", "Correct Answers: " + correctAnswers);
        }

     else {
            incorrectAnswers++;
            Log.d("QuizAcitivity","InCorrect: "+ incorrectAnswers);
        }

    }





    private void setupWindowInsets() {
        // Adjust view padding based on system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        // Cancel the timer to avoid memory leaks
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void finishTest() {
        int totalQuestion = androidQuestions.size();
        float percentage = ((float) correctAnswers / totalQuestion) * 100;

        // Inflate the layout using binding
        binding = ScoreDialogeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // Create a new Dialog instance
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        // Set dialog to be non-cancelable
        dialog.setCancelable(false);

        // Set the progress indicator and percentage for correct answers
        int progressValue = (int) percentage;
        binding.scoreProgressIndicater.setProgress(progressValue);
        binding.scoreProgressText.setText(String.format("%.0f%%", percentage));

        // Set title text color based on result
        if (percentage >= 60) {
            binding.scoreTitle.setTextColor(ContextCompat.getColor(this, R.color.green));
            binding.scoreTitle.setText("Congrats! YOU HAVE PASSED THE QUIZ");
        } else {
            binding.scoreTitle.setTextColor(ContextCompat.getColor(this, R.color.red));
            binding.scoreTitle.setText("Sorry, YOU DID NOT PASS THE QUIZ");

        }

        // Update the subtitle with the score
        binding.scoreProgressSubtitle.setText(String.format("%d out of %d Questions are Correct!", correctAnswers, totalQuestion));

        // Enhance the button feedback
        binding.finishedQuiz.setOnClickListener(v -> {
            // Disable the button to prevent multiple clicks
            binding.finishedQuiz.setEnabled(false);
            binding.finishedQuiz.setText("Finishing...");

            // Optionally add a delay before closing the dialog to show feedback
            new Handler().postDelayed(() -> {
                dialog.dismiss(); // Close the dialog
                finish(); // Optionally, finish the activity
            }, 1000);  // Delay for 1 second (adjust as needed)
        });

        // Show the dialog
        dialog.show();
    }



}
