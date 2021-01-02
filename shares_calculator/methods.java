package models;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class methods {
    private static String DBUrl;
    public static String share;
    public static String filename = "jdbc:sqlite:D:/David/OneDrive/Schule/4AHWII/SWP/SWP-Rubner/Normal_github/Projekte/shares_calculator/shares.db";
    private static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
    private static HashMap<LocalDate, String> sharesPerDates = new HashMap<>();
    private static Scanner reader = new Scanner(System.in);

    public void dataGetter(String shortenSymbol) throws JSONException,MalformedURLException, IOException{
        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                + shortenSymbol + "&outputsize=full&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));
        JSONObject objectOne = new JSONObject(link);
        JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");
        for (int i = 0; i < objectTwo.names().length(); i++){
            //add the dates in a arraylist
            dates.add(LocalDate.parse((CharSequence) objectTwo.names().get(i)));
            sharesPerDates.put(LocalDate.parse((CharSequence) objectTwo.names().get(i)), (String) objectTwo.getJSONObject(objectTwo.names().getString(i)).get("4. close"));
        }
        //sorts the arraylist
        dates.sort(null);

        //print shares in correct order
        //for(int i = dates.size()-1; i >= 0; i--) {
            //System.out.println(dates.get(i)+ "  " + sharesPerDates.get(dates.get(i)));
        //}
    }
    public String sharesSelect(){
        //input: which share your want
        System.out.print("Select Your Share (Shortcut): ");
        share = reader.next();

        return share;
    }
    private void connect() {
        try {
            dataGetter(sharesSelect());

        }catch (IOException e){
            e.getMessage();
        }
        Connection conn = null;
        try {
            String url = filename;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createDatabase(){
        DBUrl = filename;       //database directory
        String sql;
        try{
            Connection conn = DriverManager.getConnection(DBUrl);                                                                //get connected to the database
            if(conn != null){
                System.out.println("Drivername: " + conn.getMetaData());
                System.out.println("Database has been created");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private void createTable(){
        String url = filename;
        String sql = "CREATE TABLE IF NOT EXISTS " + share + "(\n"
                + "date date PRIMARY KEY UNIQUE, \n"
                + "shares float, \n"
                + "avg float);";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private Connection connectTODB(){
        String url = filename;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
    private void insert(){
        int movingAVG = getAverage();
        int count = 1;
        float temp = 0;
        float avg = 0;
        //Insert Or Ignore into wäre schneller, geht aber nicht, weil wir sonst nicht die avg row ändern können
        String sql = "REPLACE INTO " + share +"(date, shares, avg) VALUES (?,?,?)";
        try {
            Connection conn = this.connectTODB();
            for(int i = 0; i < dates.size(); i++){
                if (count == movingAVG+1){
                    temp = temp - Float.parseFloat(sharesPerDates.get(dates.get(i-movingAVG))) + Float.parseFloat(sharesPerDates.get(dates.get(i)));
                    avg = temp / movingAVG;
                }
                if (count <= movingAVG){
                    temp = temp + Float.parseFloat(sharesPerDates.get(dates.get(i)));
                    avg = temp / count;
                    count++;
                }
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setDate(1, Date.valueOf(dates.get(i)));
                pstmt.setFloat(2, Float.parseFloat(sharesPerDates.get(dates.get(i))));
                pstmt.setFloat(3, avg);
                pstmt.executeUpdate();
            }



        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private void selectAllData(){
        String sql = "SELECT * FROM " + share + " ORDER BY DATE ASC";
        try{
            Connection conn = this.connectTODB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n   Datum       Aktienwert        Gleitender Durchschnitt");
            while (rs.next()) {
                System.out.println(rs.getDate("date") + "  |  " + rs.getFloat("shares") + "  |  " + rs.getFloat("avg"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public int getAverage(){
        int limit;
        System.out.print("Gleitender Durchschnitt [20, 38 ,50, 100, 200]: ");
        limit = reader.nextInt();
        if (limit == 20 || limit == 38 || limit == 50 || limit == 100 || limit == 200){
            return limit;
        }
        else{
            System.out.println("Fehler");
            System.out.println("Standartwert wird übermittelt!");
            return 200;
        }
    }
    public void db(){
        connect();
        createDatabase();
        createTable();
        insert();
        selectAllData();
    }
}
