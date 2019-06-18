package FixerIO_Example;


import java.util.Scanner;

public class App
{
    public static String success;
    static int i =1;

    public static void main(String[] args) {
        FixerHTTPManager manager = new FixerHTTPManager();
        Boolean ex = manager.initialize();
        if (ex == true) {
            System.out.println("Successful Start");
            ui();
        } else {
            System.out.println("START UP RESPONSE WAS " + ex);
        }

    }

    public static void ui(){
        Scanner reader1 = new Scanner(System.in);
        if (i == 1){
            System.out.println("FixerIO Currency Rate API, by Oliver Hall");
            i++;
        }
        System.out.println("Start / Exit / Help");
        String command = reader1.nextLine().toLowerCase();
        if (command.contains("start")) {
            try {
                app();
            }catch (Exception ie){
                System.out.println("ERROR");
                ui();
            }
        } else if (command.contains("help")) {
            System.out.println("I WILL WRITE THIS SOON");
            ui();
        } else if (command.contains("exit")) {
            System.out.println("---- END ----");
        } else {
            System.out.println("nothing registered");
            ui();
        }
    }

    public static void app(){
        FixerHTTPManager manager = new FixerHTTPManager();
        Scanner reader = new Scanner(System.in);
        System.out.println("What currency code are you looking for?");
        String targetCurrency = reader.nextLine().toLowerCase();
        if (targetCurrency.equalsIgnoreCase("exit")){
            ui();
        }
        manager.findRate(targetCurrency);
        app();
    }






    //OLD METHOD
//    public static void main(String[] args) {
//        KeyReader keyReader = new KeyReader();
//        keyReader.getApi_key();
//        FixerHTTPManager manager = new FixerHTTPManager();
//        manager.setLatestRates();
//        manager.getLatestRates();
//    }


}

/*
==== INSTRUCTIONS ====

start up
    FUNCTION Get country codes
    put into map
    -> Return Map

    FUNCTION Find country
        get the code for a country name, ignoring case, use this mehtod to interate through an pull the key that has the matching value

public <K, V> K getKey(Map<K, V> map, V value) {
    for (Entry<K, V> entry : map.entrySet()) {
        if (entry.getValue().equals(value)) {
            return entry.getKey();
        }
    }
    return null;
}

or

public <K, V> Stream<K> keys(Map<K, V> map, V value) {
    return map
      .entrySet()
      .stream()
      .filter(entry -> value.equals(entry.getValue()))
      .map(Map.Entry::getKey);
}

or

Stream<String> keyStream1 = keys(capitalCountryMap, "South Africa");
String capital = keyStream1.findFirst().get();

    -> Return String code

    FUNCTION Get rates
    put into map
    ->return map

    Function find rates
    using code from find country code, get from the map and return
    -> return int rate

---- PROJECT SCOPE ----

Put a text ui in the front

    Options;
    Help
    Get All Rates
    Get Specific Rate
    Covert to Currency
    Convert from Currency

Switch case or ? to match countries to country code

Make a test case

Debug feature to ui for connection or other issues

Write ReadMe

---- FURTHER DOWN THE LINE ----







 */