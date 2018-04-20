import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Graph extends Application {

    private static ArrayList<ArrayList<Double>> functions = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> functionErrors = new ArrayList<>();
    private static int length = -1;
    private static double target = 0;


    public void setGraph(ArrayList<Double> line1, ArrayList<Double> line2, double t) throws Exception {
        if (line1 == null || line2 == null)
            return;
        functions.add(line1);
        functionErrors.add(line2);
        length = line1.size();
        target = t;
    }

    public static void show() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        for(int j = 0; j < functions.size(); j++) {
            Stage stage1 = new Stage();
            stage1.setTitle("Графическая информация");
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Номер итерации");
            final LineChart<Number, Number> lineChart =
                    new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Изменение ошибки / напряжения");
            XYChart.Series series = new XYChart.Series();
            series.setName("Напряжение");
            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Ошибка");
            XYChart.Series series3 = new XYChart.Series();
            series3.setName("Порог");
            lineChart.setAnimated(false);
            lineChart.setCreateSymbols(false);
            xAxis.setTickUnit(1);
            yAxis.setTickUnit(0.5);
            Scene scene = new Scene(lineChart, 800, 600);
            for (int i = 0; i < functions.get(j).size(); i++) {
                series.getData().add(new XYChart.Data(i, functions.get(j).get(i)));
                series2.getData().add(new XYChart.Data(i, functionErrors.get(j).get(i)));
                series3.getData().add(new XYChart.Data(i, target));
            }
            lineChart.getData().addAll(series, series2, series3);
            stage1.setScene(scene);
            stage1.show();
        }
    }
}
