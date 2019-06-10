package com.olamide.findartt.utils;

import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
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

    public static Transformation defaultAvatarTransformation(ImageView imageView) {
        int height; int width;
        height = imageView.getMaxHeight();
        width = imageView.getMaxWidth();
        float radius;
        if (height == width) {
            radius = (float) (height / 2);
        } else {
            radius = (float) (height + width) / 4;

        }

        RoundedTransformationBuilder transformationBuilder = new RoundedTransformationBuilder();
        transformationBuilder.cornerRadiusDp(radius);
        transformationBuilder.oval(false);
        return  transformationBuilder.build();
    }
}
