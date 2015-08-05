package com.example.navinpd.horizontalscrollview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                friendContainer.setVisibility(View.GONE);
                searchContainer.setVisibility(View.VISIBLE);
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendContainer.setVisibility(View.VISIBLE);
                searchContainer.setVisibility(View.GONE);
            }
        });

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
        int lineaWidth = linearContainer.getMeasuredWidth();
        int mainWidth = getResources().getDisplayMetrics().widthPixels;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) horizontalScroll.getLayoutParams();
        if (80 + scrollWidth > mainWidth) {
            params.width = oldWidth;
            horizontalScroll.setLayoutParams(params);
        } else {
            params.width = scrollWidth;
            horizontalScroll.setLayoutParams(params);
            oldWidth = scrollWidth;
        }

        Toast.makeText(this, "scrollWidth " + scrollWidth + " LinearWidth " + lineaWidth, Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
