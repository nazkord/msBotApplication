package SchedulerTask;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.nazar.msbot.R;
import com.example.nazar.msbot.Reservation;

public enum ReservationSharedPref {

    RESERVATION_SHARED_PREF_UTIL();

    public Reservation getReservation(Context context) {
//        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shPref_reservation_key), Context.MODE_PRIVATE);

        String userName = sharedPref.getString(context.getString(R.string.shPref_userName_key), null);
        String userPassword = sharedPref.getString(context.getString(R.string.shPref_password_key), null);
        String halfOfField = sharedPref.getString(context.getString(R.string.shPref_halfOfField_key), null);
        Integer timeOfReservation = sharedPref.getInt(context.getString(R.string.shPref_time_key), 0);

        return new Reservation(userName, userPassword, timeOfReservation, halfOfField);
    }

    public void setReservation(Context context, Reservation reservation) {
//        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shPref_reservation_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString(context.getString(R.string.shPref_userName_key), reservation.getUserName());
        prefEditor.putString(context.getString(R.string.shPref_password_key), reservation.getUserPassword());
        prefEditor.putString(context.getString(R.string.shPref_halfOfField_key), reservation.getHalfOfField());
        prefEditor.putInt(context.getString(R.string.shPref_time_key), reservation.getTimeOfGame());
        prefEditor.apply();
    }
}
