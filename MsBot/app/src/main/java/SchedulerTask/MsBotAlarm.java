package SchedulerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.nazar.msbot.Reservation;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MsBotAlarm extends BroadcastReceiver implements Serializable {

    @Override
    public void onReceive(Context context, Intent intent) {
        Reservation reservation = ReservationSharedPref.RESERVATION_SHARED_PREF_UTIL.getReservation(context);

//        long timeToStartReservation = (timeStart-(new Date().getTime())>0)?timeStart-(new Date().getTime()):0;

        displayLogs("Reservation fired");
        GetReservation getReservation = new GetReservation();
        getReservation.execute(reservation);
    }

    private void displayLogs(String s) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeString = time.format(formatter);
        Log.i("DUPA", s);
        Log.i("DUPA", timeString);
    }


}
