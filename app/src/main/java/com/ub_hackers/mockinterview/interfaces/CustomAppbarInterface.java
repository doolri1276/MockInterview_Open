package com.ub_hackers.mockinterview.interfaces;

import android.view.View;

public interface CustomAppbarInterface {
    void setAppBar();
    void setTitle(String title);
    void setBack();
    void setWhiteBack();
    void setUser(View.OnClickListener listener);
    void setQuestion(View.OnClickListener listener);
}
