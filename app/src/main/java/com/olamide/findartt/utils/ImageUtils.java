package com.olamide.findartt.utils;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.olamide.findartt.R;
import com.squareup.picasso.Transformation;
import com.yalantis.ucrop.UCrop;

public class ImageUtils {


    public static UCrop.Options defaultUcropOptions(boolean lockAspectRatio) {

        int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 16, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
        int IMAGE_COMPRESSION = 80;
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        if (lockAspectRatio) {
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);
        }
        options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);
        return options;
    }

    public static Transformation defaultAvatarTransformation(int height, int width) {
        float radius;
        if (height == width) {
            radius = (float) (height / 2);
        } else {
            radius = (float) (height + width) / 4;

        }

        RoundedTransformationBuilder transformationBuilder = new RoundedTransformationBuilder();
        transformationBuilder.cornerRadiusDp(radius);
        transformationBuilder.oval(false);
        Transformation transformation = transformationBuilder.build();
        return transformation;
    }
}
