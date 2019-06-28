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
import java.util.*;

public class FixerHTTPManager {
    private static String base_url;
    private static String api_key;
    private static String base_rate;
    private static String countryCode1;
    private static String responseString;
    private static Map< String,String> errorResponse;
    private static Map<String, Double> responseRates;
    private static Date responseRatesDate;
    private static Map<String, String> responseCountryCodes;
    private static Date responseCountryCodesDate;
    public static String success;
    private static String targetCurrency;
    private static double targetRate = 0.000000;
    private static JSONObject responseObject;
    private static JSONParser parse;
    private static String type;
    private static String info;
    private static String uri;
    private Boolean verify = false;
    private static Boolean answer;
    public static String baseRate;
    private String myErrorCode = null;
    private String api_extension = null;

    public boolean run;

    public FixerHTTPManager(){
    }

    public boolean initialize(){
        run = true;
        baseRate = "EUR";
        System.out.println("RUNNING ......");
        //setRequestVairables("rates");
        if (setRatesList() == true && setCountryCodeList() == true) {
            return true;
        }
        return false;
    }

    //Read sentacs, parse every word into a list, check to see if each entry is either code or country, then in a loop to the end display rate for each entry

    //Questions what is exchnage rate to......
        //It is 1 EUR = 3.12312 HKD

    //What is 123123 base to pounds
        //It is £234,132.00

    //What is pounds back to euros
        //Blah Blah Blah

    //What are the exchange rate trends

    //Change Base Rate to

    public boolean setRequestVairables(String requestType){
        if (requestType.equalsIgnoreCase("rates")) {
            KeyReader keyReader = new KeyReader();
            api_key = "access_key=" + keyReader.getApi_key();
            base_url = "http://data.fixer.io/api/";
            base_rate = "base=" + baseRate;
            uri = base_url + "latest?" + api_key + "&" + base_rate;
            return true;
        }
        if (requestType.equalsIgnoreCase("countryCodes")) {
            KeyReader keyReader = new KeyReader();
            api_key = "access_key=" + keyReader.getApi_key();
            base_url = "http://data.fixer.io/api/";
            base_rate = "base=" + baseRate;
            uri = base_url + "symbols?" + api_key;
            return true;
        }
        return false;
    }

    //Free Api plan allows 1,000 calls a month
    //Converst to about 33 calls a day
    //Only need to do one api call per hour, on free version rates are only updated every hour
    //Do not need to call country codes again after first time


    public void terminate(String excuse){
        myErrorCode = excuse;
        if (run == true){
            System.out.println("WARNING TERMINATING HARD (brexit means brexit)");
        }
        if (myErrorCode != null){
            System.out.println("Error: " + myErrorCode);
            if (type != null){
                System.out.println("Error Type: " + type);
            }
            if (info != null){
                System.out.println("Further Information: " + info);
            }
        }
        //System.exit(0);
    }

    public boolean contactAPI(){
        //Creates And Sends Request Of To API, Saves The Response Staticly And Responds True Or False If Completed
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet getLatestRates = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(getLatestRates);
            responseString = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse = new JSONParser();
        try {
            responseObject = (JSONObject)parse.parse(responseString);
        } catch (ParseException e) {
            System.out.println("Unable to Parse, Error Caught");
        }
        success = responseObject.get("success").toString();
        if (success=="false"){
            errorResponse = (Map<String, String>) responseObject.get("error");
            type = errorResponse.get("type");
            info = errorResponse.get("info");
            terminate("False Response From API");
            return false;
        }
        if (success=="true") {
            return true;
        }
        //not possible to get here unless 'success' throws another value
        //debug and explain to user why false
        return false;
    }

    public JSONObject getAPIResponse(){
        return responseObject;
    }

    public Boolean isItTimeToDownloadRatesListAgain(){
        Date now = new Date();
        // responseDate plus 1h < now
        //if ((responseRatesDate.).before(now))
        // SHOULD IT BE BY ONE EVERY HOUR WINDOW, OR ALLOW AGAIN AFTER 60 MINUTES HAS PASSED


        return true;
    }


    public boolean setRatesList(){

        //Gets And Saves New Rates List From API
        if (setRequestVairables("rates") == true && contactAPI() == true && isItTimeToDownloadRatesListAgain() == true) {
            responseRates = (Map<String, Double>) getAPIResponse().get("rates");
            responseRatesDate = new Date();
            return true;
        }
        //debug and explain to user why false
        return false;
    }

    public Map<String, Double> getRatesList(){
        //Returns Previously Saved Rates List
        return responseRates;
    }

    public boolean setCountryCodeList(){
        //Gets And Saves New Rates List From API
        if (setRequestVairables("countryCodes") && contactAPI() == true) {
            responseCountryCodes = (Map<String, String>) getAPIResponse().get("symbols");
            responseCountryCodesDate = new Date();
            return true;
        }
        //debug and explain to user why false
        return false;
    }

    public LinkedHashMap<String, String> getCountryCodeList(){
        //Returns Previously Saved Rates List
        return new LinkedHashMap<>(responseCountryCodes);
    }

    public boolean getCountryCodeFor(String countryName){
        for (Map.Entry<String, String> entry : getCountryCodeList().entrySet()) {
            if (entry.getValue().equalsIgnoreCase(countryName)) {
                countryCode1 = entry.getKey();
                return true;
            }
        }
        return false;
    }

    public String getAnswerCountryCodeFor(){
        return countryCode1;
    }

    public boolean getRateFor(String countryCode){
        if (countryCode.equalsIgnoreCase("list")){
            System.out.println(getCountryCodeList());
            return false;
        }
        try {
            if (getCountryCodeFor(countryCode) == true){
                countryCode = getAnswerCountryCodeFor();
            }
        } catch (Exception e){ }

       if (countryCode.length() != 3 ){
            if (wordSearch(countryCode).size() > 0 && countryCode.length() > 2){
                System.out.println("Did you mean " + wordSearch(countryCode).toString());
            } else {
                terminate("Currency code is not 3 charecters");
            }
            return false;
        }
    ////////////////////////////////////////////////////////////////////////////////////
        String targetRateString = String.valueOf(getRatesList().get(countryCode.toUpperCase()));
        if (targetRateString == "null"){
            terminate("Could not find rate for " + countryCode.toUpperCase());
            return false;
        } else {
            targetCurrency = countryCode.toUpperCase();
            targetRate = Double.parseDouble(targetRateString);
            return true;
        }
    }

    // Sentance search
    //split sentance up into words
    // compare word by word
    // take word from your sentance, and compare to each word of the


    //nah maybe just compare to keywords
    // have a key word association for each country


    public double getAnswerRateFor(){
        return targetRate;
    }

    public String getAnswerRateCode(){
        return targetCurrency;
    }


    //create map with country names and int value
    //iterate through and compare 1 by 1
    //assign similarity value to the in
        // turn to positive number if it is negative

    //if similarity is greater than 10% when compared to length of correct string "country name"

    //Maybe put that percentage in the mapping

    //Highlight the highest percentage match, should show first match that is highest if rest are same value



    public void compareToExample () {
        String str1 = "ap plee";
        String str2 = "apple";
        String str3 = "String method tutorial";

        int var1 = str1.compareTo( str2 );
        System.out.println("str1 & str2 comparison: "+var1);

        int x = minDistance(str1, str2);
        System.out.println(x);
//        System.out.println("-------------------");
//        System.out.println(getCountryCodeList().ge);

    }

    public ArrayList wordSearch(String exampleWord){
        List<String> wordsToSearch = Arrays.asList(exampleWord.split(" "));
        ArrayList <String> listOfCountries = new ArrayList(getCountryCodeList().values());
        int match = 2;
        int comp = 0;
        int existingMatched = 1;
        ArrayList <String> bestMatches = new ArrayList();
        for (int i = 0; i < listOfCountries.size(); i++){
            List<String> wordsToCompareTo = Arrays.asList(listOfCountries.get(i).split(" "));
            int newMatches = 0;
            for (int x = 0; x < wordsToSearch.size(); x++){
                for (int y = 0; y < wordsToCompareTo.size(); y++){
                    int calculatedInt = minDistance(wordsToCompareTo.get(y).toLowerCase(), wordsToSearch.get(x).toLowerCase());
                    comp++;
                    if (calculatedInt < match) {
                        newMatches++;
                    }
                }
            }
            if (newMatches > existingMatched){
                existingMatched = newMatches;
                bestMatches = new ArrayList();
                bestMatches.add(listOfCountries.get(i));
            } else if (newMatches == existingMatched){
                bestMatches.add(listOfCountries.get(i));
            }
        }
        System.out.println(comp + " Comparisons Made");
        return bestMatches;
    }

    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }
        return dp[len1][len2];
    }

}
