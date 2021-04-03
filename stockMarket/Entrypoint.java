import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static models.Input.*;
import static models.OtherMethods.*;
import static models.Database.*;

public class Entrypoint extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){
        ArrayList<String> stockNames = new ArrayList<>(stocksGetter("stocks.txt"));
        dbConnect();

        for (int i = 0; i < stockNames.size(); i++) {
            dataGetter(stockNames.get(i));
            dbCreateTable(stockNames.get(i));
            Map<LocalDate, Float> stockValues = dataGetter(stockNames.get(i));
            dbInsert(getLocalDateFromHashMap(stockValues), getCloseFromHashMap(stockValues), avgCalc(getCloseFromHashMap(stockValues)),stockNames.get(i));
            dbAllGetData(stockNames.get(i));
            try {
                primaryStage.setTitle("Aktie (full): " + stockNames.get(i));

                //Achsen
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Datum");
                yAxis.setLabel("Close-Werte");
                LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

                XYChart.Series series1 = new XYChart.Series();
                XYChart.Series series2 = new XYChart.Series();
                series1.setName("Close-Werte");
                series2.setName("Gleitender Durchschnitt");
                for(int j = 0; j < dates.size(); j++){
                    series1.getData().add(new XYChart.Data(dates.get(j), close.get(j)));
                    series2.getData().add(new XYChart.Data(dates.get(j),avg.get(j)));
                }
                lineChart.setCreateSymbols(false);
                Scene scene = new Scene(lineChart,800,600);
                lineChart.getData().addAll(series1,series2);

                yAxis.setLowerBound(Math.round(Collections.min(close) - (Collections.min(close) /10)));
                yAxis.setUpperBound(Math.round(Collections.max(close) + (Collections.max(close) /10)));
                yAxis.setAutoRanging(false);

                for (int k = 0; k < close.size(); k++){
                    if (close.get(k) > avg.get(k)){
                        scene.getStylesheets().add("greenBackground.css");
                    }else {
                        scene.getStylesheets().add("redBackground.css");
                    }
                }
                WritableImage image = scene.snapshot(null);
                File file = new File("D:\\David\\Documents\\Schule\\SWP Rubner\\Aktien\\" + stockNames.get(i) + "\\" +  LocalDate.now().getYear() + "_" + LocalDate.now().getMonth() + "_" + LocalDate.now().getDayOfMonth() + "_full.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);

                primaryStage.setScene(scene);
                primaryStage.close();

                //compact
                primaryStage.setTitle("Aktie (compact): " + stockNames.get(i));
                LineChart<String, Number> lineChart2 = new LineChart<>(xAxis, yAxis);
                XYChart.Series series3 = new XYChart.Series();
                XYChart.Series series4 = new XYChart.Series();
                series3.setName("Close-Werte");
                series4.setName("Gleitender Durchschnitt");
                for(int j = close.size()-1; j > close.size()-201; j--){
                    series3.getData().add(new XYChart.Data(dates.get(j), close.get(j)));
                    series4.getData().add(new XYChart.Data(dates.get(j),avg.get(j)));
                }
                lineChart2.setCreateSymbols(false);
                lineChart2.getData().addAll(series3,series4);

                yAxis.setLowerBound(Math.round(Collections.min(close) - (Collections.min(close) /10)));
                yAxis.setUpperBound(Math.round(Collections.max(close) + (Collections.max(close) /10)));
                yAxis.setAutoRanging(false);
                scene = new Scene(lineChart2,800,600);

                for (int k = 0; k < close.size(); k++){
                    if (close.get(k) > avg.get(k)){
                        scene.getStylesheets().add("greenBackground.css");
                    }else {
                        scene.getStylesheets().add("redBackground.css");
                    }
                }
                image = scene.snapshot(null);
                file = new File("D:\\David\\Documents\\Schule\\SWP Rubner\\Aktien\\" + stockNames.get(i) + "\\" +  LocalDate.now().getYear() + "_" + LocalDate.now().getMonth() + "_" + LocalDate.now().getDayOfMonth() + "_compact.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);

                primaryStage.setScene(scene);
                primaryStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            clearLists(dates,close,avg);

        }
    }
}