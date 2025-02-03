package com.example.quickquiz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private final ArrayList<QuizModel> questionList;
    private final Context context;

    public QuizAdapter(ArrayList<QuizModel> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizModel quizItem = questionList.get(position);
        holder.quiz_title_text.setText(quizItem.getTitle());
        holder.quiz_subtitle_text.setText(quizItem.getSub_title());
        holder.quiz_time_text.setText(String.format("%s Min", quizItem.getTimer()));




        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                 // Example to pass intent

//                intent.putExtra("quiz_title", quizItem.getTitle());
                 intent.putParcelableArrayListExtra("questions", new ArrayList<>(quizItem.getQuestionList()));
                 // Optionally, you can pass other information, like the quiz title
                intent.putExtra("quiz_title", quizItem.getTitle());

                // Convert minutes to milliseconds for the timer
                long timeInMillis = Long.parseLong(quizItem.getTimer()) * 60 * 1000;
                intent.putExtra("quiz_time", timeInMillis);

                v.getContext().startActivity(intent);
            } catch (Exception e) {
                Log.e("QuizAdapter", "Error launching activity", e);
                Toast.makeText(v.getContext(), "Error opening quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quiz_title_text, quiz_subtitle_text, quiz_time_text;
        ImageView quiz_timer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title_text = itemView.findViewById(R.id.quiz_title_text);
            quiz_subtitle_text = itemView.findViewById(R.id.quiz_subtitle_text);
            quiz_timer = itemView.findViewById(R.id.quiz_timer);
            quiz_time_text = itemView.findViewById(R.id.quiz_time_text);

        }
    }
}