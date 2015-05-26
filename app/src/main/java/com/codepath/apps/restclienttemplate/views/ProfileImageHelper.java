package com.codepath.apps.restclienttemplate.views;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

/**
 * Created by teddywyly on 5/22/15.
 */
public class ProfileImageHelper {

    public static Transformation roundTransformation() {
        return new RoundedTransformationBuilder()
                .cornerRadiusDp(3)
                .oval(false)
                .build();
    }
}
