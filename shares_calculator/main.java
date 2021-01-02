import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.methods;

import java.sql.*;
import java.util.ArrayList;
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

        float share = 0;
        float avg = 0;
        sql = "SELECT * FROM " + methods.share + " ORDER BY DATE ASC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                share = rs.getFloat("shares");
                avg = rs.getFloat("avg");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if (share > avg) {
            series1.getNode().setStyle("-fx-stroke: #006400; ");
        } else {
            series2.getNode().setStyle("-fx-stroke: #FB2C00; ");
        }
        series2.getNode().setStyle("-fx-stroke: black; ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
