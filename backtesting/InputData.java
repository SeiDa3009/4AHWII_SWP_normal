package models;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class InputData {
    public static Map<LocalDate, Float> dataGetter(String ticker, LocalDate startdate) {
        Map<LocalDate, Float> stockValues = new TreeMap<>();
        try{
            String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));
            JSONObject objectOne = new JSONObject(link);
            JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");

            for (int i = 0; i < objectTwo.names().length(); ++i) {
                if(LocalDate.parse((CharSequence) objectTwo.names().get(i)).isAfter(startdate)){
                    stockValues.put(LocalDate.parse((CharSequence) objectTwo.names().get(i)), (Float.valueOf((String) objectTwo.getJSONObject(objectTwo.names().getString(i)).get("4. close"))));
                }
            }
        }catch (IOException e){
            System.out.println("Fehler dataGetter: " + e.getMessage());
        }
        return stockValues;
    }

    public static ArrayList<String> tickerGetter(String file){
        ArrayList<String> txtOutput = new ArrayList<>();
        BufferedReader f;
        String line;

        try {
            f = new BufferedReader(new FileReader(file));
            while ((line = f.readLine()) != null){
                txtOutput.add(line.toUpperCase());
            }
            f.close();
        }catch (IOException e){
            System.out.println("Text auslesen: " + e.getMessage());
        }
        return txtOutput;
    }
}
