package models;

import java.util.ArrayList;

public class OutputMethods {
    public static void vergleich(int i, double startDepot, ArrayList<String> ticker){
        String tablename = ticker.get(i)+"S1";
        double depotS1 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S2";
        double depotS2 = DBMethods.dbGetDepot(tablename);
        tablename = ticker.get(i)+"S3";
        double depotS3 = DBMethods.dbGetDepot(tablename);

        System.out.println("Endvergleich des Kapitals (" + ticker.get(i) + ")");
        System.out.println("Startkapital: " + String.format("%.2f",startDepot));
        System.out.println("Strategie 1 (200er): " + String.format("%.2f",depotS1) + "$   %-Gewinn: " + String.format("%.2f",((depotS1-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 2 (200er + 3%): " + String.format("%.2f",depotS2) + "$   %-Gewinn: " + String.format("%.2f",((depotS2-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 3 (Buy-and-Hold): " + String.format("%.2f",depotS3) + "$   %-Gewinn: " + String.format("%.2f",((depotS3-startDepot)/startDepot)*100) + "%");
        System.out.println(" ");

    }
    public static void sumDepotOutput(double startDepot, ArrayList<String> ticker){
        double depotS1 = 0;
        double depotS2 = 0;
        double depotS3 = 0;
        String best = "";
        for (int i = 0; i < ticker.size(); i++){
            depotS1 = depotS1 + DBMethods.dbGetDepot(ticker.get(i)+"S1");
            depotS2 = depotS2 + DBMethods.dbGetDepot(ticker.get(i)+"S2");
            depotS3 = depotS3 + DBMethods.dbGetDepot(ticker.get(i)+"S3");
        }
        System.out.println(" ");
        startDepot = startDepot * ticker.size();
        System.out.println("Zusammenfassend [Startkapital: " + Math.round(startDepot) + " $]:");
        System.out.println("Strategie 1 (200er): " + String.format("%.2f",depotS1) + "$   %-Gewinn: " + String.format("%.2f",((depotS1-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 2 (200er + 3%): " + String.format("%.2f",depotS2) + "$   %-Gewinn: " + String.format("%.2f",((depotS2-startDepot)/startDepot)*100) + "%");
        System.out.println("Strategie 3 (Buy-and-Hold): " + String.format("%.2f",depotS3) + "$   %-Gewinn: " + String.format("%.2f",((depotS3-startDepot)/startDepot)*100) + "%");
        if(depotS1 > depotS2 && depotS1 > depotS3){
            best= "200er";
        }
        if(depotS2 > depotS1 && depotS2 > depotS3){
            best= "200er + 3%";
        }
        if(depotS3 > depotS2 && depotS3 > depotS1){
            best= "B&H";
        }
        System.out.println("Am besten: " + best);
        System.out.println(" ");
    }
}
