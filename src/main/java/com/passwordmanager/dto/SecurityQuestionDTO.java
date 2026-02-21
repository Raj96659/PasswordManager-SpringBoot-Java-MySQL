package com.passwordmanager.dto;

public class SecurityQuestionDTO {

    private String question;
    private String answer;

    // Default constructor
    public SecurityQuestionDTO() {
    }

    // All-args constructor
    public SecurityQuestionDTO(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}