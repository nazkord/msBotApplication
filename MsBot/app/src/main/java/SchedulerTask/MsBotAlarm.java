package SchedulerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.nazar.msbot.Reservation;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MsBotAlarm extends BroadcastReceiver implements Serializable {

    private final Long timeStart = getReservationTimeCalendar();

    private static Long getReservationTimeCalendar() {
        Calendar reservationTimeCalendar;
        reservationTimeCalendar = Calendar.getInstance();
        reservationTimeCalendar.setTimeInMillis(System.currentTimeMillis());
        reservationTimeCalendar.set(Calendar.HOUR_OF_DAY, 6);
        reservationTimeCalendar.set(Calendar.MINUTE, 0);
        reservationTimeCalendar.set(Calendar.SECOND, 54);
        return reservationTimeCalendar.getTimeInMillis();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Reservation reservation = ReservationSharedPref.RESERVATION_SHARED_PREF_UTIL.getReservation(context);

        displayLogs("Wait for reservation");

        Long timeToStartReservation = (timeStart-(new Date().getTime())>0)?timeStart-(new Date().getTime()):0;

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                displayLogs("Reservation fired");
                GetReservation getReservation = new GetReservation();
                getReservation.execute(reservation);
            }
        }, timeToStartReservation);

    }

    private void displayLogs(String s) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeString = time.format(formatter);
        Log.i("DUPA", s);
        Log.i("DUPA", timeString);

    }
}
