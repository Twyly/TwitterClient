package com.codepath.apps.restclienttemplate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;

/**
 * Created by teddywyly on 5/25/15.
 */
public class ErrorHelper {

    public enum ErrorType {
        GENERIC,
        NETWORK;
    }

    public static void showErrorAlert(Context context, ErrorType error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = null;
        switch (error) {
            case NETWORK:
                message = context.getString(R.string.network_failure_warning);
                break;
            case GENERIC:
                message = context.getString(R.string.general_failure_warning);
                break;
        }
                builder.setMessage(Html.fromHtml("<font color=" + "#000000" + ">" + message + "</font>"))
                        .setCancelable(true)
                        .setPositiveButton(R.string.casual_dialog_accept,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
        builder.create().show();
    }
}
