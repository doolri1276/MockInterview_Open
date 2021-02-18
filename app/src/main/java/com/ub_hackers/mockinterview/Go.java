package com.ub_hackers.mockinterview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.ub_hackers.mockinterview.activities.MainActivity;
import com.ub_hackers.mockinterview.activities.mockinterview.InterviewActivity;
import com.ub_hackers.mockinterview.activities.mockinterview.InterviewMainActivity;
import com.ub_hackers.mockinterview.activities.other.AnswersActivity;
import com.ub_hackers.mockinterview.activities.signinup.ChangePasswordActivity;
import com.ub_hackers.mockinterview.activities.other.TipsActivity;
import com.ub_hackers.mockinterview.activities.signinup.SignInActivity;
import com.ub_hackers.mockinterview.activities.signinup.SignUpActivity;
import com.ub_hackers.mockinterview.activities.signinup.UserActivity;

public class Go {
    public static void goToSignInActivity(Context context){
        Intent i=new Intent(context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void goToSignUpActivity(Context context){
        Intent i=new Intent(context, SignUpActivity.class);
        context.startActivity(i);
    }

    public static void goToMainActivity(Context context){
        Intent i=new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public static void goToInterviewMainActivity(Context context){
        Intent i=new Intent(context, InterviewMainActivity.class);
        context.startActivity(i);
    }

    public static void goToInterviewActivity(Context context){
        goToInterviewActivity(context, null);
    }

    public static void goToInterviewActivity(Context context, Bundle bundle){
        Intent i=new Intent(context, InterviewActivity.class);
        if(bundle!=null)i.putExtras(bundle);
        context.startActivity(i);
    }

    public static void goToAnswersActivity(Context context){
        Intent i=new Intent(context, AnswersActivity.class);
        context.startActivity(i);
    }

    public static void goToUserActivity(Context context){
        Intent i=new Intent(context, UserActivity.class);
        context.startActivity(i);
    }

    public static void goToTipsActivity(Context context){
        Intent i=new Intent(context, TipsActivity.class);
        context.startActivity(i);
    }

    public static void goToChangePasswordActivity(Context context){
        Intent i = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(i);
    }
}
