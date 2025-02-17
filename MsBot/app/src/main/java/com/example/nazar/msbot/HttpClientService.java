package com.example.nazar.msbot;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpClientService {

    private CloseableHttpClient httpclient;
    private String siteName;
    private String loginSiteLink;
    private String userAgent;

    public HttpClientService() {
        this.httpclient = HttpClients.createDefault();
        this.siteName = "https://panel.dsnet.agh.edu.pl";
        this.loginSiteLink = siteName + "/login_check";
        this.userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getLoginSiteLink() {
        return loginSiteLink;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void makeRequest(String link) throws Exception {

        HttpPost request = new HttpPost(link);
        request.setHeader("User-Agent", userAgent);
        CloseableHttpResponse response = httpclient.execute(request);
        response.close();
    }

    public String attemptReservation(Element row) {
        Element cellToReserve = row.select("td").last();
        if(checkIfNotDisabled(cellToReserve)) {
            String linkForReservation = getLinkForReservation(cellToReserve);
            return makeRequestForReservation(linkForReservation);
        } else {
            return cellToReserve.text();
        }
    }

    public String getLinkForReservation(Element element) {
        return element.select("button").first().attr("formaction");
    }

    private String makeRequestForReservation(String link) {
        try {
            makeRequest(siteName + link);
        } catch (Exception e) {
            return "Error while reserving";
        }
        return "Reservation has been made!";
    }

    private boolean checkIfNotDisabled(Element cellToReserve) {
        Set<String> classes = cellToReserve.select("button").first().classNames();
        return (!classes.contains("captcha-disabled"));
    // if rd doesn't contains "button" - this is free term (according to page)
    }

    public void postLoginForm(Reservation reservationObject) throws Exception {

        /// Create a post to login on server
        HttpPost httpPost = new HttpPost(loginSiteLink);
        httpPost.setHeader("User-Agent", userAgent);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("_username", reservationObject.getUserName()));
        nvps.add(new BasicNameValuePair("_password", reservationObject.getUserPassword()));

        // set some data to send by Post
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse loginResponse = httpclient.execute(httpPost);
        loginResponse.close();
    }

    public CloseableHttpResponse getResponseOnRequest(Reservation reservationObject, String comment) throws Exception {

        HttpGet request = new HttpGet(siteName + getLinkForHalf(reservationObject.getHalfOfField()));
        request.setHeader("User-Agent", userAgent);
        CloseableHttpResponse response = httpclient.execute(request);

        System.out.print(comment + ": ");
        isCorrectness(response);

        return response;
    }

    public Map<Integer, Element> getTimeAndRowMap(Document document) {

        Elements table = document.select("tbody tr"); // get every row from table

        // go through the loop for each row get the beginning time
        return table.stream()
                .collect(
                        Collectors.toMap(row -> {
                            String time = row.select("th").first().text();
                            String beginTime = time.substring(0, time.indexOf(":"));
                            return Integer.parseInt(beginTime);
                        }, row -> row ));
    }

    private String isCorrectness(CloseableHttpResponse response) { // check whether the response is successful
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 302) {
            return " has been successful";
        } else if(statusCode != 200) {
            return "Occurred error %" + statusCode;
        } else {
            return " okay ";
        }
    }

    private String getLinkForHalf(String half) {
        switch (half) {
            case "B" : {
                return "/reserv/rezerwuj/2193";
            }
            case "C" : {
                return "/reserv/rezerwuj/2194";
            }
        }

        //#TODO: handle situation with unknown name of field

        return null;
    }
}
