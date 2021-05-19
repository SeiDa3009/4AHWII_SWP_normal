package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OtherMethods {
    public static Map<LocalDate, Float> avgCalc(ArrayList<LocalDate> dates,ArrayList<Float> closeValue){
        Map<LocalDate,Float> avg = new TreeMap<>();
        int count = 1;
        float temp = 0;

        for(int i = 0; i < closeValue.size(); i++){
            if (i < 200){
                temp = temp + closeValue.get(i);
                avg.put(dates.get(i),temp/(i+1));
            }
            if (i >= 200){
                temp = temp - closeValue.get(i-200);
                temp = temp + closeValue.get(i);
                avg.put(dates.get(i),temp/200);
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
}
