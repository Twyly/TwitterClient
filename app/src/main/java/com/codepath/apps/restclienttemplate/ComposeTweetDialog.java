package com.codepath.apps.restclienttemplate;

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

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
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

        Tweet tweet = getArguments().getParcelable("tweet");
        if (tweet != null) {
            etTweet.setText("@" + tweet.getUser().getScreenName() + " ");
            etTweet.setSelection(etTweet.getText().length());
        }

        tvUsername.setText(user.getName());
        tvScreenname.setText(user.getScreenName());
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(ivProfile);

        setSubmitEnabled();

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
                if (count < 20) {
                    // Set to some shade to Red
                    if (count < 11) {
                        tvCount.setTextColor(getResources().getColor(R.color.theme_warning_red));
                    } else {
                        tvCount.setTextColor(getResources().getColor(R.color.theme_caution_dark_red));
                    }
                } else {
                    tvCount.setTextColor(getResources().getColor(R.color.theme_light_detail));
                }
                tvCount.setText(Integer.toString(count));
                setSubmitEnabled();
            }
        });
        return view;
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
                client.tweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet newTweet = Tweet.fromJSON(response);
                        ComposteTweetDialogListener listener = (ComposteTweetDialogListener) getActivity();
                        listener.onFinishComposeTweet(newTweet);
                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                        // Handle error
                    }

                });
            }
        });
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
