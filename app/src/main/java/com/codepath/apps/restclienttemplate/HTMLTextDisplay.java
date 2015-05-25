package com.codepath.apps.restclienttemplate;

import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by teddywyly on 5/21/15.
 */
public class HTMLTextDisplay {

    private Resources resources;

    public HTMLTextDisplay(Resources newResources) {
        resources = newResources;
    }

    public Spanned usernameSpanned(String username) {
        return Html.fromHtml("<b><font color=" + resources.getColor(R.color.theme_text_primary) + ">" + username + "</font></b>");
    }

    public Spanned screenameSpanned(String username) {
        return Html.fromHtml("<font color=" + resources.getColor(R.color.theme_text_detail) + ">" + "@" + username + "</font>");
    }

    public Spanned statSpanned(String stat, int number) {
        if (number != 1) {
            stat = stat + "S";
        }
        Spanned numSpan = Html.fromHtml("<font color=" + resources.getColor(R.color.theme_text_primary) + ">" + number + "</font>");
        Spanned statSpan = Html.fromHtml("<font color=" + resources.getColor(R.color.theme_text_detail) + ">" + stat.toUpperCase() + "</font>");
        return (Spanned) TextUtils.concat(numSpan, " ", statSpan);
    }


}
