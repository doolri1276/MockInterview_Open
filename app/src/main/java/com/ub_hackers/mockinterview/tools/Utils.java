package com.ub_hackers.mockinterview.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.interfaces.NetworkRetryInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String TAG="Utils";

    public static boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        return isValidRegx(expression, email);
    }

    public static boolean isValidPassword(String email) {
        String expression = "^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        return isValidRegx(expression, email);
    }

    public static boolean isValidRegx(String expression, String text){
        if(text==null||text.length()==0)return false;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static String convertMillsToMin(long mills){
        String strMinTime = "0";
        try{
            strMinTime = String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(mills),
                    TimeUnit.MILLISECONDS.toSeconds(mills)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mills)));
        }catch (Exception e){
            return strMinTime;
        }

        return strMinTime;
    }

    public static void keyboardHide(Activity activity){

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);

        Logger.log(TAG, "keyboardHide");
        View view = activity.getCurrentFocus();

        if(view == null){
            view=new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public boolean isNetworkAvailable(Context context){
        return isNetworkAvailable(context, null);
    }

    public boolean isNetworkAvailable(Context context, final NetworkRetryInterface retryInterface){
        try{
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();

            if(info == null || !info.isConnectedOrConnecting()){
                final CustomDialog dialog = new CustomDialog(context);

                if(retryInterface == null){
                    dialog
                            .setMessage(R.string.network_disable)
                            .setLeft("Okay", null)
                            .create()
                            .show();
                }else {
                    dialog
                            .setMessage(R.string.network_disable)
                            .setRight("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    retryInterface.retry();
                                    dialog.dialog.dismiss();
                                }
                            })
                            .setLeft("Okay", null)
                            .create()
                            .show();
                }

                Logger.log(TAG,"NETWORK NOT CONNECTED");
                return false;
            }
        }catch(Exception e){
            return false;
        }

        Logger.log(TAG,"NETWORK CONNECTED");
        return true;
    }


    public static String getCurDate(){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = df.format(currentDate);
        return formatDate;
    }
}
