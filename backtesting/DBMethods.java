package models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBMethods {

    public static String urlDB = "jdbc:mysql://localhost:3306/backtestingdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static Statement stmt;
    private static Connection connection;
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<Float> close = new ArrayList<>();
    public static ArrayList<Float> avg = new ArrayList<>();

    public static boolean dbConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(urlDB, "root", "Ds.300902");
            stmt = connection.createStatement();
            System.out.println("Datenbank verknüpft!");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Methode dbConnect: " + e.getMessage());
        }
        return false;
    }
    public static boolean dbCreateTable(String ticker){
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS backtesting" + ticker + " (date date primary key, ticker varchar(5), flag boolean, amount integer, depot double);";
            stmt.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("Methode dbCreateTable: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public static void dbInsert(String table, LocalDate date, String ticker, Boolean flag, int amount, double depot){
        try {
            stmt = connection.createStatement();
                String sql = "INSERT IGNORE INTO backtesting" + table + " (date,ticker,flag,amount,depot) VALUES ('" + date + "', '" + ticker + "', '" + flag + "', '" + amount + "', '" + depot +"');";
                stmt.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("Methode dbInsert: " + e.getMessage());
        }
    }
    public static int dbGetAmount(String ticker){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT amount FROM backtesting" + ticker + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){
                return rs.getInt("amount");
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetAmount: " + e.getMessage());
        }
        return 0;
    }
    public static double dbGetDepot(String ticker){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT depot FROM backtesting" + ticker + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){
                return rs.getDouble("depot");
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetDepot: " + e.getMessage());
        }
        return 0;
    }
    public static boolean dbGetFlag(String ticker){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT flag FROM backtesting" + ticker + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){
                return rs.getBoolean("flag");
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetFlag: " + e.getMessage());
        }
        return false;
    }
}
