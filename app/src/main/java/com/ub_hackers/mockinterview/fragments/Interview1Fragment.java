package com.ub_hackers.mockinterview.fragments;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.mockinterview.InterviewActivity;
import com.ub_hackers.mockinterview.base.BaseFragment;
import com.ub_hackers.mockinterview.tools.Logger;
import org.jetbrains.annotations.NotNull;

public class Interview1Fragment extends BaseFragment {

    private final String TAG = "Interview1Fragment"+this.hashCode();
    int questionIdx, questionPk;

    FirebaseDatabase mDatabase;

    TextView tvQuestion,tvBtn;

    boolean isRecording;

    InterviewActivity.ClickInterface clickInterface;
    InterviewActivity.AudioRecorder audioRecorder;

    int preparationSec=30;
    int recordingMin=3;

    public Interview1Fragment(InterviewActivity.ClickInterface clickInterface, InterviewActivity.AudioRecorder audioRecorder){
        this.clickInterface = clickInterface;
        this.audioRecorder=audioRecorder;
    }

    public TextView getTvBtn() {
        return tvBtn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview1, container, false);
        setFirebase();
        setViews(view);
        setListeners();
        getData();
        return view;
    }

    @Override
    public void setViews(View view) {
        super.setViews(view);
        tvQuestion = view.findViewById(R.id.tv_question);
        tvBtn = view.findViewById(R.id.tv_btn);
        clickInterface.setTitle("Get Ready!");
    }

    @Override
    public void setFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void setListeners() {
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    audioRecorder.stopRecording();
                    isRecording=false;
                    clickInterface.nextPage(false);
                }else{
                    audioRecorder.startRecording(tvQuestion.getText().toString());
                    clickInterface.setTitle("Question "+questionIdx);
                    tvBtn.setBackground(getContext().getDrawable(R.drawable.semi_round_alert));
                    isRecording=true;
                    clickInterface.startTimer(60*recordingMin);
                }
            }
        });
    }

    private void getData(){
        questionIdx=getArguments().getInt("questionIdx");
        questionPk=getArguments().getInt("questionPk");
        mDatabase.getReference().child("questions").child(String.valueOf(questionPk)).child("Question").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String str = (String) task.getResult().getValue();
                    tvQuestion.setText(str);
                    clickInterface.startTimer(preparationSec);
                }else{
                    task.getException().printStackTrace();
                }
            }
        });


    }
}