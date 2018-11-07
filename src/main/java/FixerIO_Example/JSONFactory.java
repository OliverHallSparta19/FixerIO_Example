package FixerIO_Example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFactory {

    private JSONObject latestRatesJSON;

    public JSONObject getLastestRatesJSON() {
        return latestRatesJSON;
    }

    public void setLastestRatesJSON(String lastestRatesJSONString) {

        JSONParser jsonParser = new JSONParser();
        try {
            this.latestRatesJSON = (JSONObject) jsonParser.parse(lastestRatesJSONString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
