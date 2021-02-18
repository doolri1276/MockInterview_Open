package com.ub_hackers.mockinterview.activities.mockinterview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.os.Bundle;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import com.ub_hackers.mockinterview.tools.Logger;
import org.jetbrains.annotations.NotNull;

public class InterviewMainActivity extends BaseActivity {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 88888;
    private final String TAG="InterviewMainActivity";

    TextView tvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_interview_main);

        setViews();
        setListeners();
        setAppBar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUDIO_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode){
            case AUDIO_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(InterviewMainActivity.this, "audio permission has been granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InterviewMainActivity.this, "[WARN] audio permission is not granted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setViews() {
        tvStart=findViewById(R.id.tv_start);
    }

    @Override
    public void setListeners() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                    Go.goToInterviewActivity(InterviewMainActivity.this);
            }
        });
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setWhiteBack();
        setTitle("HIGH RISE");
    }
}