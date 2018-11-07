package FixerIO_Example;


public class App 
{
    public static void main(String[] args) {
        KeyReader keyReader = new KeyReader();
        System.out.println(keyReader.getApi_key());
        FixerHTTPManager manager = new FixerHTTPManager();
        manager.setLatestRates();
        System.out.println(manager.getLatestRates());
    }

}
