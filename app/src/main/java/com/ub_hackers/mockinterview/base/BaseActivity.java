package com.ub_hackers.mockinterview.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.interfaces.ActivityFns;
import com.ub_hackers.mockinterview.interfaces.CustomAppbarInterface;
import com.ub_hackers.mockinterview.tools.BackPressCloseHandler;
import com.ub_hackers.mockinterview.tools.Utils;

public abstract class BaseActivity extends AppCompatActivity implements ActivityFns, CustomAppbarInterface {

    private final String TAG="BaseActivity";

    //appBar
    RelativeLayout rlAppbarRoot;
    TextView tvTitle;
    ImageView ivBack, ivBackWhite, ivUser, ivQuestion;

    boolean isPressAgainToClose = false;
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void setPressAgainToClose() {
        backPressCloseHandler = new BackPressCloseHandler(this);
        isPressAgainToClose = true;
    }

    @Override
    public void setAppBar() {
        rlAppbarRoot=findViewById(R.id.rl_appbar_root);
    }

    @Override
    public void setFirebase() {}

    @Override
    public void setListeners() { }

    protected void setFragments() { }

    @Override
    public void setTitle(String title) {
        tvTitle=findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    @Override
    public void setBack() {
        ivBack=findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void setWhiteBack() {
        ivBackWhite=findViewById(R.id.iv_back_white);
        ivBackWhite.setVisibility(View.VISIBLE);
        ivBackWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void setUser(View.OnClickListener listener) {
        ivUser=findViewById(R.id.iv_user);
        ivUser.setVisibility(View.VISIBLE);
        ivUser.setOnClickListener(listener);
    }

    @Override
    public void setQuestion(View.OnClickListener listener) {
        ivQuestion=findViewById(R.id.iv_question);
        ivQuestion.setVisibility(View.VISIBLE);
        ivQuestion.setOnClickListener(listener);
    }

    protected void setKeyboardSetting(final Activity activity){
        View view = findViewById(R.id.ac_root);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.keyboardHide(activity);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (isPressAgainToClose) {
            backPressCloseHandler.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
