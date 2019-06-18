package FixerIO_Example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class KeyReader {
    private String api_key;

    public KeyReader(){
        Properties appProps = new Properties();

        try {
            appProps.load(new FileReader("Resources/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        api_key = appProps.getProperty("api_key");

    }

    public String getApi_key() {
        return api_key = "a5e6864562a9f8f8f9f365a974cb26c6";
    }
}
