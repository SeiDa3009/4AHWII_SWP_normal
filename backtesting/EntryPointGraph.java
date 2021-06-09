import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.application.Application;
import javafx.stage.Stage;
import models.DBMethods;
import models.InputData;
import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;


import static models.OtherMethods.*;


public class EntryPointGraph extends Application{
    static String file = "C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\Backtesting\\stocks.txt";
    static ArrayList<String> ticker = new ArrayList<>(InputData.tickerGetter(file));

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        for (int i = 0; i < ticker.size(); i++) {
            try {
                primaryStage.setTitle("Backtesting of " + ticker.get(i));
                String tablename = setTablename("S1", i);
                //Achsen
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Verkaufsdatum");
                yAxis.setLabel("Depot");
                LineChart<String , Number> lineChart = new LineChart<>(xAxis, yAxis);

                XYChart.Series series1 = new XYChart.Series();
                series1.setName("Strat 1 (200er)");

                for(int j = 0; j < DBMethods.dbGetDateForGraph(tablename).size(); j++) {
                    series1.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(j).toString(), DBMethods.dbGetDepotForGraph(tablename).get(j)));
                }
                lineChart.setCreateSymbols(false);
                Scene scene = new Scene(lineChart,800,600);
                lineChart.getData().addAll(series1);


//                yAxis.setLowerBound(Math.round(Collections.min(DBMethods.dbGetDepotForGraph(tablename)) - (Collections.min(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setUpperBound(Math.round(Collections.max(DBMethods.dbGetDepotForGraph(tablename)) + (Collections.max(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setAutoRanging(false);

                WritableImage image = scene.snapshot(null);
                File file = new File("C:\\Users\\david\\Documents\\Schule\\SWP Rubner\\Aktien\\Backtesting\\" + ticker.get(i) + "Strat1.jpg");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
                primaryStage.setScene(scene);
                primaryStage.close();

                //Graph 2. Strat
                primaryStage.setTitle("Backtesting of " + ticker.get(i));
                tablename = setTablename("S2", i);
                //Achsen
                LineChart<String , Number> lineChart2 = new LineChart<>(xAxis, yAxis);

                XYChart.Series series2 = new XYChart.Series();
                series2.setName("Strat 2 (200er+3%)");

                for(int j = 0; j < DBMethods.dbGetDateForGraph(tablename).size(); j++) {
                    series2.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(j).toString(), DBMethods.dbGetDepotForGraph(tablename).get(j)));
                }
                lineChart2.setCreateSymbols(false);
                scene = new Scene(lineChart2,800,600);
                lineChart2.getData().addAll(series2);


//                yAxis.setLowerBound(Math.round(Collections.min(DBMethods.dbGetDepotForGraph(tablename)) - (Collections.min(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setUpperBound(Math.round(Collections.max(DBMethods.dbGetDepotForGraph(tablename)) + (Collections.max(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setAutoRanging(false);

                image = scene.snapshot(null);
                file = new File("C:\\Users\\david\\Documents\\Schule\\SWP Rubner\\Aktien\\Backtesting\\" + ticker.get(i) + "Strat2.jpg");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
                primaryStage.setScene(scene);
                primaryStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public String setTablename(String strat, int i){
        return ticker.get(i) + strat.toUpperCase();
}
}
