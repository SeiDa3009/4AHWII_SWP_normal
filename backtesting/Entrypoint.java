import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class Entrypoint {
    static ArrayList<String> ticker = new ArrayList<>(InputData.tickerGetter("C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt"));
    static LocalDate startdate = LocalDate.of(2010,1,1);
    public static void main(String[] args) {
        Double startDepot = 10000.00;
        DBMethods.dbConnect();
        for(int i = 0; i < ticker.size(); i++){
            Map<LocalDate, Float> stockvalues = InputData.dataGetter(ticker.get(i), startdate);
            System.out.println("Test");
            strategie1(stockvalues,i,startDepot);
            System.out.println("Test");
            //strategie2(stockvalues,i,startDepot);
            //strategie3(stockvalues,i,startDepot);
            vergleich(i,startDepot);
        }


    }
    public static void strategie1(Map<LocalDate,Float> stockvalues, int i, double startdepot){
        String tableName = ticker.get(i)+"S1";
        DBMethods.dbCreateTable(tableName);
        System.out.println(tableName);
        DBMethods.dbInsert(tableName, startdate.minusDays(1),ticker.get(i),false,0,startdepot);
        for(LocalDate d = startdate; d.isBefore(LocalDate.now()); d=d.plusDays(1)){
            //Überprüfen ob Feiertag,etc
            if(stockvalues.containsKey(d)){
                //Sell or Buy
                if(!DBMethods.dbGetFlag(tableName)){
                    //BUY
                    if(CheckMethods.check1Buy(stockvalues.get(d),OtherMethods.avgCalc(OtherMethods.getLocalDateFromHashMap(stockvalues),OtherMethods.getCloseFromHashMap(stockvalues)).get(d))){
                        buy(stockvalues, i, d,tableName);
                    }
                }
                else if(DBMethods.dbGetFlag(tableName)){
                    if(d.isEqual(LocalDate.now().minusDays(1))){
                        sell(stockvalues,i,d,tableName);
                    }
                    else if (CheckMethods.check1Sell(stockvalues.get(d), OtherMethods.avgCalc(OtherMethods.getLocalDateFromHashMap(stockvalues),OtherMethods.getCloseFromHashMap(stockvalues)).get(d))){
                        sell(stockvalues,i,d,tableName);
                    }
                }
            }
        }
    }
    public static void strategie2(Map<LocalDate,Float> stockvalues, int i, double startdepot){
        String tableName = ticker.get(i)+"S2";
        DBMethods.dbCreateTable(tableName);
        //200er + 3%
        DBMethods.dbInsert(tableName, startdate.minusDays(1),ticker.get(i),false,0,startdepot);
        for(LocalDate d = startdate; d.isBefore(LocalDate.now()); d=d.plusDays(1)){
            //Überprüfen ob Feiertag,etc
            if(stockvalues.containsKey(d)){
                //Sell or Buy
                if(DBMethods.dbGetFlag(ticker.get(i))){
                    //BUY
                    if(CheckMethods.check2Buy(stockvalues.get(d),OtherMethods.avgCalc(OtherMethods.getLocalDateFromHashMap(stockvalues),OtherMethods.getCloseFromHashMap(stockvalues)).get(d))){
                        buy(stockvalues, i, d,tableName);
                    }
                }
                else if(!DBMethods.dbGetFlag(ticker.get(i))){
                    if(d.isEqual(LocalDate.now().minusDays(1))){
                        sell(stockvalues,i,d,tableName);
                    }
                    else if (CheckMethods.check2Sell(stockvalues.get(d), OtherMethods.avgCalc(OtherMethods.getLocalDateFromHashMap(stockvalues),OtherMethods.getCloseFromHashMap(stockvalues)).get(d))){
                        sell(stockvalues,i,d,tableName);
                    }
                }
            }
        }
    }
    public static void strategie3(Map<LocalDate,Float> stockvalues, int i, double startdepot){
        String tableName = ticker.get(i)+"S3";
        DBMethods.dbCreateTable(tableName);
        //Buy-and-Hold
        DBMethods.dbInsert(tableName, startdate.minusDays(1),ticker.get(i),false,0,startdepot);
        LocalDate d = startdate;
        while(!stockvalues.containsKey(d)){
            d = d.plusDays(1);
        }
        buy(stockvalues,i,d,tableName);
        sell(stockvalues,i,LocalDate.now().minusDays(1),tableName);
    }
    public static void buy(Map<LocalDate, Float> stockvalues, int i, LocalDate d, String tablename) {
        double depot = DBMethods.dbGetDepot(tablename)%stockvalues.get(d);
        int amount = (int) Math.floor(DBMethods.dbGetDepot(tablename)/stockvalues.get(d));
        System.out.println(depot);
        System.out.println("B: " + amount);
        System.out.println(Math.floor(DBMethods.dbGetDepot(tablename)/stockvalues.get(d)));
//        System.out.println(DBMethods.dbGetDepot(tablename));

        DBMethods.dbInsert(tablename,d,ticker.get(i),true,amount,depot);
    }
    public static void sell(Map<LocalDate,Float> stockvalues, int i, LocalDate d, String tablename){
        double depot = stockvalues.get(d)*DBMethods.dbGetAmount(tablename) + DBMethods.dbGetDepot(tablename);
        System.out.println(depot);
        System.out.println(DBMethods.dbGetDepot(tablename));
        DBMethods.dbInsert(tablename,d,ticker.get(i),false,0,depot);
    }
    public static void vergleich(int i, double startDepot){
        String tablename = ticker.get(i)+"S1";
        double depotS1 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S2";
        double depotS2 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S3";
        double depotS3 = DBMethods.dbGetDepot(tablename);

        System.out.println("Endvergleich des Kapitals");
        System.out.println("Startkapital: " + startDepot + "$");
        System.out.println("Strategie 1 (200er): " + depotS1 + "$   %-Veränderung: " + (depotS1/startDepot)*100 + "%");
        System.out.println("Strategie 2 (200er + 3%): " + depotS2 + "$   %-Veränderung: " + (depotS2/startDepot)*100 + "%");
        System.out.println("Strategie 3 (Buy-and-Hold): " + depotS3 + "$   %-Veränderung: " + (depotS3/startDepot)*100 + "%");

    }




}

