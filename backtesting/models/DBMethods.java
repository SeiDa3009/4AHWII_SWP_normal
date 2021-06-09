package models;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DBMethods {

    public static String urlDB = "jdbc:mysql://localhost:3306/backtestingdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static Statement stmt;
    private static Connection connection;
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<Float> close = new ArrayList<>();
    public static ArrayList<Float> avg = new ArrayList<>();


    //DB-Methods for Backtesting
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
    public static boolean dbCreateTableStrats(String tablename){
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + tablename + " (date date primary key, ticker varchar(5), flag bool, amount integer, depot double, close float );";
            stmt.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("Methode dbCreateTableStrats: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public static void dbInsertStrats(String tablename, LocalDate date, String ticker, int flag, int amount, double depot, float close){
        try {
            stmt = connection.createStatement();
                String sql = "INSERT INTO " + tablename + " (date,ticker,flag,amount,depot, close) VALUES ('" + date + "', '" + ticker + "', '" + flag + "', '" + amount + "', '" + depot +"', '" + close + "');";
                stmt.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("Methode dbInsertStrats: " + e.getMessage());
        }
    }
    public static int dbGetAmount(String tablename){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT amount FROM " + tablename + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){
                return rs.getInt("amount");
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetAmount: " + e.getMessage());
        }
        return 0;
    }
    public static double dbGetDepot(String tablename){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT depot FROM " + tablename + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){
                return rs.getDouble("depot");
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetDepot: " + e.getMessage());
        }
        return 0;
    }
    public static boolean dbGetFlag(String tablename){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT flag FROM " + tablename + " ORDER BY date DESC LIMIT 1");
            if(rs.next()){

                return rs.getBoolean("flag");
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetFlag: " + e.getMessage());
        }
        return false;
    }
    public static void dbDropTable(String tablename){
        try {
            stmt = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS " + tablename;
            stmt.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("DB Drop Table: " + e.getMessage());
        }
    }
    public static Float dbGetAvg(String tablename, LocalDate date){
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT avgClose FROM " + tablename + " WHERE date = '" + date + "' ORDER BY date ASC");
            if (rs.next()){
                return rs.getFloat("avgClose");
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetAvg: " + e.getMessage());
        }
        return null;
    }
    public static Float dbGetAdjustedClose(String tablename, LocalDate date){
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT adjustedClose FROM " + tablename + " WHERE date = '" + date + "' ORDER BY date ASC");
            if (rs.next()){
                return rs.getFloat("adjustedClose");
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetAdjustedClose: " + e.getMessage());
        }
        return null;
    }
    public static boolean dbGetValidDate(String tablename, LocalDate date){
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM " + tablename + " WHERE date = '" + date + "' ORDER BY date ASC");
            if (rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetDate: " + e.getMessage());
        }
        return false;
    }
    public static void dbConnClose(){
        try {
            connection.close();
        }catch (SQLException e){
            System.out.println("Methode dbConnClose: " + e.getMessage());
        }
    }


    //DB-Methods for APIData
    public static boolean dbCreateTableAPIData(String tablename){
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + tablename + " (date date primary key, close float, adjustedClose float, avgClose float, coefficient float);";
            stmt.executeUpdate(sql);
            return true;
        }catch (Exception e){
            System.out.println("Methode");
        }
        return false;
    }
    public static void dbInsertAPIData(String tablename, LocalDate date, float close, float coefficient){
        try {
            stmt = connection.createStatement();
            String sql = "INSERT IGNORE INTO " + tablename + " (date, close, coefficient) VALUES ('" + date + "', '" + close + "', '" + coefficient + "');";
            stmt.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("Methode dbInsertAPIData: " + e.getMessage());
        }
    }
    public static void dbInsertAverage(String tablename, ArrayList<Float> averageClose){
        String sql;
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < averageClose.size(); i++){
                sql = "Update " + tablename + " SET avgClose = " + averageClose.get(i) + " WHERE date = '" + dbGetDate(tablename).get(i) + "'";
                stmt.executeUpdate(sql);
            }
        }catch (SQLException e){
            System.out.println("Methode dbInsertAverage :" + e.getMessage());
        }
    }
    public static void dbInsertAdjustedClose(String tablename, ArrayList<Float> adjustedClose){
        String sql;
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < dbGetDate(tablename).size();i++){
                sql = "UPDATE " + tablename + " SET adjustedClose = " + adjustedClose.get(i) + " WHERE date = '" + DBMethods.dbGetDate(tablename).get(i) + "'";
                stmt.executeUpdate(sql);
            }
        }catch (SQLException e){
            System.out.println("Methode dbInsertAdjustedClose: " + e.getMessage());
        }
    }
    public static ArrayList<Date> dbGetDate(String tablename){
        ArrayList<Date> dates = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM " + tablename + " ORDER BY date ASC");
            while (rs.next()){
                dates.add(rs.getDate("date"));
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetDate: " + e.getMessage());
        }
        return dates;
    }
    public static ArrayList<Float> dbGetCoefficient(String tablename){
        ArrayList<Float> coefficient = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT coefficient FROM " + tablename + " ORDER BY date DESC");
            while (rs.next()){
                coefficient.add(rs.getFloat("coefficient"));
            }
            }catch(SQLException e){
            System.out.println("Methode dbGetCoefficient: " + e.getMessage());
        }
        return coefficient;
    }
    public static ArrayList<Float> dbGetClose(String tablename){
        ArrayList<Float> close = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT close FROM " + tablename + " ORDER BY date ASC");
            while (rs.next()){
                close.add(rs.getFloat("close"));
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetClose: " + e.getMessage());
        }
        return close;
    }
    public static ArrayList<Float> dbGetCloseReverse(String tablename){
        ArrayList<Float> close = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT close FROM " + tablename + " ORDER BY date DESC");
            while (rs.next()){
                close.add(rs.getFloat("close"));
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetClose: " + e.getMessage());
        }
        return close;
    }
    public static ArrayList<Float> dbGetAdjustedClose(String tablename){
        ArrayList<Float> adjustedClose = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT adjustedClose FROM " + tablename + " ORDER BY date ASC");
            while (rs.next()){
                adjustedClose.add(rs.getFloat("adjustedClose"));
            }
        }catch(SQLException e){
            System.out.println("Methode dbGetAdjustedClose: " + e.getMessage());
        }
        return adjustedClose;
    }

    //DB-Methods for Graph
    public static List<Double> dbGetDepotForGraph (String tablename){
        ArrayList<Double> depot = new ArrayList<>();
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT depot FROM " + tablename + " WHERE flag = '0' ORDER BY date ASC");
            while (rs.next()){
                depot.add(rs.getDouble("depot"));
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetDepotForGraph: " + e.getMessage());
        }
        return depot;
    }
    public static List<String> dbGetDateForGraph (String tablename){
        ArrayList<String> date = new ArrayList<>();
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM " + tablename + " WHERE flag = '0' ORDER BY date ASC");
            while (rs.next()){
                date.add(rs.getDate("date").toString());
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetDateForGraph: " + e.getMessage());
        }
        return date;
    }
}

