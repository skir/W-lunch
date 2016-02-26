package tseh20.wlunch.tools;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ilia on 22.02.16.
 */
public class TypefaceManager {

    static Typeface mRegular;
    static Typeface mLight;
    static Typeface mMedium;

    public static Typeface getRegularTypeface(Context context) {
        if (mRegular == null) {
            mRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
        }
        return mRegular;
    }

    public static Typeface getLightTypeface(Context context) {
        if (mLight == null) {
            mLight = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Light.ttf");
        }
        return mLight;
    }

    public static Typeface getMediumTypeface(Context context) {
        if (mMedium == null) {
            mMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Medium.ttf");
        }
        return mMedium;
    }
}
