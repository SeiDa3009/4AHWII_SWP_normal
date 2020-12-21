import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.methods;

import java.util.ArrayList;
import java.util.List;


public class main extends Application {
    public static List<Float> shares = new ArrayList<>();
    public static List<String> date = new ArrayList<>();
    public static Float avg;

    public static void main(String[] args) {
        methods m1 = new methods();
        m1.db();
        for(int i = m1.getShares().size()-1; i >= 0; i--){
            shares.add(m1.getShares().get(i));
            date.add(m1.getDates().get(i).toString());
        }
        avg = m1.getAVG();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Aktie " + methods.share);

        //Achsen erstellen
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Datum");
        yAxis.setLabel("Close-Werte");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Close-Werte");
        series2.setName("Gleitender Durchschnitt");

        for (int i = 0; i < date.size(); i++){
            series1.getData().add(new XYChart.Data(date.get(i), shares.get(i)));
            series2.getData().add(new XYChart.Data(date.get(i), avg));
        }
        lineChart.setCreateSymbols(false);
        Scene scene = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series1, series2);

        /*
        Überlegungen zum Einfärben:

        float lastShare = 0;
        for (int i = 0; i < shares.size(); i++) {
            lastShare = shares.get(i);

            if (lastShare > avg) {
                series1.getNode().setStyle("-fx-stroke: #006400; ");
            } else {
                series2.getNode().setStyle("-fx-stroke: #FB2C00; ");
            }

            series2.getNode().setStyle("-fx-stroke: black; ");
        */
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
