/**
 */

package zad1;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final String APP_TITLE = "TPO Wikipedia App";
    private static final String MAIN_VIEW_PATH = "resources/mainGuiView.fxml";

    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 400;

    public static void main(String[] args) {

        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();

        Application.launch(Main.class, args);
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_VIEW_PATH));
        Parent root = FXMLLoader.load(getClass().getResource(MAIN_VIEW_PATH));
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
