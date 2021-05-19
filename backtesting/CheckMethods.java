package models;

public class CheckMethods {
    //Close >< 200er
    public static boolean check1Buy(float close, float avg){
        return close > avg;
    }
    public static boolean check1Sell(float close, float avg){
        return close < avg;
    }
    //Close >< 200er + 3%
    public static boolean check2Buy(float close, float avg){
        float temp = avg * Float.parseFloat("1.03");
        return close > temp;
    }
    public static boolean check2Sell(float close, float avg){
        float temp = avg * Float.parseFloat("0.97");
        return close < temp;
    }
}
