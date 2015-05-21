package com.codepath.apps.restclienttemplate;

import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

/**
 * Created by teddywyly on 5/21/15.
 */
public class HTMLTextDisplay {

    private Resources resources;

    public HTMLTextDisplay(Resources newResources) {
        resources = newResources;
    }

    public Spanned usernameSpanned(String username) {
        return Html.fromHtml("<font color=" + resources.getColor(R.color.theme_primary) + ">" + username + "</font>");
    }

    public Spanned screenameSpanned(String username) {
        return Html.fromHtml("<font color=" + resources.getColor(R.color.theme_light_detail) + ">" + "@" + username + "</font>");
    }


}
