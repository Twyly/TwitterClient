package com.codepath.apps.restclienttemplate;

import android.widget.AbsListView;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by teddywyly on 5/31/15.
 */
public abstract class EndlessCursorScrollListener implements AbsListView.OnScrollListener {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
//    // The current offset index of data you have loaded
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
//    private long startingCursor = 0;

    public EndlessCursorScrollListener() {
    }

    public EndlessCursorScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
    {
        //super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            //nextCursor = 0;
//            this.currentCursor = this.startingCursor;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            } else {
                // Ask Data Source for Next Cursor
                //nextCursor =

            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            //nextCursor =

        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(getNextCursor(), totalItemCount);
            loading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(long nextCursor, int totalItemsCount);
    public abstract long getNextCursor();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //super.onScrollStateChanged(view, scrollState);
        // Don't take any action on changed
    }

}
