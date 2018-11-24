package com.example.android.moviestwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * {@link VideoListRecyclerAdapter} exposes a list of video items to a
 * {@link RecyclerView}
 */
public class VideoListRecyclerAdapter extends
        RecyclerView.Adapter<VideoListRecyclerAdapter.VideoViewHolder> {

    //TAG for log statements
//    private static final String TAG = VideoListRecyclerAdapter.class.getSimpleName();

    // Create a final private ListItemClickListener called mOnClickListener
    /*
     * An on-click handler defined for an Activity to interface with the RecyclerView
     */
    private final ListItemClickListener mVideoOnClickListener;
    private String[][] mVideoData;

    /**
     * Constructor for VideoListRecyclerAdapter that accepts
     * the specification for the ListItemClickListener.
     * <p>
     *
     * @param listener Listener for list item clicks
     */
    public VideoListRecyclerAdapter(ListItemClickListener listener) {
        mVideoOnClickListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new VideoViewHolder that holds the View for each list item
     */
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        VideoViewHolder viewHolder = new VideoViewHolder(view);

        return viewHolder;
    }

    // Implement OnClickListener in the VideoViewHolder class

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
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        String ytVideoBaseUrl = "https://img.youtube.com/vi/";
        String ytVideoUrlEnd = "/mqdefault.jpg";
        String ytVideoUrl = ytVideoBaseUrl + mVideoData[position][0] + ytVideoUrlEnd;
        String type = mVideoData[position][1];
        String title = mVideoData[position][2];
//        Log.d(TAG, "onBindViewHolder: " + ytVideoUrl);
        Context context = holder.mThumbnailImageView.getContext();
        Glide.with(context).load(ytVideoUrl).placeholder(R.drawable.placeholder_video).into(holder.mThumbnailImageView);
        holder.mVideoTypeTextView.setText(type);
        holder.mVideoTitleTextView.setText(title);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        if (null == mVideoData) return 0;
        return mVideoData.length;
    }

    /**
     * @param videoData Position of the item in the list
     */
    public void setVideoData(String[][] videoData) {
        mVideoData = videoData;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(String[] videoInfo);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final TextView mVideoTypeTextView;
        private final TextView mVideoTitleTextView;
        private final ImageView mThumbnailImageView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link VideoListRecyclerAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        private VideoViewHolder(View itemView) {
            super(itemView);

            mVideoTypeTextView = itemView.findViewById(R.id.tv_video_type);
            mVideoTitleTextView = itemView.findViewById(R.id.tv_video_title);
            mThumbnailImageView = itemView.findViewById(R.id.iv_video_thumbnail);
            // Call setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }

        // Override onClick, passing the clicked item's position (getAdapterPosition())
        // to mVideoOnClickListener via its onListItemClick method

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String[] videoInfo = mVideoData[clickedPosition];
            mVideoOnClickListener.onListItemClick(videoInfo);
        }
    }

}
