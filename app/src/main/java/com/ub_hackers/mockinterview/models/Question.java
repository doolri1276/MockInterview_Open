package com.ub_hackers.mockinterview.models;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private boolean bookmark = false;
    private List<Attempts> attempts;
    private String latestTime;
    private String questionText;
    private String questionPk;

    public Question(){}
    public Question(Attempts attempts, String questionPk, String questionText){
        this.attempts = new ArrayList<>();
        this.attempts.add(attempts);
        latestTime = attempts.getDatetime();
        this.questionText=questionText;
        this.questionPk = questionPk;
    }

    public void setBookmark(boolean bookmark){
        this.bookmark = bookmark;
    }

    public boolean getBookmark(){
        return bookmark;
    }

    public List<Attempts> getAttempts(){
        return attempts;
    }

    public String getLatestTime(){
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionPk() {
        return questionPk;
    }

    public String getQuestionText() {
        return questionText;
    }
}
