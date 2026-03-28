package org.example.lyaii;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.Collection;

public class HelloApplication extends Application {
    private Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        crearUI();
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toString());
        stage.setTitle("Compilador");
        stage.setScene(scene);
        stage.show();
    }

    private void crearUI(){
        CodeArea consola=new CodeArea();
        consola.getStyleClass().add("consola");
        //CodeArea consola_info=new CodeArea();
        //consola_info.getStyleClass().add("consola");
        VBox espacio=new VBox();
        espacio.setMinHeight(10);

        VBox vConsolas=new VBox(consola, espacio);
        vConsolas.setMargin(consola, new Insets(20, 20, 0, 50));

        BorderPane bdpPrincipal=new BorderPane();
        bdpPrincipal.setCenter(vConsolas);

        Panel pnlPrincipal=new Panel();
        pnlPrincipal.getStyleClass().add("panel-primary");
        pnlPrincipal.setBody(bdpPrincipal);

        scene = new Scene(pnlPrincipal,600,600);
        //iniciar
    }

    private StyleSpans<Collection<String>> identificarPalabras(String texto){
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();
        //Incompleto
        return creadorSpans.create();
    }

    public static void main(String[] args) {
        launch();
    }
}