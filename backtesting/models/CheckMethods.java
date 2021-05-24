package models;

import java.time.LocalDate;

public class CheckMethods {
    //Close >< 200er
    public static boolean check1Buy(String tablename, LocalDate date){
        try {
            return DBMethods.dbGetAdjustedClose(tablename, date) > DBMethods.dbGetAvg(tablename,date);
        }catch (NullPointerException e){
            System.out.println("Wert ist null!");
        }
        return false;
    }
    public static boolean check1Sell(String tablename,LocalDate date){
        try {
            return DBMethods.dbGetAdjustedClose(tablename, date) < DBMethods.dbGetAvg(tablename, date);
        }catch (NullPointerException e){
            System.out.println("Wert ist null!");
        }
        return false;
    }
    //Close >< 200er + 3%
    public static boolean check2Buy(String tablename, LocalDate date){
        try {
            float temp = DBMethods.dbGetAvg(tablename, date) * Float.parseFloat("1.03");
            return DBMethods.dbGetAdjustedClose(tablename, date) > temp;
        }catch (NullPointerException e){
            System.out.println("Wert ist null!");
        }
        return false;
    }
    public static boolean check2Sell(String tablename, LocalDate date){
        try {
            float temp = DBMethods.dbGetAvg(tablename, date) * Float.parseFloat("0.97");
            return DBMethods.dbGetAdjustedClose(tablename, date) < temp;
        }catch (NullPointerException e){
            System.out.println("Wert ist null!");
        }
        return false;
    }
    public static boolean isDateValid(String tablename, LocalDate date){
        try {
            if (DBMethods.dbGetValidDate(tablename, date)){
                return true;
            }
        }catch (NullPointerException e){
            System.out.println("Wert ist null!");
        }
        return false;
    }
}
