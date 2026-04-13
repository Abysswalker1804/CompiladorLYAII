package org.example.lyaii;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.example.lyaii.Automatas.AutomataCadena;
import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Automatas.AutomataPalabrasReservadas;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class HelloApplication extends Application {
    private Scene scene;
    private CodeArea cda_consola;
    private boolean error_lexico=false;

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
        cda_consola=new CodeArea();
        cda_consola.setPrefSize(450, 600);
        IniciarEditor();
        //CodeArea consola_info=new CodeArea();
        //consola_info.getStyleClass().add("consola");
        VBox espacio=new VBox();
        espacio.setMinHeight(10);

        VBox vConsolas=new VBox(cda_consola, espacio);
        vConsolas.setMargin(cda_consola, new Insets(20, 20, 0, 50));

        MenuItem mit_abrir=new MenuItem("Abrir");
        mit_abrir.setOnAction(event -> {});//está vacío

        Menu men_archivo=new Menu("Archivo");
        men_archivo.getItems().addAll(mit_abrir);

        MenuBar mbr_principal=new MenuBar();
        mbr_principal.getMenus().addAll(men_archivo);

        BorderPane bdpPrincipal=new BorderPane();
        bdpPrincipal.setCenter(vConsolas);

        Panel pnl_Principal=new Panel();
        pnl_Principal.getStyleClass().add("panel-primary");
        pnl_Principal.setBody(bdpPrincipal);
        pnl_Principal.setHeading(mbr_principal);

        scene = new Scene(pnl_Principal,600,600);
        //iniciar
    }

    private void IniciarEditor() {
        cda_consola.setStyle("-fx-font-size: 14px; -fx-font-family: Consolas;");
        cda_consola.textProperty().addListener((obs, texto, nuevoTexto) -> {
            if (!nuevoTexto.isEmpty() && !nuevoTexto.isBlank()) {
                cda_consola.setStyleSpans(0, identificarPalabras(nuevoTexto));
                //Aquí se revisa el lexico y la sintaxis
            }
        });
    }

    private StyleSpans<Collection<String>> identificarPalabras(String codigo){
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();
        String [] codigoArreglo=Tokenizador.tokenizar(codigo);

        int longitud_acumulada=0;
        error_lexico=false;
        for (String palabra : codigoArreglo) {
            int length = palabra.length();//Para aplicar los estilos en rangos correspondientes
            if(AutomataPalabrasReservadas.analizar(palabra)){
                switch (AutomataPalabrasReservadas.getPalabra()){
                    case PR04:
                    case PR05:
                    case PR06:
                    case PR07:
                    case PR08:
                        creadorSpans.add(Collections.singleton("tiposDatos"),length);
                        break;
                    default:
                        creadorSpans.add(Collections.singleton("palabraReservada"),length);
                }
            } else if(AutomataCadena.analizar(palabra)){
                creadorSpans.add(Collections.singleton("cadena"),length);
            } else if(AutomataNumero.analizar(palabra)){
                creadorSpans.add(Collections.singleton("default"),length);
            } else if(AutomataID.analizar(palabra)){
                creadorSpans.add(Collections.singleton("identificador"),length);
            }else if(!palabra.isEmpty() 
                && (palabra.charAt(0)==' ' 
                    || palabra.charAt(0)=='\n' 
                    || palabra.charAt(0)=='\t'
                    || palabra.charAt(0)=='{'
                    || palabra.charAt(0)=='}'
                    || palabra.charAt(0)=='('
                    || palabra.charAt(0)==')'
                    || palabra.charAt(0)==';'
                    || palabra.charAt(0)=='+'
                    || palabra.charAt(0)=='-'
                    || palabra.charAt(0)=='*'
                    || palabra.charAt(0)=='/'
                    || palabra.charAt(0)=='=')){
                creadorSpans.add(Collections.singleton("default"),length);
            }else {
                creadorSpans.add(Collections.singleton("error"),length);
                error_lexico=true;
            }
        }
        
        //Incompleto

        return creadorSpans.create();
    }

    public static void main(String[] args) {
        launch();
    }
}

class Tokenizador{
    private static String [] tokens;
    /*prueba
    public static void main(String args[]){
        tokenizar(".start\n dclr @var .end");
        for(String token : tokens){
            System.out.println("\""+token+"\"");
        }
    }*/
    public static String[] tokenizar(String texto){
        tokens=new String[1];
        char caracter;
        String espacio="", palabra="";
        for(int i=0; i<texto.length();i++){
            caracter=texto.charAt(i);
            switch (caracter) {
                case ' ':
                case '\n':
                case '\t':
                    espacio = espacio + caracter;
                    if(!palabra.isEmpty()){
                        addToken(palabra);
                        palabra="";
                    }else if (tokens[0]==null){
                        tokens[0]=palabra;
                        palabra="";
                    }
                    break;

                case '{':
                case '}': 
                case '(': 
                case ')':
                case ';':
                case '+':
                case '-': 
                case '*':
                case '/':
                case '=':
                    if(!palabra.isEmpty()){
                        addToken(palabra);
                        palabra="";
                    }
                    palabra=caracter+"";
                    addToken(palabra);
                    palabra="";
                    break;
                default:
                    palabra=palabra+caracter;
                    if(!espacio.isEmpty()){
                        addToken(espacio);
                        espacio="";
                    }else if (tokens[0]==null){
                        tokens[0]=espacio;
                        espacio="";
                    }
            }
            if(i==texto.length()-1){
               if(!espacio.isEmpty()){
                   addToken(espacio);
                   espacio="";
               } else if(!palabra.isEmpty()){
                   addToken(palabra);
                   palabra="";
               }
            }
        }
        String [] aux=new String[tokens.length-1];
        for(int i=1; i< tokens.length; i++){
            aux[i-1]=tokens[i];
        }
        tokens=aux;
        return tokens;
    }
    private static void addToken(String token){
        String [] aux;
        if(tokens==null){
            aux=new String[1];
        }else{
            aux=tokens;
        }
        tokens=new String[aux.length+1];
        for(int i=0; i< aux.length; i++){if(aux[i]!=null){tokens[i]=aux[i];}}
        tokens[aux.length]=token;
    }
}