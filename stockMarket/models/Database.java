package models;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    public static String DBurl = "jdbc:mysql://localhost:3306/db_stocks?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static Statement stmt;
    private static Connection connection;
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<Float> close = new ArrayList<>();
    public static ArrayList<Float> avg = new ArrayList<>();

    public static boolean dbConnect(){
        try {
            connection = DriverManager.getConnection(DBurl,"root","password");
            stmt = connection.createStatement();
            System.out.println("Datenbank verknüpft");
            return true;
        }catch (SQLException e){
            System.out.println("Methode dbConnect: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public static boolean dbCreateTable(String tablename){
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + tablename + " (dates date primary key, close float, avg float);";
            stmt.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("Methode dbCreateTable: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public static void dbInsert(ArrayList<LocalDate> dates, ArrayList<Float> closeValue, ArrayList<Float> avgValue, String tablename){
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < dates.size(); i++){
                String sql = "INSERT IGNORE INTO " + tablename + " (dates,close,avg) VALUES ('" + dates.get(i) + "', '" + closeValue.get(i) + "', '" + avgValue.get(i) + "');";
                stmt.executeUpdate(sql);
            }
        }catch (SQLException e){
            System.out.println("Methode dbInsert: " + e.getMessage());
        }
    }
    public static void dbAllGetData(String tablename){
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tablename + " ORDER BY dates ASC");
            System.out.println("Stock: " + tablename);
            System.out.println("Datum               Close               Avg");
            while (rs.next()){
                System.out.println(
                    rs.getDate("dates") + "\t\t\t\t" + rs.getFloat("close") + "\t\t\t\t" + rs.getFloat("avg"));
                    dates.add(rs.getDate("dates").toString());
                    close.add(rs.getFloat("close"));
                    avg.add(rs.getFloat("avg"));
            }
        }catch (SQLException e){
            System.out.println("Methode dbGetData: " + e.getMessage());
        }
    }
}
