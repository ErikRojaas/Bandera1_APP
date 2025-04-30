package com.bandera1.android;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.badlogic.gdx.Gdx;
import com.bandera1.Utils.KeyboardHelper;

public class AndroidKeyboardHelper implements KeyboardHelper {
    private Activity activity;

    public AndroidKeyboardHelper(Activity activity) {
        this.activity = activity;
    }

    public void showKeyboard() {
        Gdx.app.log("AndroidKeyboardHelper","showKeyboard");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                // You need to attach this to a View that can accept input
                View view = activity.findViewById(android.R.id.content);
                if (view != null) {
                    view.requestFocus();
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    public void hideKeyboard() {
        Gdx.app.log("AndroidKeyboardHelper","hideKeyboard");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                View view = activity.getCurrentFocus();
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

}
