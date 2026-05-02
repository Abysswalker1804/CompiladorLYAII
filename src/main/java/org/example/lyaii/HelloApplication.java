package org.example.lyaii;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.example.lyaii.AST.AST;
import org.example.lyaii.Automatas.AutomataCadena;
import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Automatas.AutomataPalabrasReservadas;
import org.example.lyaii.Automatas.AutomataSintax;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;
import org.example.lyaii.Tools.PilaErrores;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HelloApplication extends Application {
    private Scene scene;
    private CodeArea cda_consola;
    private CodeArea cda_error;
    private boolean error_lexico=false;
    private String [] codigoArreglo;
    private MenuItem mit_compilar;
    private String error_tabla="";
    private static boolean error_semantico=false;

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
        cda_error=new CodeArea();
        cda_error.setPrefSize(450,300);
        cda_error.setEditable(false);
        IniciarEditor();
        //CodeArea consola_info=new CodeArea();
        //consola_info.getStyleClass().add("consola");
        VBox espacio=new VBox();
        espacio.setMinHeight(10);

        ToggleButton tgl_luz=new ToggleButton("Modo Oscuro");
        tgl_luz.selectedProperty().addListener((ods,oldVal,newVal) -> {
            if(newVal){
                tgl_luz.setText("Modo Luz");
                scene.getRoot().setStyle("-fx-background-color: rgb(65, 65, 65);");
                cda_consola.getStyleClass().remove("AreaEditable");
                cda_consola.getStyleClass().add("AreaEditableNoche");
            }else{
                tgl_luz.setText("Modo Oscuro");
                scene.getRoot().setStyle("-fx-background-color: #f4f4f4;");
                cda_consola.getStyleClass().remove("AreaEditableNoche");
                cda_consola.getStyleClass().add("AreaEditable");
            }
        });

        VBox vConsolas=new VBox(tgl_luz,cda_consola, espacio, cda_error);
        VBox.setMargin(cda_consola, new Insets(20, 20, 0, 50));
        VBox.setMargin(cda_error, new Insets(20, 20, 20, 50));

        MenuItem mit_abrir=new MenuItem("Abrir");
        mit_abrir.setOnAction(event -> {});//está vacío
        mit_compilar=new MenuItem("Compilar");
        mit_compilar.setDisable(true);
        mit_compilar.setOnAction(event -> {
            String[] codigo_limpio=Tokenizador.limpiar(limpiar(codigoArreglo));
            if(AutomataSintax.analizar(codigo_limpio)){
                cda_error.replaceText("<CORE>: Sintaxis correcta!");
                //Dar ilusión de estar cargando
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        Platform.runLater(() -> {
                            // actualizar UI aquí
                        });
                    } catch (InterruptedException e) {}
                }).start();
                //Comeinza análsis semántico
                AST ast=new AST();
                ast.crearAST(codigo_limpio);
                if(ast.validar(ast.getPrograma())){
                    cda_error.replaceText("<CORE>: Código validado!");
                }else{
                    cda_error.replaceText("");
                    String [] errores=PilaErrores.dump();
                    for(String error: errores){
                        cda_error.appendText("<CORE>: "+error);
                    }
                }
                //popear la pila de errores si hay errores semanticos -> PilaErrores.pop()
            }else{
                String error=AutomataSintax.getError();
                cda_error.replaceText(error==null?"Error Sintáctico!":error);
            }
        });

        Menu men_archivo=new Menu("Archivo");
        men_archivo.getItems().addAll(mit_abrir,mit_compilar);

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
        cda_error.getStyleClass().add("AreaError");
        cda_consola.getStyleClass().add("AreaEditable");
        cda_consola.textProperty().addListener((obs, texto, nuevoTexto) -> {
            if (!nuevoTexto.isEmpty() && !nuevoTexto.isBlank()) {
                cda_consola.setStyleSpans(0, identificarPalabras(nuevoTexto));
                //Aquí se revisa el lexico y la sintaxis
            }
        });
    }

    private StyleSpans<Collection<String>> identificarPalabras(String codigo){
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();
        codigoArreglo=Tokenizador.tokenizar(codigo);
        
        //Analisis Lexico
        error_lexico=false;
        for (String palabra : codigoArreglo) {
            int length = palabra.length();//Para aplicar los estilos en rangos correspondientes
            //System.out.println("palabra: "+palabra+", len: "+length);
            if(AutomataPalabrasReservadas.analizar(palabra)){
                switch (AutomataPalabrasReservadas.getPalabra()){
                    case PR04:
                    case PR05:
                    case PR06:
                    case PR07:
                    case PR08:
                        creadorSpans.add(Collections.singleton("tiposDatos"),length);
                        break;
                    case PR12:
                    case PR13:
                        creadorSpans.add(Collections.singleton("booleans"),length);
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
                    || palabra.charAt(0)=='='
                    || palabra.equals("||")
                    || palabra.equals("&&"))){
                creadorSpans.add(Collections.singleton("default"),length);
            }else if(palabra.charAt(0)=='%'){
                creadorSpans.add(Collections.singleton("comentario"),length);
            }else{
                creadorSpans.add(Collections.singleton("error"),length);
                error_lexico=true;
                PilaErrores.push(palabra+" no se reconoce!");
            }

        }
        if(error_lexico){
            cda_error.replaceText("");
            mit_compilar.setDisable(true);
            String[] errores=PilaErrores.dump();
            for(String error: errores){
                cda_error.appendText("<CORE>: "+error+"\n");
            }
        }else{
            mit_compilar.setDisable(false);
            cda_error.replaceText("<CORE>: Todo en orden.");
        }

        return creadorSpans.create();
    }

    public static void main(String[] args) {
        launch();
    }
    private String [] limpiar(String [] entrada){
        String[] tokens=entrada;
        for(int i=0; i < tokens.length; i++){
            if(tokens[i].charAt(0)=='\n' || tokens[i].charAt(0)=='\t' ||tokens[i].equals(" ")){
                tokens[i]=" ";
            }
        }
        return tokens;
    }
    public static void setErrorSemantico(boolean flag){
        error_semantico=flag;
        //Hay que hacer pila de errores
    }
}


class Tokenizador{
    private static String [] tokens;
    /*
    public static void main(String args[]){
        String [] arr={" ", "begin", " ", " ", " ","=", "end"};
        arr=limpiar(arr);
        String[] arr1=arr;
    }*/
    public static String[] tokenizar(String texto){
        tokens=new String[1];
        char caracter;
        String espacio="", palabra="",comentario="";
        boolean opLog, flag_comentario=false;
        for(int i=0; i<texto.length();i++){
            caracter=texto.charAt(i);
            switch (caracter) {
                case ' ':
                case '\n':
                case '\t':
                    if(flag_comentario){
                        comentario=comentario+caracter;
                        if(i==texto.length()-1){
                            addToken(comentario);
                        }
                    }else{ 
                        espacio = espacio + caracter;
                        if(!palabra.isEmpty()){
                            addToken(palabra);
                            palabra="";
                        }else if (tokens[0]==null){
                            tokens[0]=palabra;
                            palabra="";
                        }
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
                    if(flag_comentario){
                        comentario=comentario+caracter;
                        if(i==texto.length()-1){
                            addToken(comentario);
                        }
                    }else if(!palabra.isEmpty()){
                        addToken(palabra);
                        palabra="";
                    }
                    palabra=caracter+"";
                    addToken(palabra);
                    palabra="";
                    break;
                case '%':
                    if(!palabra.isEmpty()){
                        addToken(palabra);
                        palabra="";
                    }
                    if(!espacio.isEmpty()){
                        addToken(espacio);
                        espacio="";
                    }
                    if(flag_comentario){
                        flag_comentario=false;
                        comentario=comentario+caracter;
                        addToken(comentario);
                        comentario="";
                    }else{
                        flag_comentario=true;
                        comentario=comentario+caracter;
                    }
                    break;
                case '|':
                case '&':
                    if(flag_comentario){
                        comentario=comentario+caracter;
                        if(i==texto.length()-1){
                            addToken(comentario);
                        }
                    }else {
                        if(!palabra.isEmpty() && palabra.charAt(0)!='|' && palabra.charAt(0)!='&' ){
                            addToken(palabra);
                            palabra="";
                        }
                        palabra=palabra+caracter;
                        if(!espacio.isEmpty()){
                            addToken(espacio);
                            espacio="";
                        }else if (tokens[0]==null){
                            tokens[0]=espacio;
                            espacio="";
                        }
                    }
                    break;
                default:
                    if(flag_comentario){
                        comentario=comentario+caracter;
                        if(i==texto.length()-1){
                            addToken(comentario);
                        }
                    }else {
                        if(palabra.contains("|") || palabra.contains("&")){
                            addToken(palabra);
                            palabra="";
                        }
                        palabra=palabra+caracter;
                        if(!espacio.isEmpty()){
                            addToken(espacio);
                            espacio="";
                        }else if (tokens[0]==null){
                            tokens[0]=espacio;
                            espacio="";
                        }
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
        return unirOperadores(tokens);
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
    public static String [] limpiar(String [] tokens){
        int cant=0;
        for(String token: tokens){if(!token.isBlank()){cant++;}}
        String [] nuevo=new String[cant];
        for(int j=0, i=0; i<tokens.length; i++) {
            if (!tokens[i].isBlank()){
                nuevo[j] = tokens[i];
                j++;
            }
        }
        return nuevo;
    }
    public static String[] unirOperadores(String[] tokens) {
        List<String> resultado = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            String actual = tokens[i];

            // Verifica si es uno de los operadores que te interesan
            if ((actual.equals("=") || actual.equals("!") || actual.equals("<") || actual.equals(">"))
                    && i + 1 < tokens.length
                    && tokens[i + 1].equals("=")) {

                // Une con el siguiente "="
                resultado.add(actual + "=");
                i++; // Saltamos el siguiente porque ya lo usamos
            } else {
                resultado.add(actual);
            }
        }

        return resultado.toArray(new String[0]);
    }
}

class CorrectorLexico extends Thread{
    public void checar(){}
}