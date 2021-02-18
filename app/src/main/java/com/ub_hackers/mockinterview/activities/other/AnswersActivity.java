package com.ub_hackers.mockinterview.activities.other;

import android.media.MediaPlayer;
import android.view.View;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.adapters.AnswerAdapter;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.models.Question;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import com.ub_hackers.mockinterview.tools.Logger;

import java.io.IOException;


public class AnswersActivity extends BaseActivity {

    private static final String TAG = "AnswersActivity";
    RecyclerView rvQuestions;
    AnswerAdapter adapter;
    DatabaseReference mBase;

    //Audio
    private MediaPlayer player = null;
    private View prevPlaying=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        setViews();
        setListeners();
        setAppBar();
        setFirebase();
        setAdapter();
    }

    private void startPlaying(String url){
        if(player!=null) stopPlaying();
        player = new MediaPlayer();
        try{
            player.setDataSource(url);
            player.prepare();
            player.start();
        }catch (IOException e){
            Logger.error(TAG, "prepare() failed");
        }
    }

    private void stopPlaying(){
        if(player==null)return;
        player.release();
        player=null;
    }

    private void setAdapter() {
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(mBase.child("users").child(LoginedUser.getInstance().getUserPk()).child("questions"), Question.class).build();

        adapter = new AnswerAdapter(options);
        adapter.setAudioPlayer(new AudioPlayer() {
            @Override
            public void startPlaying(String url, View v) {
                if(prevPlaying!=null){
                    prevPlaying.setBackground(getApplicationContext().getDrawable(R.drawable.ic_play_default));
                }
                prevPlaying = v;
                AnswersActivity.this.startPlaying(url);
            }

            @Override
            public void stopPlaying() {
                prevPlaying.setBackground(getApplicationContext().getDrawable(R.drawable.ic_play_default));
                AnswersActivity.this.stopPlaying();
            }
        });
        rvQuestions.setAdapter(adapter);

    }

    @Override
    public void setFirebase() {
        super.setFirebase();
        mBase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setViews() {
        rvQuestions = findViewById(R.id.rv_questions);
    }

    @Override
    public void setListeners() {
        super.setListeners();
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setTitle("HIGH RISE");
        setWhiteBack();
        setQuestion(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go.goToTipsActivity(AnswersActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

        if(player!=null){
            player.release();
            player = null;
        }
    }

    public interface AudioPlayer{
        void startPlaying(String url, View v);
        void stopPlaying();
    }
}