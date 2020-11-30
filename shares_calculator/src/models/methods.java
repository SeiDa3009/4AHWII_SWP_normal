package models;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class methods {
    private static String DBUrl;
    private static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
    private static Scanner reader = new Scanner(System.in);

    public void dataGetter(String shortenSymbol) throws JSONException,MalformedURLException, IOException{
        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                + shortenSymbol + "&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));
        JSONObject objectOne = new JSONObject(link);
        JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");
        for (int i = 0; i < 100; i++){
            System.out.print(objectTwo.names().get(i) + ": ");
            dates.add(LocalDate.parse((CharSequence) objectTwo.names().get(i)));
            System.out.println(objectTwo.getJSONObject(objectTwo.names().getString(i)).get("4. close"));
        }
        dates.sort(null);
        System.out.println(dates);
    }

    public String sharesSelect(){
            System.out.print("Select Your Share (Shortcut): ");
            return reader.next();
    }

    public void setdatabase(){
        DBUrl = "jdbc:sqlite:C:/Users/david/OneDrive/Schule/4AHWII/SWP/SWP-Rubner/Normal_github/projects/sharesCalculator/" + sharesSelect() + ".db";
        String sql;
        try{
            Connection conn = DriverManager.getConnection(DBUrl);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
