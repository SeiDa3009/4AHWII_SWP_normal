import models.DBMethods;
import models.InputData;
import models.OtherMethods;

import java.util.Locale;

public class EntryPointAPIData {
    //Main-Class to put API-Data in Database

    public static void main(String[] args) {

        String file = "C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt";
        String tablename;

        for (int i = 0; i < InputData.tickerGetter(file).size();i++){
            DBMethods.dbConnect();
            tablename = InputData.tickerGetter(file).get(i);
            DBMethods.dbCreateTableAPIData(tablename);
            InputData.dataGetter(tablename);
            OtherMethods.splitCorrection(tablename);
            OtherMethods.avgCalc(tablename);
            System.out.println("Data loading in DB finished [" + i+1 + "/" + InputData.tickerGetter(file).size() + "]");
            DBMethods.dbConnClose();
        }
    }
}
