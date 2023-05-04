package comp3350.ppa.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import comp3350.ppa.R;

public class CustomDatePicker {
    public static void getDate(Activity activity, EditText dueDate) {
        DatePickerDialog datePickerDialog;

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dueDate.setText(date);
        };

        int year, month, day;

        Calendar cal;

        try {
            cal = makeCalendar(dueDate.getText().toString());
        } catch (ParseException e) {
            Messages.warning(activity,"Error in date string, defaulting to today.");
            cal = Calendar.getInstance();
        }
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.Theme_MaterialComponents_Light_Dialog_Alert;
        datePickerDialog = new DatePickerDialog(activity, style, dateSetListener, year, month, day);

        dueDate.clearFocus();
        datePickerDialog.show();
    }
    public static String makeDateString(int day, int month, int year) {
        return year + "-" + String.format(Locale.CANADA, "%02d", month)  + "-" + String.format(Locale.CANADA, "%02d", day) ;
    }

    public static Calendar makeCalendar(String s) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date date = formatter.parse(s);
        if (date == null)
            throw new ParseException("Error parsing date string " + s, 0);
        cal.setTime(date);
        return cal;
    }

    public static String getTodayAsString() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}
