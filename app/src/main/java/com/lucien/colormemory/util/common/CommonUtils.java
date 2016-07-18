package com.lucien.colormemory.util.common;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Lucien on 17/7/2016.
 */
public class CommonUtils {

    public static int[] ShuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    public static void ShowToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void NotifyMessageShort(View parent, CharSequence content,
                                          CharSequence action, View.OnClickListener listener) {
        Snackbar.make(parent, content, Snackbar.LENGTH_SHORT)
                .setAction(action, listener).show();
    }
}
