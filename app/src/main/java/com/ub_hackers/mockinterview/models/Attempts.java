package com.ub_hackers.mockinterview.models;

import com.ub_hackers.mockinterview.tools.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Attempts {
    private String datetime;
    private String audioUrl;

    public Attempts(){}

    public Attempts(String url){
        this.audioUrl = url;
        setDate();
    }

    private void setDate(){
        this.datetime = Utils.getCurDate();
    }

    public String getDatetime(){
        return datetime;
    }


    public String getAudioUrl(){
        return audioUrl;
    }
}
