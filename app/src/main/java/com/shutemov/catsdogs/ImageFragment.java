package com.shutemov.catsdogs;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONObject;

import com.shutemov.catsdogs.data.FlickrContent;
import com.shutemov.catsdogs.data.FlickrContent.FlickrItem;



/**
 * A fragment representing a list of Images.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ImageFragment extends Fragment implements DownloadCallback<DownloadTask.Result> {

    public static final String ARG_TAG = "tag";
    private static boolean error_occured  = false;

    private String mTag;
    private OnListFragmentInteractionListener mListener;

    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;
    private FlickrContent mData;
    private MyImageRecyclerViewAdapter mAdapter;
    private boolean mDownloading = false;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageFragment() {

    }

    public static ImageFragment getInstance(FragmentManager fragmentManager, String tag) {
        // Recover ImageFragment in case we are re-creating the Activity due to a config change.
        // The ImageFragment is recoverable because it calls setRetainInstance(true).
        ImageFragment fragment = (ImageFragment) fragmentManager
                .findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new ImageFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TAG, tag);
            fragment.setArguments(args);
            fragmentManager.beginTransaction().add(fragment, tag).commit();
            fragment.setTag(tag);
        }
        return fragment;
    }

    public static ImageFragment newInstance(String tag) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAG, tag);
        fragment.setArguments(args);
        fragment.setTag(tag);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTag = getArguments().getString(ARG_TAG);
        }
        setRetainInstance(true);
    }

    public void setTag(String tag) { mTag = tag; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false));
            mData = new FlickrContent();
            mAdapter = new MyImageRecyclerViewAdapter(mData, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UpdateContents();
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateFromDownload(DownloadTask.Result result) {
        // Something happened during download
        if (result.mException != null) {
            result.mException.printStackTrace();

            // Display error dialog and interrupt
            if (!error_occured) {
                error_occured = true;
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this.getContext(),
                            android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this.getContext());
                }
                builder.setTitle(R.string.connectivity_problem)
                        .setMessage(R.string.check_your_connection)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
            return;
        }

        String resultText = new String(result.mResultValue);
        try {
            mData.UpdateContent(new JSONObject(resultText));
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FlickrItem item, View iconView);
    }

    public void UpdateContents()
    {
        if (!mDownloading) {
            mDownloading = true;
            error_occured = false;
            android.net.Uri.Builder builder = Uri.parse(getString(R.string.api_url)).buildUpon();
            builder.appendQueryParameter("format", "json");
            builder.appendQueryParameter("nojsoncallback", "1");

            if (mTag != null)
                builder.appendQueryParameter("tags", mTag);
            String url = builder.build().toString();
            mDownloadTask = CatsDogsApp.getInstance().getDownloadTask(this);
            mDownloadTask.execute(url);
        }
    }
}
