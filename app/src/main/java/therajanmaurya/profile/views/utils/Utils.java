package therajanmaurya.profile.views.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

/**
 * @author Rajan Maurya
 *         On 27/08/17.
 */
public class Utils {

    public static void setToolbarIconColor(Context context, Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(
                        ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
            }
        }
    }
}
