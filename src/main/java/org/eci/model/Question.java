package org.eci.model;

import java.util.List;

public class Question {
    private String category;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public Question(String category, String questionText, List<String> options, String correctAnswer) {
        this.category = category;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
