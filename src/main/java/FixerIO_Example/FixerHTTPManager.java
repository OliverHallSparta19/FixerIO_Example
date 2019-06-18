package FixerIO_Example;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

public class FixerHTTPManager {
    private static String base_url;
    private static String api_key;
    private static String base_rate;
    private static String latestRates;
    private static Map< String,String> errorResponse;
    private static Map<String, Double> responseRates;
    public static String success;
    private static String targetCurrency;
    private static double targetRate = 0.000000;
    private static JSONObject responseObject;
    private static JSONParser parse;
    private static String type;
    private static String info;
    public static String baseRate;
    private String myErrorCode = null;
    private String api_extension = null;

    public boolean run;

    public boolean initialize(){
        run = true;
        baseRate = "EUR";
        System.out.println("RUNNING ......");
        setRequestVairables();
        return getRates();
    }

    public FixerHTTPManager(){
    }

    public void setRequestVairables(){
        KeyReader keyReader = new KeyReader();
        api_key = "access_key=" + keyReader.getApi_key();
        base_url = "http://data.fixer.io/api/";
        base_rate = "base=" + baseRate;
    }

    public void terminate(){
        if (run == true){
            System.out.println("WARNING TERMINATING HARD (brexit means brexit)");
        } else {
            //System.out.println("TERMINATING");
        }
        if (myErrorCode != null){
            System.out.println("Error: " + myErrorCode);
        }
    }


    public boolean getRates(){
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            System.out.println("BASE RATE IS " + baseRate);
            HttpGet getLatestRates = new HttpGet(base_url + "latest?" + api_key + "&" + base_rate);
            CloseableHttpResponse response = httpClient.execute(getLatestRates);
            latestRates = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse = new JSONParser();
        try {
            responseObject = (JSONObject)parse.parse(latestRates);
        } catch (ParseException e) {
            System.out.println("Unable to Parse, Error Caught");
            e.printStackTrace();
        }
        success = responseObject.get("success").toString();
        if (success=="false"){
            errorResponse = (Map<String, String>) responseObject.get("error");
            type = errorResponse.get("type");
            info = errorResponse.get("info");
            System.out.println("Error Type: " + type);
            System.out.println("Further Information: " + info);
            terminate();
            return false;
        }
        if (success=="true") {
            responseRates = (Map<String, Double>) responseObject.get("rates");
            return true;
        }
        return false;
    }

    public void findRate(String findRate){
        if (success!="true") {
            myErrorCode = "Success is not true";
            terminate();
        } else if (findRate.length() != 3){
            myErrorCode = "Currency code is not 3 charecters";
            terminate();
        }else {
            targetCurrency = findRate.toUpperCase();
            String targetRateString = String.valueOf(responseRates.get(targetCurrency));
            if (targetRateString != "null") {
                targetRate = Double.parseDouble(targetRateString);
            } else {
                targetRate = 0.000000;
            }
            if (targetRate == 0.000000){
                System.out.println("COULD NOT FIND RATE FOR " + targetCurrency);
            } else {
                System.out.println( targetCurrency + ": " + targetRate);
            }

        }
    }
}
