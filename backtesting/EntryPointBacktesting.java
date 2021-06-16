import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class EntryPointBacktesting {
    static String file = "C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt";
    static ArrayList<String> ticker = new ArrayList<>(InputData.tickerGetter(file));
    static LocalDate startdate;

    public static void main(String[] args) {
        Double startDepot = InputData.inputStartDepot(ticker);
        startdate = InputData.inputStartDate(ticker);
        DBMethods.dbConnect();
        for(int i = 0; i < ticker.size(); i++){

            StratMethods.strategie1(i,startDepot,startdate,ticker);
            StratMethods.strategie2(i,startDepot,startdate,ticker);
            StratMethods.strategie3(i,startDepot,startdate,ticker);
            OutputMethods.vergleich(i,startDepot,ticker);
        }
        EntryPointGraph.main(null);
        OutputMethods.sumDepotOutput(startDepot,ticker);
        DBMethods.dbConnClose();
    }
}
