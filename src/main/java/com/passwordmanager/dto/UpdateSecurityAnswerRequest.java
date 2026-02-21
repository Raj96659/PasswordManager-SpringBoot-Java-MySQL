package com.passwordmanager.dto;

public class UpdateSecurityAnswerRequest {

    private Long questionId;
    private String newAnswer;
    private String masterPassword;

    // Default constructor
    public UpdateSecurityAnswerRequest() {
    }

    // All-args constructor
    public UpdateSecurityAnswerRequest(Long questionId,
                                       String newAnswer,
                                       String masterPassword) {
        this.questionId = questionId;
        this.newAnswer = newAnswer;
        this.masterPassword = masterPassword;
    }

    // Getters and Setters

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getNewAnswer() {
        return newAnswer;
    }

    public void setNewAnswer(String newAnswer) {
        this.newAnswer = newAnswer;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}