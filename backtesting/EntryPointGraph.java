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
                series1.setName("Strat 2 (200er+3%)");
                series1.setName("Strat 3 (B&H)");

                //add data to graph
                int a = 0;
                int b = 0;
                int c = 0;
                String tablename;

                for(LocalDate d = DBMethods.dbGetFirstDateForGraph(setTablename("S1", i)); d.isBefore(LocalDate.now()); d=d.plusDays(1)) {
                    if(a < DBMethods.dbGetDateForGraph(setTablename("S1",i)).size()){
                        if (d.isEqual(LocalDate.parse(DBMethods.dbGetDateForGraph(setTablename("S1", i)).get(a)))){
                            tablename = setTablename("S1", i);
                            series1.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(a).toString(), DBMethods.dbGetDepotForGraph(tablename).get(a)));
                            a++;
                        }
                    }
                    if(b < DBMethods.dbGetDateForGraph(setTablename("S2", i)).size()){
                        if (d.isEqual(LocalDate.parse(DBMethods.dbGetDateForGraph(setTablename("S2", i)).get(b)))){
                            tablename = setTablename("S2", i);
                            series2.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(b).toString(), DBMethods.dbGetDepotForGraph(tablename).get(b)));
                            b++;
                        }
                    }
                    if(c < DBMethods.dbGetDateForGraph(setTablename("S3", i)).size()){
                        if (d.isEqual(LocalDate.parse(DBMethods.dbGetDateForGraph(setTablename("S3", i)).get(c)))){
                            tablename = setTablename("S3", i);
                            series3.getData().add(new XYChart.Data(DBMethods.dbGetDateForGraph(tablename).get(c).toString(), DBMethods.dbGetDepotForGraph(tablename).get(c)));
                            c++;
                        }
                    }

                }
                System.out.println(series1.getData());
                System.out.println(series2.getData());
                System.out.println(series3.getData());


                lineChart.setCreateSymbols(false);
                Scene scene = new Scene(lineChart,800,600);

                lineChart.getData().clear();
                lineChart.layout();

                lineChart.getData().add(series1);
                lineChart.getData().add(series2);
                lineChart.getData().add(series3);

//                yAxis.setLowerBound(Math.round(Collections.min(DBMethods.dbGetDepotForGraph(tablename)) - (Collections.min(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setUpperBound(Math.round(Collections.max(DBMethods.dbGetDepotForGraph(tablename)) + (Collections.max(DBMethods.dbGetDepotForGraph(tablename)) /10)));
//                yAxis.setAutoRanging(false);

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
