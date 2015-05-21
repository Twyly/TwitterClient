package com.codepath.apps.restclienttemplate;


import android.util.Log;
import android.widget.AbsListView;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by teddywyly on 5/19/15.
 */


// Should Change this to act off of Max_ids instead of pages

public abstract class EndlessTweetScrollListener implements AbsListView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private long currentMaxID = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private long startingMaxID = 0;

    public EndlessTweetScrollListener() {
    }

    public EndlessTweetScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessTweetScrollListener(int visibleThreshold, long startMaxID) {
        this.visibleThreshold = visibleThreshold;
        this.startingMaxID = startMaxID;
        this.currentMaxID = startMaxID;
    }
    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
    {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state

        if (totalItemCount < previousTotalItemCount) {
            this.currentMaxID = this.startingMaxID;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            } else {
                Tweet lastTweet = (Tweet) view.getAdapter().getItem(totalItemCount-1);
                currentMaxID = lastTweet.getUid();
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            Tweet lastTweet = (Tweet) view.getAdapter().getItem(totalItemCount-1);
            currentMaxID = lastTweet.getUid();
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentMaxID - 1, totalItemCount);
            loading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(long maxID, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
