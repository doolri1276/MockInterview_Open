package com.ub_hackers.mockinterview.activities.other;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.base.BaseActivity;
import org.jetbrains.annotations.NotNull;

public class TipsActivity extends BaseActivity {

    private final String TAG="AnswersActivity";

    ViewPager vp;
    TabLayout tlTab;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        setViews();
        setListeners();
        setAppBar();
        setAdapter();
    }

    @Override
    public void setViews() {
        vp=findViewById(R.id.vp);
        tlTab=findViewById(R.id.tl_tab);
    }

    @Override
    public void setAppBar() {
        super.setAppBar();
        setWhiteBack();
        setTitle("HIGH RISE");
    }

    private void setAdapter(){
        adapter = new Adapter();
        vp.setAdapter(adapter);
        tlTab.setupWithViewPager(vp);

    }

    class Adapter extends PagerAdapter{
        @Override
        public int getCount() {
            return 8;
        }

        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.item_tip, container, false);
            ImageView ivImg = view.findViewById(R.id.iv_img);
            TextView tvTipTitle = view.findViewById(R.id.tv_tip_title);
            TextView tvTipText = view.findViewById(R.id.tv_tip_text);

            ivImg.setImageDrawable(getDrawable(R.drawable.img_tip1+position));
            tvTipTitle.setText(getString(R.string.tip_title1+position));
            tvTipText.setText(getString(R.string.tip_text1+position));

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            container.removeView((View)object);
        }
    }


}

