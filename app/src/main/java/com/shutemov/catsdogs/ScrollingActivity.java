package com.shutemov.catsdogs;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shutemov.catsdogs.data.FlickrContent;

import java.text.SimpleDateFormat;

public class ScrollingActivity extends AppCompatActivity implements ImageFragment
        .OnListFragmentInteractionListener {
    private ImageFragment mCatsFragment;
    private ImageFragment mDogsFragment;
    private ImageFragment mUnfilteredFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresing...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                Refresh();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        mCatsFragment = ImageFragment.getInstance(fragmentManager, "cats");
        mDogsFragment = ImageFragment.getInstance(fragmentManager, "dogs");
        mUnfilteredFragment = ImageFragment.getInstance(fragmentManager, "");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.CatsFragment, mCatsFragment);
        fragmentTransaction.replace(R.id.DogsFragment, mDogsFragment);
        fragmentTransaction.replace(R.id.UnfilteredFragment, mUnfilteredFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(FlickrContent.FlickrItem item, View iconView) {
        Intent intent = new Intent(getBaseContext(), ZoomActivity.class);
        intent.putExtra(ZoomActivity.TAG_IMAGEURL, item.media);
        intent.putExtra(ZoomActivity.TAG_URL, item.link);
        intent.putExtra(ZoomActivity.TAG_TAGS, item.tags);
        intent.putExtra(ZoomActivity.TAG_TITLE, item.title);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        intent.putExtra(ZoomActivity.TAG_DATE, df.format(item.date_taken));
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, iconView, "icon");
        startActivity(intent, options.toBundle());
    }

    void Refresh() {
        mCatsFragment.UpdateContents();
        mDogsFragment.UpdateContents();
        mUnfilteredFragment.UpdateContents();
    }
}
