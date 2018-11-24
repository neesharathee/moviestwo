package com.example.android.moviestwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ReviewListRecyclerAdapter} exposes a list of review items to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class ReviewListRecyclerAdapter extends
        RecyclerView.Adapter<ReviewListRecyclerAdapter.ReviewViewHolder> {

    //TAG for log statements
    // Create a final private ListItemClickListener called mOnClickListener
    /*
     * An on-click handler defined for an Activity to interface with the RecyclerView
     */
    private final ListItemClickListener mReviewOnClickListener;
    private String[][] mReviewData;

    /**
     * Constructor for ReviewListRecyclerAdapter that accepts
     * the specification for the ListItemClickListener.
     * <p>
     *
     * @param listener Listener for list item clicks
     */
    public ReviewListRecyclerAdapter(ListItemClickListener listener) {
        mReviewOnClickListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ReviewViewHolder that holds the View for each list item
     */
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    // Implement OnClickListener in the ReviewViewHolder class

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
//        String reviewId = mReviewData[position][0];
        String author = mReviewData[position][1];
        String content = mReviewData[position][2];
//        String reviewUrl = mReviewData[position][3];
        holder.mReviewAuthorTextView.setText(author);
        holder.mReviewTextView.setText(content);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.length;
    }

    /**
     * @param reviewData Position of the item in the list
     */
    public void setReviewData(String[][] reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(String[] reviewInfo);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class ReviewViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final TextView mReviewAuthorTextView;
        private final TextView mReviewTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link ReviewListRecyclerAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        private ReviewViewHolder(View itemView) {
            super(itemView);

            mReviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            mReviewTextView = itemView.findViewById(R.id.tv_review_content);
            // Call setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }

        // Override onClick, passing the clicked item's position (getAdapterPosition())
        // to mOnClickListener via its onListItemClick method

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String[] reviewInfo = mReviewData[clickedPosition];
            mReviewOnClickListener.onListItemClick(reviewInfo);
        }
    }

}
