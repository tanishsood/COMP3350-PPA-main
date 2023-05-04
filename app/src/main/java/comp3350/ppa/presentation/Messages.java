package comp3350.ppa.presentation;

import android.app.Activity;
import android.app.AlertDialog;

import comp3350.ppa.R;

public class Messages {
    public static void fatalError(final Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.fatalError));
        alertDialog.setMessage(message);
        alertDialog.setOnCancelListener(dialog -> owner.finish());
        alertDialog.show();
    }

    public static void warning(Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.warning));
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    public static void success(Activity owner, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(owner);
        alertDialog
            .setTitle("Success!")
            .setMessage(message)
            .setPositiveButton("OK", (alert, which) -> alert.dismiss());
        alertDialog.show();
    }

}
