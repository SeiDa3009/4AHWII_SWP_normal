package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OtherMethods {
    public static ArrayList<Float> avgCalc(ArrayList<Float> closeValue){
        ArrayList<Float> avg = new ArrayList<>();
        int count = 1;
        float temp = 0;

        for(int i = 0; i < closeValue.size(); i++){
            if (i < 200){
                temp = temp + closeValue.get(i);
                avg.add(temp/(i+1));
            }
            if (i >= 200){
                temp = temp - closeValue.get(i-200);
                temp = temp + closeValue.get(i);
                avg.add(temp/200);
            }
        }
        return avg;
    }
    public static ArrayList<LocalDate> getLocalDateFromHashMap (Map<LocalDate, Float> stockValues){
        ArrayList<LocalDate> dates = new ArrayList<>();
        for (Map.Entry<LocalDate, Float> e : stockValues.entrySet()){
            dates.add(e.getKey());
        }
        return dates;
    }
    public static ArrayList<Float> getCloseFromHashMap (Map<LocalDate, Float> stockValues){
        ArrayList<Float> closeValue = new ArrayList<>();
        for (Map.Entry<LocalDate, Float> e : stockValues.entrySet()){
            closeValue.add(e.getValue());
        }
        return closeValue;
    }
    public static void clearLists(ArrayList<String> dates, ArrayList<Float> close, ArrayList<Float> avg){
        dates.clear();
        close.clear();
        avg.clear();
        avgCalc(close).clear();
    }
}
