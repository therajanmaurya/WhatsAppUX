package therajanmaurya.profile.views.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

import therajanmaurya.profile.views.R;

/**
 * @author Rajan Maurya
 *         On 27/08/17.
 */

public class ImageLoaderUtils {

    public static void shareImage(Context context, ImageView imageView) {
        Uri bitmapUri = getImageUri(context, getBitmapFromView(imageView));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        context.startActivity(Intent.createChooser(shareIntent,
                context.getString(R.string.share_profile)));
    }

    public static Bitmap getBitmapFromView(ImageView view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, inContext.getString(R.string.profile_photo), null);
        return Uri.parse(path);
    }

    public static void loanUriIntoImageView(Context context, final ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .placeholder(R.drawable.cheese_4)
                .error(R.drawable.cheese_4)
                .centerCrop()
                .into(imageView);
    }

    public static void loadBitmapIntoImage(Context context, final ImageView imageView,
            byte[] bytes) {
        Glide.with(context)
                .load(bytes)
                .asBitmap()
                .placeholder(R.drawable.cheese_4)
                .error(R.drawable.cheese_4)
                .centerCrop()
                .into(imageView);
    }

    public static void loanDrawableIntoImageView(Context context, final ImageView imageView,
            int drawableId) {
        Glide.with(context)
                .load(drawableId)
                .asBitmap()
                .placeholder(R.drawable.cheese_4)
                .error(R.drawable.cheese_4)
                .centerCrop()
                .into(imageView);
    }
}
