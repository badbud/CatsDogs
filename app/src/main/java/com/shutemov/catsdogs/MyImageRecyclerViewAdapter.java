package com.shutemov.catsdogs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shutemov.catsdogs.ImageFragment.OnListFragmentInteractionListener;
import com.shutemov.catsdogs.data.FlickrContent;
import com.shutemov.catsdogs.data.FlickrContent.FlickrItem;
import com.squareup.picasso.Picasso;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FlickrItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImageRecyclerViewAdapter extends RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder> {

    private final FlickrContent mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyImageRecyclerViewAdapter(FlickrContent values, OnListFragmentInteractionListener
            listener) {
        mValues = values;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.getItem(position);
        Picasso.get().load(holder.mItem.media).into(holder.mPictureView);
        holder.mTextView.setText(shorten(holder.mItem.title,38));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, holder.mPictureView);
                }
            }
        });
    }

    private String shorten(String str, int len) {
        if (str.length() > len) {
            return str.substring(0,len-3) + "...";
        } else
            return str;
    }

    @Override
    public int getItemCount() {
        return mValues.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPictureView;
        public final TextView mTextView;
        public FlickrItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPictureView = (ImageView) view.findViewById(R.id.picture);
            mTextView = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }

    }
}
