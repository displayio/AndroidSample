package io.display.androidsample.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import io.display.androidsample.R;

public class FragmentHelper
{
    public static void performBack(FragmentActivity activity) {
        clearFocus(activity);
        activity.onBackPressed();
    }

    public static void performTransaction(FragmentActivity activity, Fragment fragment) {
        clearFocus(activity);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static void clearFocus(Activity activity) {
        View focused = activity.getCurrentFocus();

        if (focused != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }
    }
}
