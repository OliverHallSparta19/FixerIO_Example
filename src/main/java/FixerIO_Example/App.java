package FixerIO_Example;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class App
{
    public static String success;
    static int i =1;
    private static Double Doubleinput;
    private static FixerHTTPManager manager = new FixerHTTPManager();
    private static SearchMaster searchMaster = new SearchMaster();
    //private static ThreadExample threadExample = new ThreadExample();

    private static Double input;
    private static String stringInput;

    public static void main(String[] args) throws Exception {

//        threadExample.setUp();
        //searchMaster.search();

//        LoadingBar loadingBar = new LoadingBar();
//        loadingBar.m

//        if (manager.initialize() == true) {
//                System.out.println("Successful Start");
//                ui();
//            } else {
//                System.out.println("False Start ");
//            }
//        System.exit(0);
    }

    public static void murder(){
        System.exit(999);
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
            murder();
        } else {
            System.out.println("nothing registered");
            ui();
        }
    }

    public static void app(){
        FixerHTTPManager manager = new FixerHTTPManager();
        System.out.println("What currency code are you looking for?");
        String targetCurrency = getStringInput();
        if (targetCurrency.equalsIgnoreCase("exit")){
            ui();
        }
        if (manager.getRateFor(targetCurrency) == true){
            System.out.println(manager.getAnswerRateCode() + ": " + manager.getAnswerRateFor());
        }
        app();
    }

    public static String getStringInput(){
        Scanner reader = new Scanner(System.in);
        stringInput = reader.nextLine().toLowerCase();
        if (stringInput.isEmpty()){
            getStringInput();
        }
        return stringInput;
    }

    public static Boolean setDoubleInput() {
        String string = getStringInput();
        input = null;
        if (string.equalsIgnoreCase("exit")) {
            return false;
        }
            try {
                input = Double.parseDouble(string);
            } catch (Exception e) { }
            if (input == null){
                System.out.println(string + " is not a number");
                setDoubleInput();
            }
            if (input != null) {
            Doubleinput = input;
            return true;
            }
            return false;
    }

    public static Double getDoubleInput(){
        return Doubleinput;
    }

// NEED MORE ABSTRACATION

    // Sets set the maps and vairables

    // But getters need to be getters, just do a return back here,

    //Should not need the system exits, system should run its course with out duplciating commands after the exit command
        //This may be static values?
            //Commands with no returns?


    //GO WAY OF WORK COULD BE THIS
//    funcGetFromMap{
//        IF SETVAIRABLE ==TRUE
//        RETURN GET VAIRABLEthathasjustbeenset
//            else
//                return false or null
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