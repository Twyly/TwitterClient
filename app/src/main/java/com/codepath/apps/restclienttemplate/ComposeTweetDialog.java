package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by teddywyly on 5/19/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    public ComposeTweetDialog() {

    }

    public static ComposeTweetDialog newInstance(String title) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        return view;
    }
}
