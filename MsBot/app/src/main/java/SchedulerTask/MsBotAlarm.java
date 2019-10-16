package SchedulerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.nazar.msbot.Reservation;

import java.io.Serializable;

public class MsBotAlarm extends BroadcastReceiver implements Serializable {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  Reservation reservation = (Reservation) intent.getSerializableExtra("reservationObject");
        Log.d("DUPA", "Alarm just fired");
      //  Log.d("DUPA2", reservation.getUserName());
    }
}
