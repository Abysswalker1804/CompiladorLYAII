package org.example.lyaii.Graficos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ASTGrafico extends Application{
    
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage stage){
        Scene scene=nodeViewer();
        stage.setTitle("Árbol");
        stage.setMinWidth(860);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }
    private static Scene nodeViewer(){
        Pane root = new Pane();
        // Líneas
        Line l1 = new Line(300, 100, 200, 200);
        Line l2 = new Line(300, 100, 400, 200);

        // Nodos
        Circle a = new Circle(300, 100, 25, Color.LIGHTBLUE);
        Circle b = new Circle(200, 200, 25, Color.LIGHTGREEN);
        Circle c = new Circle(400, 200, 25, Color.LIGHTGREEN);

        // Texto
        Text ta = new Text(295, 105, "Programa");
        Text tb = new Text(195, 205, "Inicio");
        Text tc = new Text(395, 205, "Fin");

        root.getChildren().addAll(
                l1, l2,
                a, b, c,
                ta, tb, tc
        );
        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
    private static Scene graph(){
        ;
        return null;
    }
}
