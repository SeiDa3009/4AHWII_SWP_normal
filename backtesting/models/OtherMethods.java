package models;

import java.time.LocalDate;
import java.util.*;

public class OtherMethods {
    public static void avgCalc(String tablename){
        ArrayList<Float> adjustedClose = DBMethods.dbGetAdjustedClose(tablename);
        ArrayList<Float> avg = new ArrayList<>();
        float temp = 0;

        for(int i = 0; i < adjustedClose.size(); i++){
            if (i < 200){
                temp = temp + adjustedClose.get(i);
                avg.add(temp/(i+1));
            }
            if (i >= 200){
                temp = temp - adjustedClose.get(i-200);
                temp = temp + adjustedClose.get(i);
                avg.add(temp/200);
            }
        }
        DBMethods.dbInsertAverage(tablename,avg);
    }
    public static void splitCorrection(String tablename){
        ArrayList<Float> adjustedClose = new ArrayList<>();
        ArrayList<Float> coefficient = DBMethods.dbGetCoefficient(tablename);
        ArrayList<Float> close = DBMethods.dbGetCloseReverse(tablename);
        float divisor = 1;
        float tempcoeff = 1;

        for (int i = 0; i < coefficient.size(); i++){
            if (tempcoeff == coefficient.get(i)){
                adjustedClose.add(close.get(i)/divisor);
                tempcoeff = coefficient.get(i);
            }
            else{
                adjustedClose.add(close.get(i)/divisor);
                divisor = divisor * coefficient.get(i);
                tempcoeff = 1;
            }

        }
        Collections.reverse(adjustedClose);
        DBMethods.dbInsertAdjustedClose(tablename,adjustedClose);
    }
}
