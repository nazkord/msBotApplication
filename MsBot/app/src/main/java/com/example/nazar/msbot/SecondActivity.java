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
import java.text.DateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import SchedulerTask.MsBotAlarm;

public class SecondActivity extends AppCompatActivity implements Serializable {

    Reservation reservationToBeMade;
    HttpClientService msClient = new HttpClientService();
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
        resultView = (TextView) findViewById(R.id.resultText);

        Long date = new Date().getTime() + 30*1000;
//        setAlarm(reservationTimeCalendar.getTimeInMillis());
        setAlarm(date);

//        GetReservation getReservation = new GetReservation();
//        getReservation.execute();

    }

    private void setAlarm(Long alarmTime) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MsBotAlarm.class);
     //   intent.putExtra("reservationObject", reservationToBeMade);

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

    private class GetReservation extends AsyncTask<Void, String, String> implements Serializable {

        @Override
        protected void onPreExecute() {
            resultView.setText(getString(R.string.making_reservation_in_progress));
        }

        //#TODO - make better response while errors!

        @Override
        protected String doInBackground(Void... voids) {

            try {
                msClient.makeRequest(msClient.getSiteName());
                publishProgress("Connection: okay");
            } catch (Exception e) {
                return "Error connection to the website";
            }

            try {
                msClient.postLoginForm(reservationToBeMade);
                publishProgress("Login: okay");
            } catch (Exception e) {
                return "Error connection the login page";
            }

            // #TODO – find out how to replace do-while loop with something more effective

            String result = "";
            int timer = 0;

            do { // STARTING POINT FOR LOOP

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    timer++;
                } catch (InterruptedException e) {
                    Log.i("timer", e.getMessage());
                }

                /// get html of the page in response and analyze it later
                try {
                    CloseableHttpResponse responseOnReservationRequest = msClient.getResponseOnRequest(reservationToBeMade, "Reservation page");
                    HttpEntity entityReservation = responseOnReservationRequest.getEntity();
                    String responseStr = EntityUtils.toString(entityReservation); // convert response in String

                    Document pageDocument = Jsoup.parse(responseStr); // convert String to doc (jSoup library)

                    // get map where key are times and value are rows of the table with reservations
                    Map<Integer,Element> timeMap = msClient.getTimeAndRowMap(pageDocument);

                    //TODO: make this work using stream

                    for (Map.Entry<Integer, Element> entry : timeMap.entrySet()) {
                        Integer time = entry.getKey();

                        //if so, reserve this term
                        if (time.equals(reservationToBeMade.getTimeOfGame())) {
                            result = msClient.attemptReservation(timeMap.get(time));
                        }
                    }
                } catch (Exception e) {
                    Log.i("pageOfReservations", e.getMessage());
                }
                publishProgress(Integer.toString(timer));
            } while(!result.equals("Reservation has been made!") && timer != 15); // ENDING POINT FOR LOOP
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            resultView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            resultView.setText(result);
        }
    }
}
