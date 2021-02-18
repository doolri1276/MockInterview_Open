package com.ub_hackers.mockinterview.fragments;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.mockinterview.InterviewActivity;
import com.ub_hackers.mockinterview.base.BaseFragment;

public class Interview2Fragment extends BaseFragment {

    TextView tvAgain, tvNext, tvMsg, tvPlay;
    ImageView ivImg;
    InterviewActivity.ClickInterface clickInterface;
    InterviewActivity.AudioRecorder audioRecorder;

    boolean isPlaying;
    int QUESTION_MAX, questionIdx;

    public Interview2Fragment(int QUESTION_MAX, InterviewActivity.ClickInterface clickInterface, InterviewActivity.AudioRecorder audioRecorder){
        this.clickInterface = clickInterface;
        this.QUESTION_MAX=QUESTION_MAX;
        this.audioRecorder = audioRecorder;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview2, container, false);
        getData();
        setViews(view);
        setListeners();
        return view;
    }

    private void getData(){
        questionIdx=getArguments().getInt("questionIdx");
    }

    @Override
    public void setViews(View view) {
        tvAgain = view.findViewById(R.id.tv_again);
        tvNext = view.findViewById(R.id.tv_next);
        tvMsg=view.findViewById(R.id.tv_msg);
        ivImg=view.findViewById(R.id.iv_img);
        tvPlay = view.findViewById(R.id.tv_play);
        if(questionIdx>=QUESTION_MAX) {
            tvNext.setText("Finish Interview");
            tvMsg.setText("Keep practicing \nand you'll ace the interview!");
            ivImg.setImageDrawable(getContext().getDrawable(R.drawable.interview_done_img));
        }

    }

    public void setPlayer(boolean isPlaying){
        this.isPlaying=isPlaying;
        if(!isPlaying){
            tvPlay.setText("Play Your Answer");
            tvPlay.setBackground(getContext().getDrawable(R.drawable.semi_round_branding));
            audioRecorder.stopPlaying();
        }else{
            tvPlay.setText("Stop Playing");
            tvPlay.setBackground(getContext().getDrawable(R.drawable.semi_round_alert));
            audioRecorder.startPlaying();
        }
    }

    @Override
    public void setListeners() {

        tvAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.nextPage(true);
            }
        });

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayer(!isPlaying);
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.nextPage(false);
            }
        });


    }
}