package com.example.navinpd.horizontalscrollview;

import android.animation.Animator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * A linearlayout contains child views.
 * Created by navin.pd@gmail.com on 5/8/15
 */
public class MainActivity extends ActionBarActivity {
    RelativeLayout mainHolder;
    RelativeLayout friendContainer;
    LinearLayout linearContainer;
    HorizontalScrollView horizontalScroll;
    ImageView searchIcon;
    RelativeLayout searchContainer;
    SearchView searchText;
    TextView searchCancel;
    int oldWidth;
    ArrayList<View> viewContainer;
    Button addButton;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        addButton = (Button) findViewById(R.id.add_button);
        linearContainer = (LinearLayout) findViewById(R.id.horizontal_linear);
        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontal_Scroll);
        mainHolder = (RelativeLayout) findViewById(R.id.main_holder);
        friendContainer = (RelativeLayout) findViewById(R.id.friend_container);
        searchIcon = (ImageView) findViewById(R.id.search_icon);

        searchContainer = (RelativeLayout) findViewById(R.id.search_container);
        searchCancel = (TextView) findViewById(R.id.cancel_image);
        searchText = (SearchView) findViewById(R.id.search_text);
        viewContainer = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater i = LayoutInflater.from(MainActivity.this.getBaseContext());
                View view = getLayoutInflater().inflate(R.layout.scroll_item_view, null);
                ImageView iv = (ImageView) view.findViewById(R.id.image_view);
                TextView tx = (TextView) view.findViewById(R.id.text);
                tx.setText(viewContainer.size() + 1 + " Item");
                linearContainer.addView(view);
                iv.setImageResource(R.mipmap.ic_launcher);
                view.setTag(viewContainer.size());

                viewContainer.add(view);
                checkAndSetWidth();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearContainer.removeView(viewContainer.get((Integer) v.getTag()));
                        viewContainer.remove((int) (Integer) v.getTag());
                        fixPosition();
                        checkAndSetWidth();
                        mainHolder.invalidate();
                    }
                });

            }
        });

        findViewById(R.id.scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScrollSampleActivity.class));
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewHorizontally(friendContainer, searchContainer);
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewHorizontally(searchContainer, friendContainer);
            }
        });
    }

    private void animateViewHorizontally(final View view1, final View view2) {
        YoYo.with(Techniques.FadeOutLeft).delay(100)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        view1.setVisibility(View.VISIBLE);
                        view1.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        view1.setAlpha(0);
                        view1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                    }
                })
                .playOn(view1);
        YoYo.with(Techniques.FadeInRight).delay(100)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        view2.setVisibility(View.VISIBLE);
                        view2.setAlpha(0);
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        view2.setVisibility(View.VISIBLE);
                        view2.setAlpha(1);
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                    }
                })
                .playOn(view2);
    }

    private void fixPosition() {
        for (int i = 0; i < viewContainer.size(); i++) {
            View vi = viewContainer.get(i);
            TextView tx = (TextView) vi.findViewById(R.id.text);
            tx.setText(i + 1 + " Item");
            vi.setTag(i);
            viewContainer.set(i, vi);
        }
    }

    private void checkAndSetWidth() {
        horizontalScroll.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linearContainer.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int scrollWidth = horizontalScroll.getMeasuredWidth();
        int linearWidth = linearContainer.getMeasuredWidth();
        int mainWidth = getResources().getDisplayMetrics().widthPixels;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) horizontalScroll.getLayoutParams();
        if (80 + scrollWidth > mainWidth) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    horizontalScroll.setScrollbarFadingEnabled(true);
                }
            }, 300);
            horizontalScroll.setScrollbarFadingEnabled(false);
            params.width = oldWidth;
            horizontalScroll.setLayoutParams(params);
        } else {
            horizontalScroll.setScrollbarFadingEnabled(true);
            params.width = scrollWidth;
            horizontalScroll.setLayoutParams(params);
            oldWidth = scrollWidth;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
