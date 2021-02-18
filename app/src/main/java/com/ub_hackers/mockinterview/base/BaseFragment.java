package com.ub_hackers.mockinterview.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.fragments.Interview1Fragment;
import com.ub_hackers.mockinterview.interfaces.ActivityFns;
import com.ub_hackers.mockinterview.interfaces.OnBackPressedListener;
import com.ub_hackers.mockinterview.tools.BackPressCloseHandler;

public abstract class BaseFragment extends Fragment implements ActivityFns {

    public OnBackPressedListener onBackPressedListener;

    private View rootView;

    boolean isPressAgainToClose = false;
    BackPressCloseHandler backPressCloseHandler;

    public BaseFragment(){
        setOnBackPressedListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interview1, container, false);
    }


    protected void setOnBackPressedListener(){
        onBackPressedListener = new OnBackPressedListener() {
            @Override
            public boolean doBack() {
//                if(isPressAgainToClose){
//                    backPressCloseHandler.onBackPressed();
//                    return true;
//                }
//
//                if(getFrag)
                return false;
            }
        };
    }

    @Override
    public void setFirebase() {

    }


    public void setViews(View view) {
        rootView=view;
    }

    @Override
    public void setViews() {

    }
}
