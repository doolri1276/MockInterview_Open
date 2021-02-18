package com.ub_hackers.mockinterview.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ub_hackers.mockinterview.R;

public class CustomDialog {
    private final String TAG="custom_dial";

    private Context context;
    public Dialog dialog;

    TextView tvTitle, tvMessage, tvLeft,tvRight;

    View vLine;

    String title, message, right, left;

    View.OnClickListener leftListener, rightListener;

    boolean titleExists=false, msgExists=false,leftExists=false, rightExists=false, isCanceledOnTouchOutside=false;

    DialogInterface.OnCancelListener cancelListener;

    public CustomDialog(Context context){this.context=context;}

    public CustomDialog setTitle(String title){
        this.title =title;
        titleExists=true;
        return this;
    }

    public CustomDialog setMessage(int rIdx){
        message = context.getString(rIdx);
        msgExists=true;
        return this;
    }

    public CustomDialog setMessage(String msg){
        message =msg;
        msgExists=true;
        return this;
    }

    public CustomDialog setLeft(String msg, View.OnClickListener onClickListener){
        left=msg;
        leftListener=onClickListener;
        leftExists=true;
        return this;
    }

    public CustomDialog setRight(String msg, View.OnClickListener onClickListener){
        right=msg;
        rightListener=onClickListener;
        rightExists=true;
        return this;
    }

    public CustomDialog setCanceledOnTouchOutside(Boolean bool){
        isCanceledOnTouchOutside=bool;
        return this;
    }

    public CustomDialog setCancelListener(DialogInterface.OnCancelListener cancelListener){
        this.cancelListener = cancelListener;
        return this;
    }

    public CustomDialog setDismissListener(DialogInterface.OnDismissListener dismissListener){
        this.dismissListener = dismissListener;
        return this;
    }

    public CustomDialog create(){
        try{
            dialog=new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog);

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount=0.5f;
            dialog.getWindow().setAttributes(lp);

            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);

            if(titleExists){
                tvTitle=dialog.findViewById(R.id.tv_d_title);
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            }

            if(msgExists){
                tvMessage=dialog.findViewById(R.id.tv_message);
                tvMessage.setText(message);
                tvMessage.setVisibility(View.VISIBLE);
            }

            if(leftExists){
                tvLeft=dialog.findViewById(R.id.tv_left);
                tvLeft.setText(left);
                tvLeft.setVisibility(View.VISIBLE);
                if(leftListener==null){
                    tvLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissListener.onDismiss(dialog);
                        }
                    });
                }else
                    tvLeft.setOnClickListener(leftListener);
            }

            if(rightExists){
                tvRight=dialog.findViewById(R.id.tv_right);
                tvRight.setText(right);
                tvRight.setVisibility(View.VISIBLE);
                if(rightListener ==null)
                    tvRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissListener.onDismiss(dialog);
                        }
                    });
                else
                    tvRight.setOnClickListener(rightListener);
            }
            if(leftExists!=rightExists){
                vLine=dialog.findViewById(R.id.v_line);
                vLine.setVisibility(View.GONE);
            }
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });

            if(cancelListener!=null)
                dialog.setOnCancelListener(cancelListener);

            if(dismissListener!=null)
                dialog.setOnDismissListener(dismissListener);

            dialog.create();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public void show(){
        try{
            if(dialog!=null)
                dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            dialog.dismiss();
        }
    };

}
