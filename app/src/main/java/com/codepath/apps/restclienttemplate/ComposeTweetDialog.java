package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by teddywyly on 5/19/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    private EditText etTweet;
    private TextView tvCount;
    private Button btnTweet;
    private Button btnCancel;
    private int textCount;

    public ComposeTweetDialog() {

    }

    public static ComposeTweetDialog newInstance(String title) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        textCount = Integer.parseInt(tvCount.getText().toString());

        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        setupButtonListeners();

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = textCount - s.length();
                tvCount.setText(Integer.toString(count));
            }
        });
        return view;
    }

    private void setupButtonListeners() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
