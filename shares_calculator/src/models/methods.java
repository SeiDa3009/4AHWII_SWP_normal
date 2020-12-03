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
        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="     //api-link + daily updates + which share + token
                + shortenSymbol + "&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));                                       //link to get the Data
        JSONObject objectOne = new JSONObject(link);
        JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");
        for (int i = 0; i < 100; i++){
            System.out.print(objectTwo.names().get(i) + ": ");                                                                  //print the dates of the api in the console
            dates.add(LocalDate.parse((CharSequence) objectTwo.names().get(i)));                                                //add the dates in a arraylist
            System.out.println(objectTwo.getJSONObject(objectTwo.names().getString(i)).get("4. close"));                        //print the share index of the api in the console
        }
        dates.sort(null);                                                                                                    //sorts the arraylist
        System.out.println(dates);                                                                                              //print the sorted days in the console
    }

    public String sharesSelect(){
            System.out.print("Select Your Share (Shortcut): ");                                                                 //input: which share your want
            return reader.next();
    }

    public void setdatabase(){
        DBUrl = "jdbc:sqlite:C:/Users/david/OneDrive/Schule/4AHWII/SWP/SWP-Rubner/Normal_github/projects/sharesCalculator/" + sharesSelect() + ".db";       //database directory
        String sql;
        try{
            Connection conn = DriverManager.getConnection(DBUrl);                                                                                           //get connected to the database
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
