package com.shutemov.catsdogs;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ZoomActivity extends AppCompatActivity implements Callback, Animator.AnimatorListener {

    static public String TAG_IMAGEURL = "IMAGE_URL";
    static public String TAG_TITLE = "TITLE";
    static public String TAG_TAGS = "TAGS";
    static public String TAG_URL = "URL";
    static public String TAG_DATE = "DATE";

    private boolean mLoadingLarge;
    private String mImageUrl;
    private String mTags;
    private String mUrl;
    private String mTitle;
    private String mDate;

    private ImageView mImv;
    private View mDetailsView;
    private boolean mDetailsShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(browserIntent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageUrl = getIntent().getStringExtra(TAG_IMAGEURL);
        mUrl = getIntent().getStringExtra(TAG_URL);
        mTags = getIntent().getStringExtra(TAG_TAGS);
        mTitle = getIntent().getStringExtra(TAG_TITLE);
        mDate = getIntent().getStringExtra(TAG_DATE);

        setTitle(mTitle);
        mImv = findViewById(R.id.imageView);
        TextView tags = findViewById(R.id.tvTags);
        TextView date = findViewById(R.id.tvDate);
        tags.setText("Tags: " + mTags);
        date.setText("Date: " + mDate.toString());

        // Trying to get larger image by removing "_m" out of image name
        mLoadingLarge = true;
        Picasso.get().load(mImageUrl).into(mImv, this);
        mDetailsShown = false;

        mDetailsView = findViewById(R.id.detailsLayout);
        mDetailsView.setVisibility(View.GONE);
        mDetailsView.setAlpha(0.0f);

        mImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDetailsShown) {
                    mDetailsView.animate()
                            .alpha(0.0f)
                            .setListener(ZoomActivity.this);
                } else {
                    mDetailsView.setVisibility(View.VISIBLE);
                    mDetailsView.animate()
                            .alpha(1.0f)
                            .setListener(ZoomActivity.this);
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onSuccess() {
        if (mLoadingLarge) {
            mLoadingLarge = false;
            final String extension =
                    mImageUrl.substring(mImageUrl.length() - 4, mImageUrl.length());
            final String large_url = mImageUrl.substring(0, mImageUrl.length() - 6) + extension;
            new Thread(new Runnable() {
                public void run() {
                    // Waiting for animation to finish before loading higher-res image
                    try {
                        Thread.sleep(450);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Picasso.get()
                                        .load(large_url)
                                        .placeholder(mImv.getDrawable())
                                        .into(mImv, ZoomActivity.this);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onError(Exception e) {
        if (mLoadingLarge) {
            mLoadingLarge = false;
            Picasso.get().load(mImageUrl).into(mImv);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
//            overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
            return true;
        }
        return false;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (mDetailsShown) {
            mDetailsView.setVisibility(View.GONE);
        }
        mDetailsShown = !mDetailsShown;
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
