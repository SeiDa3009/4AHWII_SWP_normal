import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.methods;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class main extends Application {
    public static void main(String[] args) {
        methods m1 = new methods();
        m1.db();
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
        Connection conn = DriverManager.getConnection(methods.filename);
        String sql = "SELECT * FROM " + methods.share + " ORDER BY DATE ASC";
        try {
            Statement stmt =  conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                series1.getData().add(new XYChart.Data(rs.getDate("date").toString(), rs.getFloat("shares")));
                series2.getData().add(new XYChart.Data(rs.getDate("date").toString(), rs.getFloat("avg")));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        lineChart.setCreateSymbols(false);
        Scene scene = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series1, series2);

        sql = "SELECT MIN(SHARES), MAX(SHARES) FROM " + methods.share;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                yAxis.setLowerBound(Math.round(rs.getFloat("MIN(shares)") - (rs.getFloat("MIN(SHARES)") /10 )));
                yAxis.setUpperBound(Math.round(rs.getFloat("MAX(shares)") + (rs.getFloat("MAX(SHARES)") /10 )));

            }
        }catch (SQLException e){
            System.out.println("Range: " + e.getMessage());
        }
        yAxis.setAutoRanging(false);

        sql = "SELECT * FROM " + methods.share + " ORDER BY DATE DESC LIMIT 1";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                if (rs.getFloat("shares") > rs.getFloat("avg")) {
                    scene.getStylesheets().add("greenBackground.css");
                } else {
                    scene.getStylesheets().add("redBackground.css");
                }
            }
        }        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
