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
                //Achsen
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Verkaufsdatum");
                yAxis.setLabel("Depot");
                LineChart<String , Number> lineChart = new LineChart<>(xAxis, yAxis);

                XYChart.Series series1 = new XYChart.Series();
                XYChart.Series series2 = new XYChart.Series();
                XYChart.Series series3 = new XYChart.Series();
                series1.setName("Strat 1 (200er)");
                series2.setName("Strat 2 (200er+3%)");
                series3.setName("Strat 3 (B&H)");

                //add data to graph
                String tablename;
                tablename = setTablename("S1", i);
                for(int k = 0; k < DBMethods.dbGetDateForGraph(tablename).size(); k++){
                    series1.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(k), DBMethods.dbGetDepotForGraph(tablename).get(k)));
                }
                tablename = setTablename("S2", i);
                for(int k = 0; k < DBMethods.dbGetDateForGraph(tablename).size(); k++){
                    series2.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(k), DBMethods.dbGetDepotForGraph(tablename).get(k)));
                }
                tablename = setTablename("S3", i);
                for(int k = 0; k < DBMethods.dbGetDateForGraph(tablename).size(); k++){
                    series3.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(k), DBMethods.dbGetDepotForGraph(tablename).get(k)));
                }

                lineChart.setCreateSymbols(false);
                Scene scene = new Scene(lineChart,800,600);

                lineChart.getData().clear();
                lineChart.layout();

                lineChart.getData().add(series1);
                lineChart.getData().add(series2);
                lineChart.getData().add(series3);

                WritableImage image = scene.snapshot(null);
                File file = new File("C:\\Users\\david\\Documents\\Schule\\SWP Rubner\\Aktien\\Backtesting\\" + ticker.get(i) + ".jpg");
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
