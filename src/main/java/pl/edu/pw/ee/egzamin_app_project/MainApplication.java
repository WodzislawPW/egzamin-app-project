package pl.edu.pw.ee.egzamin_app_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        String css = this.getClass().getResource("style.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setTitle("Hello!");

//        Screen screen = Screen.getPrimary();
//        double dpi = screen.getDpi();
//        double scaleFactor = dpi / 96.0;
//
//        stage.setRenderScaleX(scaleFactor);
//        stage.setRenderScaleY(scaleFactor);


        stage.setScene(scene);
        stage.show();
    }
}
