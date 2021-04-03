package models;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

public class Input {
   public static Map<LocalDate, Float> dataGetter(String shortenSymbol) {
      Map<LocalDate, Float> stockValues = new TreeMap<>();
      try{
         String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + shortenSymbol + "&outputsize=full&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));
         JSONObject objectOne = new JSONObject(link);
         JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");

         for (int i = 0; i < objectTwo.names().length(); ++i) {
            stockValues.put(LocalDate.parse((CharSequence) objectTwo.names().get(i)), (Float.valueOf((String) objectTwo.getJSONObject(objectTwo.names().getString(i)).get("5. adjusted close"))));
         }
      }catch (IOException e){
         System.out.println("Fehler dataGetter: " + e.getMessage());
      }
      return stockValues;
   }
   public static ArrayList<String> stocksGetter(String file){
      ArrayList<String> txtOutput = new ArrayList<>();
      BufferedReader f;
      String line;

      try {
         f = new BufferedReader(new FileReader(file));
         while ((line = f.readLine()) != null){
            txtOutput.add(line);
         }
         f.close();
      }catch (IOException e){
         System.out.println("Text auslesen: " + e.getMessage());
      }
      return txtOutput;
   }
}
