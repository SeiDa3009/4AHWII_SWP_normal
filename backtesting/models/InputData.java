package models;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class InputData {

    public static void dataGetter(String ticker) {
        try{
            String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + ticker + "&outputsize=full&apikey=N4XI1HLI5LGAYT87"), Charset.forName("UTF-8"));
            JSONObject objectOne = new JSONObject(link);
            JSONObject objectTwo = objectOne.getJSONObject("Time Series (Daily)");
            for (int i = 0; i < objectTwo.names().length(); ++i) {
                DBMethods.dbInsertAPIData(ticker, LocalDate.parse((CharSequence) objectTwo.names().get(i)) , objectTwo.getJSONObject(LocalDate.parse((CharSequence) objectTwo.names().get(i)).toString()).getFloat("4. close"), objectTwo.getJSONObject(LocalDate.parse((CharSequence) objectTwo.names().get(i)).toString()).getFloat("8. split coefficient"));
            }
        }catch (IOException e){
            System.out.println("Fehler dataGetter: " + e.getMessage());
        }
    }

    public static ArrayList<String> tickerGetter(String file){
        ArrayList<String> txtOutput = new ArrayList<>();
        BufferedReader f;
        String line;

        try {
            f = new BufferedReader(new FileReader(file));
            while ((line = f.readLine()) != null){
                txtOutput.add(line.toUpperCase());
            }
            f.close();
        }catch (IOException e){
            System.out.println("Text auslesen: " + e.getMessage());
        }
        return txtOutput;
    }
    public static double inputStartDepot(ArrayList<String> ticker){
        Scanner reader = new Scanner(System.in);
        double depot;
        int i = 0;

        do {
            depot = 0.0;
            i++;
            System.out.print("Startkapital [$]: ");
            depot = Math.round(reader.nextDouble()/ticker.size());
        }while (depot < 0.0 && i <= 2);
        if (i > 3){
            System.exit(0);
        }
        return depot;
    }
    public static LocalDate inputStartDate(ArrayList<String> ticker){
        Scanner reader = new Scanner(System.in);
        LocalDate date = null;
        boolean check;
        int i = 0;
        do{
            check = false;
            i++;
            System.out.print("Startdatum [yyyy-mm-dd]: ");
            try {
                date = LocalDate.parse(reader.nextLine());
            }catch (Exception e){
                check = true;
            }
        }while(i <= 2 && check);
        if (i > 3){
            System.exit(0);
        }
        return date;
    }
}
