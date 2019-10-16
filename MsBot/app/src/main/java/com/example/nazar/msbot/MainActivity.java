package com.example.nazar.msbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    EditText emailText;
    EditText passwordText;
    Spinner spinnerOfTime;
    Spinner spinnerOfHalves;
    Button reserveButton;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.emailInput);
        passwordText = (EditText) findViewById(R.id.passwordInput);
        reserveButton = findViewById(R.id.reserveButton);
        stopButton = findViewById(R.id.stopButton);

        setSpinnerOfTime();
        setSpinnerOfHalves();

        reserveButtonClicked();
        //stopButtonClicked();

    }

    private void reserveButtonClicked() {
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Reservation reservation = getReservationData();
                if(reservation != null) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("reservationObject", reservation);
                    startActivity(intent);
                } else {
                    Log.i("dataProvider", "Wrong inputs");
                }
            }
        });
    }

    private void stopButtonClicked() {
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: make stop button work
            }
        });
    }

    private Reservation getReservationData() {
        String userName = emailText.getText().toString();
        String userPassword = passwordText.getText().toString();
        String timeOfReservationString = spinnerOfTime.getSelectedItem().toString();
        String halfOfField = spinnerOfHalves.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        if (userName.isEmpty()) {
            emailText.setError(getString(R.string.error_field_required));
            focusView = emailText;
            cancel = true;
        }

        if (userPassword.isEmpty()) {
            passwordText.setError(getString(R.string.error_field_required));
            focusView = passwordText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            return new Reservation(userName, userPassword, Integer.parseInt(timeOfReservationString), halfOfField);
        }
        return null;
    }

    //#TODO - make spinner display default item

    private void setSpinnerOfTime() {
        spinnerOfTime = findViewById(R.id.spinnerOfTimes);

        ArrayAdapter<String> timeFootballAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timeOfReservationFootball));

        timeFootballAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerOfTime.setAdapter(timeFootballAdapter);
    }

    private void setSpinnerOfHalves() {
        spinnerOfHalves = findViewById(R.id.spinnerOfHalves);

        ArrayAdapter<String> halvesAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.halves));

        halvesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerOfHalves.setAdapter(halvesAdapter);
    }
}
