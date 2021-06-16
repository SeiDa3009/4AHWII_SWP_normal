package models;

import java.time.LocalDate;
import java.util.ArrayList;

public class StratMethods {

    public static void strategie1(int i, double startdepot, LocalDate startdate, ArrayList<String> ticker){
        String tableName = ticker.get(i)+"S1";
        DBMethods.dbDropTable(tableName);
        DBMethods.dbCreateTableStrats(tableName);
        DBMethods.dbInsertStrats(tableName, startdate.minusDays(1),ticker.get(i),0,0,startdepot,0);
        for(LocalDate d = startdate; d.isBefore(LocalDate.now()); d=d.plusDays(1)){
            //Überprüfen ob Feiertag,etc
            if(CheckMethods.isDateValid(ticker.get(i), d)){
                //Sell or Buy
                if(!DBMethods.dbGetFlag(tableName)){
                    //BUY
                    if(CheckMethods.check1Buy(ticker.get(i), d)){
                        buy(i,d,tableName,ticker);
                    }
                }
                else if(DBMethods.dbGetFlag(tableName)){
                    if (CheckMethods.check1Sell(ticker.get(i),d)){
                        sell(i,d,tableName,ticker);
                    }
                }
            }
        }
        if(DBMethods.dbGetFlag(tableName)){
            LocalDate d = LocalDate.now();
            while (!CheckMethods.isDateValid(ticker.get(i),d)){
                d = d.minusDays(1);
            }
            sell(i,d,tableName,ticker);
        }
    }
    public static void strategie2(int i, double startdepot, LocalDate startdate, ArrayList<String> ticker){
        String tableName = ticker.get(i)+"S2";
        DBMethods.dbDropTable(tableName);
        DBMethods.dbCreateTableStrats(tableName);
        //200er + 3%
        DBMethods.dbInsertStrats(tableName, startdate.minusDays(1),ticker.get(i),0,0,startdepot,0);
        for(LocalDate d = startdate; d.isBefore(LocalDate.now()); d=d.plusDays(1)){
            //Überprüfen ob Feiertag,etc
            if(CheckMethods.isDateValid(ticker.get(i), d)){
                //Sell or Buy
                if(!DBMethods.dbGetFlag(tableName)){
                    //BUY
                    if(CheckMethods.check2Buy(ticker.get(i), d)){
                        buy(i, d,tableName,ticker);
                    }
                }
                else if(DBMethods.dbGetFlag(tableName)){
                    if (CheckMethods.check2Sell(ticker.get(i), d)){
                        sell(i,d,tableName,ticker);
                    }
                }
            }
        }
        if(DBMethods.dbGetFlag(tableName)){
            LocalDate d = LocalDate.now();
            while (!CheckMethods.isDateValid(ticker.get(i),d)){
                d = d.minusDays(1);
            }
            sell(i,d,tableName,ticker);
        }
    }
    public static void strategie3(int i, double startdepot, LocalDate startdate, ArrayList<String> ticker){
        String tableName = ticker.get(i)+"S3";
        DBMethods.dbDropTable(tableName);
        DBMethods.dbCreateTableStrats(tableName);
        //Buy-and-Hold
        DBMethods.dbInsertStrats(tableName, startdate.minusDays(1),ticker.get(i),0,0,startdepot,0);
        LocalDate d = startdate;
        while(!CheckMethods.isDateValid(ticker.get(i),d)){
            d = d.plusDays(1);
        }
        buy(i,d,tableName,ticker);
        d = LocalDate.now();
        while (!CheckMethods.isDateValid(ticker.get(i),d)){
            d = d.minusDays(1);
        }
        sell(i,d,tableName,ticker);
    }
    public static void buy(int i, LocalDate d, String tablename, ArrayList<String> ticker) {
        double depot = DBMethods.dbGetDepot(tablename)%DBMethods.dbGetAdjustedClose(ticker.get(i),d);
        int amount = (int) Math.floor(DBMethods.dbGetDepot(tablename)/DBMethods.dbGetAdjustedClose(ticker.get(i),d));
        DBMethods.dbInsertStrats(tablename,d,ticker.get(i),1,amount,depot,DBMethods.dbGetAdjustedClose(ticker.get(i),d));
    }
    public static void sell(int i, LocalDate d, String tablename, ArrayList<String> ticker){
        double depot = DBMethods.dbGetAdjustedClose(ticker.get(i), d) * DBMethods.dbGetAmount(tablename) + DBMethods.dbGetDepot(tablename);
        DBMethods.dbInsertStrats(tablename,d,ticker.get(i),0,0,depot,DBMethods.dbGetAdjustedClose(ticker.get(i),d));
    }
}
