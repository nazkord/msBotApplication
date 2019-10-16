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
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeString = time.format(formatter);
        Log.d("DUPA", "Alarm just fired");
        Log.d("DUPA", timeString);

        GetReservation getReservation = new GetReservation();
        getReservation.execute(reservation);
    }
}
