package SchedulerTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nazar.msbot.HttpClientService;
import com.example.nazar.msbot.Reservation;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GetReservation extends AsyncTask<Reservation, String, String> implements Serializable {

    private HttpClientService msClient = new HttpClientService();

    @Override
    protected void onPreExecute() {
        Log.i("making_reservation_in_progress", "IN PROGRESS");
    }

    //#TODO - make better response while errors!

    @Override
    protected String doInBackground(Reservation... reservations) {
        Reservation reservationToBeMade = reservations[0];

        try {
            msClient.makeRequest(msClient.getSiteName());
//            publishProgress("Connection: okay");
            Log.i("Connection","Okay");
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
//            return "Error connection to the website";
        }

        try {
            msClient.postLoginForm(reservationToBeMade);
//            publishProgress("Login: okay");
            Log.i("Login", "Okay");
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
//            return "Error connection the login page";
        }

        // #TODO – find out how to replace do-while loop with something more effective

        String result = "";
        int timer = 0;

        do { // STARTING POINT FOR LOOP

            try {
                TimeUnit.MILLISECONDS.sleep(4000);
                timer++;
                Log.i("timer", String.valueOf(timer));
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
        } while(!result.equals("Reservation has been made!") && timer != 300); // ENDING POINT FOR LOOP
        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {
       // resultView.setText(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
      //  resultView.setText(result);
    }
}
