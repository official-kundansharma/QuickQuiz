package com.example.quickquiz;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class QuizModel implements Parcelable {
    private String id;
    private String title;
    private String sub_title;
    private String timer;
    private List<QuestionModel> questionList;  // List of questions

    public QuizModel() {
        questionList = new ArrayList<>();
    }

    // Constructor
    public QuizModel(String id, String title, String sub_title, String timer) {
        this.id = id;
        this.title = title;
        this.sub_title = sub_title;
        this.timer = timer;
        this.questionList = new ArrayList<>();
    }

    // Getter and Setter methods
    public List<QuestionModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    // Parcelable implementation for QuizModel
    @Override
    public int describeContents() {
        return 0; // No special content
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id); // Write the id to the Parcel
        dest.writeString(title); // Write the title to the Parcel
        dest.writeString(sub_title); // Write the sub_title to the Parcel
        dest.writeString(timer); // Write the timer to the Parcel
        dest.writeTypedList(questionList); // Write the list of questions to the Parcel
    }

    // Constructor to recreate QuizModel from Parcel
    protected QuizModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        sub_title = in.readString();
        timer = in.readString();
        questionList = in.createTypedArrayList(QuestionModel.CREATOR); // Read the list of questions
    }

    // Parcelable Creator for QuizModel
    public static final Creator<QuizModel> CREATOR = new Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel in) {
            return new QuizModel(in); // Create a new QuizModel from the Parcel
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size]; // Return an array of QuizModel objects
        }
    };
}

class QuestionModel implements Parcelable {
    private String question;  // The question text
    private List<String> options;  // List of answer options
    private String correct;  // The correct answer


    // No-argument constructor required for Firebase
    public QuestionModel() {
        options = new ArrayList<>();
    }

    // Constructor
    public QuestionModel(String question, List<String> options, String correct) {
        this.question = question;
        this.options = options;
        this.correct = correct;
    }

    // Getter and Setter methods
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    // Parcelable implementation for QuestionModel
    @Override
    public int describeContents() {
        return 0; // No special content
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(question); // Write the question to the Parcel
        dest.writeStringList(options); // Write the list of options to the Parcel
        dest.writeString(correct); // Write the correct answer to the Parcel
    }

    // Constructor to recreate QuestionModel from Parcel
    protected QuestionModel(Parcel in) {
        question = in.readString();
        options = in.createStringArrayList(); // Read the list of options
        correct = in.readString();
    }

    // Parcelable Creator for QuestionModel
    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in); // Create a new QuestionModel from the Parcel
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size]; // Return an array of QuestionModel objects
        }
    };
}
