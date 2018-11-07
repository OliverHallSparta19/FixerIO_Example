package FixerIO_Example;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class FixerHTTPManager {
    private String base_url;
    private String api_key;
    private String latestRates;

    public FixerHTTPManager(){
        KeyReader keyReader = new KeyReader();
        api_key = "access_key=" + keyReader.getApi_key();
        base_url = "http://data.fixer.io/api/";
    }

    public void setLatestRates(){
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet getLatestRates = new HttpGet(base_url + "latest?" + api_key);
            CloseableHttpResponse response = httpClient.execute(getLatestRates);
            latestRates = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLatestRates() {
        return latestRates;
    }
}
