import com.sun.javafx.iio.ios.IosDescriptor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class holidaysCounterWithAPIMain extends Application{


    static Scanner reader = new Scanner(System.in);
    static int Startyear, years;
    static String url;
    static List<LocalDate> holidayDates = new ArrayList<>();
    static int monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, allTogether = 0;
    final static String itemA = "Monday";
    final static String itemB = "Tuesday";
    final static String itemC = "Wednesday";
    final static String itemD = "Thursday";
    final static String itemE = "Friday";




    public static void main(String[] args) throws JSONException, MalformedURLException, IOException {
        holidaysCounterWithAPIMain holidaysCounterMain = new holidaysCounterWithAPIMain();
        holidaysCounterMain.InputStartYear();
        holidaysCounterMain.DataInList();
        holidaysCounterMain.checkIfFixedHoliday();
        holidaysCounterMain.showResults();
        launch(args);
    }
    private void InputStartYear(){
        System.out.print("Startyear: ");
        Startyear = reader.nextInt();
        System.out.print("How many Years?: ");
        years = reader.nextInt();
        url = "https://date.nager.at/api/v2/PublicHolidays/" + Startyear +"/AT";

    }
    void checkIfFixedHoliday(){
        for (int i = 0; i < holidayDates.size(); i++){

            switch(holidayDates.get(i).getDayOfWeek()){
                case MONDAY:
                    monday++;
                    break;
                case TUESDAY:
                    tuesday++;
                    break;
                case WEDNESDAY:
                    wednesday++;
                    break;
                case THURSDAY:
                    thursday++;
                    break;
                case FRIDAY:
                    friday++;
                    break;
            }
        }
    }
    void DataInList() throws IOException {
        for(int k = 0; k < years;k++) {
            String date = IOUtils.toString(new URL(url), Charset.forName("UTF-8")).replace("[", "").replace("]", "");
            String[] splittedJSON = date.split("},*");
            for (int i = 0; i < splittedJSON.length; i++) {
                String s = splittedJSON[i];
                String[] datedSplitted = s.split(":");
                String[] finalDate = datedSplitted[1].split(",");
                holidayDates.add(LocalDate.parse(finalDate[0].replace("\"", "")));
            }
            Startyear++;
            url = "https://date.nager.at/api/v2/PublicHolidays/" + Startyear +"/AT";
        }
    }
    void showResults(){
        System.out.println("\nHolidays:");
        System.out.println("Monday: " + monday + " days");
        System.out.println("Tuesday: " + tuesday + " days");
        System.out.println("Wednesday: " + wednesday + " days");
        System.out.println("Thursday: " + thursday + " days");
        System.out.println("Friday: " + friday + " days");
        System.out.println(" ");
        allTogether = monday + tuesday + wednesday + thursday + friday;
        System.out.println("All together: " + allTogether + " days");
    }
    @Override
    public void start(Stage primaryStage){
        try{
            //Achsen:
            final NumberAxis xAxis = new NumberAxis();
            final CategoryAxis yAxis = new CategoryAxis();

            //Achsenbenennung und anlegen der Barchart
            final BarChart<Number, String> barChart = new BarChart<Number, String>(xAxis,yAxis);
            xAxis.setLabel("Tage");
            yAxis.setLabel("Wochentag");

            //Werte für Graf
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Holiday Dates:");
            series1.getData().add(new XYChart.Data(monday, itemA));
            series1.getData().add(new XYChart.Data(tuesday, itemB));
            series1.getData().add(new XYChart.Data(wednesday, itemC));
            series1.getData().add(new XYChart.Data(thursday, itemD));
            series1.getData().add(new XYChart.Data(friday,itemE));

            Scene scene = new Scene(barChart,640, 480);
            barChart.getData().addAll(series1);

            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e){
            System.out.println("Error!");
        }

    }
}
