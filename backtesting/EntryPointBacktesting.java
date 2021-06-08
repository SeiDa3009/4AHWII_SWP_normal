import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class EntryPointBacktesting {
    static String file = "C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt";
    static ArrayList<String> ticker = new ArrayList<>(InputData.tickerGetter(file));
    static LocalDate startdate;

    public static void main(String[] args) {
        Double startDepot = inputStartDepot();
        startdate = inputStartDate();
        DBMethods.dbConnect();
        for(int i = 0; i < ticker.size(); i++){

            strategie1(i,startDepot);
            strategie2(i,startDepot);
            strategie3(i,startDepot);
            vergleich(i,startDepot);
        }
        DBMethods.dbConnClose();
    }
    public static void strategie1(int i, double startdepot){
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
                        buy(i,d,tableName);
                    }
                }
                else if(DBMethods.dbGetFlag(tableName)){
                    if (CheckMethods.check1Sell(ticker.get(i),d)){
                        sell(i,d,tableName);
                    }
                }
            }
        }
        if(DBMethods.dbGetFlag(tableName)){
            LocalDate d = LocalDate.now();
            while (!CheckMethods.isDateValid(ticker.get(i),d)){
                d = d.minusDays(1);
            }
            sell(i,d,tableName);
        }
    }
    public static void strategie2(int i, double startdepot){
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
                        buy(i, d,tableName);
                    }
                }
                else if(DBMethods.dbGetFlag(tableName)){
                    if (CheckMethods.check2Sell(ticker.get(i), d)){
                        sell(i,d,tableName);
                    }
                }
            }
        }
        if(DBMethods.dbGetFlag(tableName)){
            LocalDate d = LocalDate.now();
            while (!CheckMethods.isDateValid(ticker.get(i),d)){
                d = d.minusDays(1);
            }
            sell(i,d,tableName);
        }
    }
    public static void strategie3(int i, double startdepot){
        String tableName = ticker.get(i)+"S3";
        DBMethods.dbDropTable(tableName);
        DBMethods.dbCreateTableStrats(tableName);
        //Buy-and-Hold
        DBMethods.dbInsertStrats(tableName, startdate.minusDays(1),ticker.get(i),0,0,startdepot,0);
        LocalDate d = startdate;
        while(!CheckMethods.isDateValid(ticker.get(i),d)){
            d = d.plusDays(1);
        }
        buy(i,d,tableName);
        d = LocalDate.now();
        while (!CheckMethods.isDateValid(ticker.get(i),d)){
            d = d.minusDays(1);
        }
        sell(i,d,tableName);
    }
    public static void buy(int i, LocalDate d, String tablename) {
        double depot = DBMethods.dbGetDepot(tablename)%DBMethods.dbGetAdjustedClose(ticker.get(i),d);
        int amount = (int) Math.floor(DBMethods.dbGetDepot(tablename)/DBMethods.dbGetAdjustedClose(ticker.get(i),d));
        DBMethods.dbInsertStrats(tablename,d,ticker.get(i),1,amount,depot,DBMethods.dbGetAdjustedClose(ticker.get(i),d));
    }
    public static void sell(int i, LocalDate d, String tablename){
        double depot = DBMethods.dbGetAdjustedClose(ticker.get(i), d) * DBMethods.dbGetAmount(tablename) + DBMethods.dbGetDepot(tablename);
        DBMethods.dbInsertStrats(tablename,d,ticker.get(i),0,0,depot,DBMethods.dbGetAdjustedClose(ticker.get(i),d));
    }
    public static void vergleich(int i, double startDepot){
        String tablename = ticker.get(i)+"S1";
        double depotS1 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S2";
        double depotS2 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S3";
        double depotS3 = DBMethods.dbGetDepot(tablename);

        System.out.println("Endvergleich des Kapitals (" + ticker.get(i) + ")");
        System.out.println("Startkapital: " + String.format("%.2f",startDepot));
        System.out.println("Strategie 1 (200er): " + String.format("%.2f",depotS1) + "$   %-Veränderung: " + String.format("%.2f",((depotS1-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 2 (200er + 3%): " + String.format("%.2f",depotS2) + "$   %-Veränderung: " + String.format("%.2f",((depotS2-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 3 (Buy-and-Hold): " + String.format("%.2f",depotS3) + "$   %-Veränderung: " + String.format("%.2f",((depotS3-startDepot)/startDepot)*100) + "%");
        System.out.println(" ");

    }
    public static double inputStartDepot(){
        Scanner reader = new Scanner(System.in);
        System.out.print("Startkapital [$]: ");
        return Math.round(reader.nextDouble()/ticker.size());
    }
    public static LocalDate inputStartDate(){
        Scanner reader = new Scanner(System.in);
        String date;
        System.out.print("Startdatum [yyyy-mm-dd]: ");
        date = reader.nextLine();
        return LocalDate.parse(date);
    }
}
