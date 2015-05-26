package com.codepath.apps.restclienttemplate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.views.ProfileImageHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by teddywyly on 5/19/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    private EditText etTweet;
    private TextView tvCount;
    private Button btnTweet;
    private ImageButton ibCancel;
    private int textCount;
    private TwitterClient client;
    private Tweet replyToTweet;

    public interface ComposteTweetDialogListener {
        void onFinishComposeTweet(Tweet tweet);
    }


    public ComposeTweetDialog() {

    }

    public static ComposeTweetDialog newInstance(User user) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        dialog.setArguments(args);
        return dialog;
    }

    public static ComposeTweetDialog newInstance(User user, Tweet tweet) {
        ComposeTweetDialog dialog = new ComposeTweetDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        args.putParcelable("tweet", tweet);
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
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);

        client = TwitterApplication.getRestClient();

        setupButtonListeners();

        User user = getArguments().getParcelable("user");
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        TextView tvScreenname = (TextView) view.findViewById(R.id.tvScreenname);
        ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfile);

        replyToTweet = getArguments().getParcelable("tweet");
        if (replyToTweet != null) {
            etTweet.setText("@" + replyToTweet.getUser().getScreenName() + " ");
            etTweet.setSelection(etTweet.getText().length());
        }

        tvUsername.setText(user.getName());
        tvScreenname.setText(user.getScreenName());
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(ivProfile);

        updateUIForTextChange();

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIForTextChange();
            }
        });
        return view;
    }

    private void updateUIForTextChange() {
        int count = textCount - etTweet.length();
        if (count < 20) {
            // Set to some shade to Red
            if (count < 11) {
                tvCount.setTextColor(getResources().getColor(R.color.theme_warning_red));
            } else {
                tvCount.setTextColor(getResources().getColor(R.color.theme_caution_dark_red));
            }
        } else {
            tvCount.setTextColor(getResources().getColor(R.color.theme_text_detail));
        }
        tvCount.setText(Integer.toString(count));
        setSubmitEnabled();
    }

    private void setSubmitEnabled() {
        int count = textCount - etTweet.getText().toString().length();
        btnTweet.setEnabled(!(count == textCount || count < 0));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ComposteTweetDialogListener listener = (ComposteTweetDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ComposeTweetDialogListener");
        }
    }

    private void setupButtonListeners() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!client.isNetworkAvailable()) {
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.NETWORK);
                    return;
                }
                if (replyToTweet != null) {
                    client.tweet(etTweet.getText().toString(), jsonHandle());
                } else {
                    client.tweet(etTweet.getText().toString(), jsonHandle());
                }
            }
        });
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private JsonHttpResponseHandler jsonHandle() {
        return new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSON(response);
                ComposteTweetDialogListener listener = (ComposteTweetDialogListener) getActivity();
                listener.onFinishComposeTweet(newTweet);
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
            }
        };
    }



}
