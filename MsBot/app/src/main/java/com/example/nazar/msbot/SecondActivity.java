package com.example.nazar.msbot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import SchedulerTask.MsBotAlarm;
import SchedulerTask.ReservationSharedPref;

public class SecondActivity extends AppCompatActivity implements Serializable {

    Reservation reservationToBeMade;
    TextView resultView;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    static Calendar reservationTimeCalendar = getReservationTimeCalendar();

    private static Calendar getReservationTimeCalendar() {
        reservationTimeCalendar = Calendar.getInstance();
        reservationTimeCalendar.setTimeInMillis(System.currentTimeMillis());
        reservationTimeCalendar.set(Calendar.HOUR_OF_DAY, 19);
        reservationTimeCalendar.set(Calendar.MINUTE, 45);
//        reservationTimeCalendar.set(Calendar.SECOND, 15);
        return reservationTimeCalendar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        reservationToBeMade = (Reservation) getIntent().getSerializableExtra("reservationObject");

        ReservationSharedPref.RESERVATION_SHARED_PREF_UTIL.setReservation(this, reservationToBeMade);

        resultView = (TextView) findViewById(R.id.resultText);

        Long date = new Date().getTime() + 5*60*1000;
//        setAlarm(reservationTimeCalendar.getTimeInMillis());
        setAlarm(date);
    }

    private void setAlarm(Long alarmTime) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MsBotAlarm.class);

        //TODO: consider add:PendingIntent.FLAG_UPDATE_CURRENT instead of last 0
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        int AlarmType = AlarmManager.RTC;
        Log.d("DUPA", "Alarm check");
        Log.d("DUPA", getTestTime());
        alarmManager.setExact(AlarmType, alarmTime, alarmIntent);

    }

    private String getTestTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
}
