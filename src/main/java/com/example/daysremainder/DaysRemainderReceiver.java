package com.example.daysremainder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DaysRemainderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        StringBuilder msgStr = new StringBuilder("Текущее время: ");
//        Format formatter = new SimpleDateFormat("hh:mm:ss a");
//        msgStr.append(formatter.format(new Date()));
//        Toast.makeText(context, msgStr, Toast.LENGTH_SHORT).show();

//        Calendar rightNow = Calendar.getInstance();
//        StringBuilder sb = new StringBuilder();
//        sb.append(rightNow + "\n");
//        sb.append(rightNow.get(Calendar.YEAR)
//                + "-"
//                + rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT,
//                Locale.US) + "-" + rightNow.get(Calendar.DATE) + "\n");
//        sb.append("День года: " + rightNow.get(Calendar.DAY_OF_YEAR) + "\n");
//        sb.append("День месяца: " + rightNow.get(Calendar.DAY_OF_MONTH) + "\n");
//        sb.append("День недели: " + rightNow.get(Calendar.DAY_OF_WEEK) + "\n");
//        sb.append("getTime(): " + rightNow.getTime() + "\n");
//        sb.append("getTimeInMillis(): " + rightNow.getTimeInMillis() + "\n");
//        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int ACTION_TIME = Integer.parseInt(prefs.getString(String.valueOf(R.string.action_time), "9"));
        int ACTION_VIBRO = Integer.parseInt(prefs.getString(String.valueOf(R.string.action_vibro), "5"));
        Calendar rightNow = Calendar.getInstance();
        int currentYear =rightNow.get(Calendar.YEAR);
        int currentMonth = rightNow.get(Calendar.MONTH);
        int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);
//        if (currentMinute==43 && currentHour==13) {
        if (currentMinute==0 && currentHour==ACTION_TIME) {
            //TODO find all occurrences for this date and show notifications
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("УВЕДОМЛЕНИЯ")
                    .setMessage("Этот пункт меню еще не реализован")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Закрываем окно
                            dialog.cancel();
                        }
                    }).create().show();
        }
    }

}
