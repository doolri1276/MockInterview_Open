package com.ub_hackers.mockinterview.activities.mockinterview;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.base.BaseFragment;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.fragments.Interview1Fragment;
import com.ub_hackers.mockinterview.fragments.Interview2Fragment;

import com.ub_hackers.mockinterview.models.Attempts;
import com.ub_hackers.mockinterview.models.Question;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Logger;
import com.ub_hackers.mockinterview.tools.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class InterviewActivity extends BaseActivity {

    private final String TAG="InterviewActivity";


    private int fragment=1;
    private Integer questionIdx =1, questionPk;
    int DB_MAX =19, QUESTION_MAX=5;
    int[] arr=new int[QUESTION_MAX];

    TextView tvSubTitle, tvTimer;

    Interview1Fragment frag1;
    Interview2Fragment frag2;

    FirebaseDatabase mDatabase;
    DatabaseReference mDataRef;
    Bundle bundle = new Bundle();

    Question question;
    String questionText;


    //Timer
    CountDownTimer countDownTimer;


    //Audio
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String fileName =null, fileTime=null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private String audioUrl = null;
    private Uri audioUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview);
        setFirebase();
        setViews();
        setRnd();
        setAppBar();
        setAudio();
        if(savedInstanceState==null){
            setFragments();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(recorder != null){
            recorder.release();
            recorder = null;
        }

        if(player!=null){
            player.release();
            player = null;
        }
    }

    @Override
    public void setFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDataRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setWhiteBack();
        setTitle("1/5");
    }

    private void setRnd(){

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            QUESTION_MAX=1;
            arr=new int[]{(Integer)bundle.get("questionPk")};
            return;
        }

        int i=0;
        Random rnd = new Random();
        boolean duplicates = false;
        while(i<arr.length){
            int num=rnd.nextInt(DB_MAX)+1;
            for(int j=0;j<i;j++){
                if(num==arr[j]) duplicates = true;
            }
            if (duplicates){
                duplicates = false;
                continue;
            }
            arr[i++]=num;
        }

        Logger.error(TAG, "ARRAY L "+ Arrays.toString(arr));
    }

    @Override
    public void setViews() {
        tvSubTitle = findViewById(R.id.tv_sub_title);
        tvTimer = findViewById(R.id.tv_timer);
    }

    @Override
    protected void setFragments() {
        replaceFragment(fragment);
    }

    private void replaceFragment(int nextFragment){

        if(player!=null && player.isPlaying()){
            stopPlaying();
        }

        fragment = nextFragment;
        if(questionIdx-1<QUESTION_MAX)
            questionPk=arr[questionIdx-1];
        else
            questionPk=-1;

        bundle.putInt("questionIdx", questionIdx);
        bundle.putInt("questionPk", questionPk);

        switch (fragment){
            case 1:
                    frag1= (Interview1Fragment) getSupportFragmentManager().findFragmentByTag("1");
                    if(frag1==null)
                    frag1 = new Interview1Fragment(new ClickInterface() {
                        @Override
                        public void nextPage(boolean tryAgain) {
                            stopTimer();

                            Logger.warn(TAG, "questionIDx++"+questionIdx);
                            replaceFragment(2);
                        }

                        @Override
                        public void setTitle(String title) {
                            tvSubTitle.setText(title);
                        }

                        @Override
                        public void startTimer(int time) {
                            InterviewActivity.this.startTimer(time);
                        }
                    }, new AudioRecorder() {
                        @Override
                        public void startRecording(String questionText) {

                            if(question==null){
                                InterviewActivity.this.questionText=questionText;
                            }else
                                question.setQuestionText(questionText);
                            InterviewActivity.this.startRecording();
                        }

                        @Override
                        public void stopRecording() {
                            InterviewActivity.this.stopRecording();
                        }

                        @Override
                        public void startPlaying() {
                            InterviewActivity.this.startPlaying();
                        }

                        @Override
                        public void stopPlaying() {
                            InterviewActivity.this.stopPlaying();
                        }
                    });
                tvTimer.setVisibility(View.VISIBLE);
                setTitle(questionIdx +"/"+ QUESTION_MAX);
                replaceFragment(frag1);
                getData();
                break;
            case 2:
                frag2= (Interview2Fragment) getSupportFragmentManager().findFragmentByTag("2");
                if(frag2==null)
                    frag2 = new Interview2Fragment(QUESTION_MAX, new ClickInterface() {
                        @Override
                        public void nextPage(boolean tryAgain) {
                            stopTimer();
                            if(tryAgain){

                            }else{
                                questionIdx++;
                            }
                            if(questionIdx > QUESTION_MAX){
                                if(QUESTION_MAX==1){
                                    finish();
                                }else
                                    Go.goToMainActivity(getApplicationContext());
                                return;
                            }
                            fileTime = System.currentTimeMillis()+"";
                            fileName=getExternalCacheDir().getAbsolutePath()+"/rec"+fileTime+".3gp";
                            Logger.error(TAG, "[][][][][][]["+fileName);
                            replaceFragment(1);
                        }

                        @Override
                        public void setTitle(String title) {
                            tvSubTitle.setText(title);
                        }

                        @Override
                        public void startTimer(int time) {

                        }
                    }, new AudioRecorder() {
                        @Override
                        public void startRecording(String questionText) {
                            InterviewActivity.this.startRecording();
                        }

                        @Override
                        public void stopRecording() {
                            InterviewActivity.this.stopRecording();
                        }

                        @Override
                        public void startPlaying() {
                            InterviewActivity.this.startPlaying();
                        }

                        @Override
                        public void stopPlaying() {
                            InterviewActivity.this.stopPlaying();
                        }
                    });
                tvTimer.setVisibility(View.INVISIBLE);
                replaceFragment(frag2);
                break;

        }
    }

    public void replaceFragment(BaseFragment nextFragment){
        nextFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, nextFragment, fragment+"").commitAllowingStateLoss();

    }

    private void startTimer(int time){
        stopTimer();
        countDownTimer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(Utils.convertMillsToMin(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                try{
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                        countDownTimer=null;
                    }


                    frag1.getTvBtn().performClick();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        countDownTimer.start();
    }

    private void stopTimer(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }

    private void setAudio() {
        fileName = getExternalCacheDir().getAbsolutePath();
        mProgress = new ProgressDialog(this);
        fileTime = System.currentTimeMillis()+"";
        fileName+="/rec"+fileTime+".3gp";
    }

    private void startRecording() {
        Logger.warn(TAG, "start recording : "+fileName);


        try {
            if(recorder!=null) stopRecording();
            recorder = new MediaRecorder();
            //recorder.reset();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Logger.error(TAG, "prepare() failed");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void stopRecording() {
        Logger.warn(TAG, "stop recording");
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
        
        uploadAudio();

    }

    final ValueEventListener questionListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            question = snapshot.getValue(Question.class);
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    };

    public void getData(){
        mDataRef.removeEventListener(questionListener);
        question=null;
        Logger.warn(TAG, "aaaaaaaaa : question pk : "+questionPk);
        mDataRef.child("users").child(LoginedUser.getInstance().getUserPk()).child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(questionPk.toString())){
                    mDataRef.child("users").child(LoginedUser.getInstance().getUserPk()).child("questions").child(questionPk.toString()).addValueEventListener(questionListener);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void uploadAudio() {
        mProgress.setMessage("Uploading Audio..");
        mProgress.show();
        final StorageReference filePath = mStorage.child("Audio").child("rec"+fileTime+".3gp");


        Uri uri = Uri.fromFile(new File(fileName));
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        audioUri=uri;
                        audioUrl = uri.toString();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        Attempts attempts = new Attempts(audioUrl);
                        if(question==null)
                            question = new Question(attempts, questionPk.toString(), questionText);
                        else{
                            question.getAttempts().add(attempts);
                            question.setLatestTime(attempts.getDatetime());
                        }

                        Logger.log(TAG, "wiofjowfjwfew : pakpkpk : "+questionPk);

                        mDataRef.child("users").child(user.getUid()).child("questions").child(questionPk.toString())
                                .setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Logger.error(TAG, "firebase uploaded");
                                    Logger.warn(TAG, uri.toString());
                                    mProgress.dismiss();

                                } else{
                                    task.getException().printStackTrace();
                                }
                            }
                        });

                    }
                });
            }
        });


    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audioUrl);
            player.prepare();
            player.start();

        } catch (IOException e) {
            Logger.error(TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if(player==null) return;
        player.release();
        player = null;
    }

    @Override
    public void onBackPressed() {
        final CustomDialog dialog =new CustomDialog(this);
        dialog
                .setTitle("Are you sure you want to leave?")
                .setMessage("Any progress you made will be lost.")
                .setCanceledOnTouchOutside(true)
                .setLeft("Cancel", null)
                .setRight("Leave", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dialog.dismiss();
                        InterviewActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    public interface ClickInterface{
        void nextPage(boolean tryAgain);
        void setTitle(String title);
        void startTimer(int time);
    }

    public interface AudioRecorder{
        void startRecording(String questionText);
        void stopRecording();
        void startPlaying();
        void stopPlaying();
    }


}