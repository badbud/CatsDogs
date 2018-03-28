package com.shutemov.catsdogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.InputStream;

public class DrawableMatcher extends TypeSafeMatcher<View> {

    private final String mAssetFileName;

    private DrawableMatcher(String assetFileName) {
        super(View.class);
        this.mAssetFileName = assetFileName;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)){
            return false;
        }
        ImageView imageView = (ImageView) target;
        Context context = target.getContext();
        try {
            InputStream is = context.getAssets().open(mAssetFileName);
            Bitmap otherBitmap = BitmapFactory.decodeStream(is);
            if (otherBitmap == null)
                return false;
            Bitmap bitmap = getBitmap(imageView.getDrawable());
            return bitmap.sameAs(otherBitmap);
        } catch (Exception e) {
            // File cannot be opened
            return false;
        }
    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmapFromVectorDrawable(drawable);
        }
        else {
            throw new IllegalArgumentException("unsupported drawable type " + drawable.getClass());
        }
    }

    private static Bitmap getBitmapFromVectorDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from asset file: ");
        description.appendText(mAssetFileName);
    }

    public static DrawableMatcher imageWithAssetFile(String  assetFileName) {
        return new DrawableMatcher(assetFileName);
    }
}