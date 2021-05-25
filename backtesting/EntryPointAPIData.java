import models.DBMethods;
import models.InputData;
import models.OtherMethods;

import java.util.Locale;

public class EntryPointAPIData {
    //Main-Class to put API-Data in Database

    public static void main(String[] args) {

        String file = "C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt";
        String tablename;
        DBMethods.dbConnect();

        for (int i = 0; i < InputData.tickerGetter(file).size();i++){
            tablename = InputData.tickerGetter(file).get(i);
            DBMethods.dbCreateTableAPIData(tablename);
            InputData.dataGetter(tablename);
            System.out.println("1");
            OtherMethods.splitCorrection(tablename);
            System.out.println("test");
            OtherMethods.avgCalc(tablename);
            System.out.println("Data loading in DB finished");
            DBMethods.dbConnClose();

        }
    }
}
