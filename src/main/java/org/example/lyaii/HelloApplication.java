package org.example.lyaii;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.example.lyaii.AST.AST;
import org.example.lyaii.Automatas.AutomataCadena;
import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Automatas.AutomataPalabrasReservadas;
import org.example.lyaii.Automatas.AutomataSintax;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;
import org.example.lyaii.Tools.PilaErrores;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

public class HelloApplication extends Application {

    // ── Estado ───────────────────────────────────────────────────────────────
    private Scene       scene;
    private CodeArea    cda_consola;
    private VBox        vbox_consola;
    private ScrollPane  scroll_consola;
    private boolean     error_lexico = false;
    private String[]    codigoArreglo;
    private MenuItem    mit_compilar;
    private static boolean error_semantico = false;

    // ── Modo actual ──────────────────────────────────────────────────────────
    private boolean modoOscuro = true;

    // ── Widgets de estado ────────────────────────────────────────────────────
    private Label lbl_statusDot;
    private Label lbl_statusText;
    private Label lbl_lineCount;
    private Label lbl_tokenCount;

    // ── Nodos que se reestilan al cambiar modo ───────────────────────────────
    private VBox   root;
    private HBox   header;
    private HBox   cuerpo;       // HBox: [editorConConsola | tablaSimbolos]
    private HBox   statusBar;
    private HBox   barraEditor;
    private HBox   barraConsola;
    private VBox   panelEditor;
    private VBox   panelConsola;
    private Label  lbl_editorTitle;
    private Label  lbl_consTitle;
    private Label  lbl_lang;
    private Label  lbl_version;
    private Button btn_modo;
    private Button btn_compilar_header;
    private Label  lblLogo;
    private Label  lblSub;

    // ── Tabla de símbolos ─────────────────────────────────────────────────────
    private VBox       panelTabla;          // panel derecho completo
    private VBox       vbox_filas;          // contenedor de filas de la tabla
    private ScrollPane scroll_tabla;
    private Label      lbl_tablaTitle;
    private Label      lbl_tablaBadge;
    private HBox       barraTabla;

    // ── Popup del menú archivo ───────────────────────────────────────────────
    private Popup   archivoPopup;
    private VBox    archivoMenu;
    private boolean popupVisible = false;

    // ══════════════════════════════════════════════════════════════════════
    //  PALETAS
    // ══════════════════════════════════════════════════════════════════════
    private static final String D_BG         = "#0E1017";
    private static final String D_SURFACE     = "#161B27";
    private static final String D_SURFACE2    = "#1E2336";
    private static final String D_BORDER      = "#2A304A";
    private static final String D_ACCENT      = "#5B8DEF";
    private static final String D_ACCENT_DARK = "#4A7ADE";
    private static final String D_SUCCESS     = "#3DDC84";
    private static final String D_DANGER      = "#FF6B6B";
    private static final String D_TEXT        = "#D0D8EC";
    private static final String D_TEXT_MUTED  = "#5E6E8C";
    private static final String D_TEXT_DIM    = "#2A3450";
    private static final String D_MENU_HOVER  = "#232B42";
    private static final String D_ROW_ALT     = "#1A2038";
    private static final String D_ROW_HOVER   = "#232B44";

    private static final String L_BG          = "#F1F3F8";
    private static final String L_SURFACE      = "#FFFFFF";
    private static final String L_SURFACE2     = "#E8EBF4";
    private static final String L_BORDER       = "#C8CEDF";
    private static final String L_ACCENT       = "#3A6FD8";
    private static final String L_ACCENT_DARK  = "#2A5CC4";
    private static final String L_SUCCESS      = "#1A9E5C";
    private static final String L_DANGER       = "#D63B3B";
    private static final String L_TEXT         = "#1A2035";
    private static final String L_TEXT_MUTED   = "#6B7490";
    private static final String L_TEXT_DIM     = "#B0B8CF";
    private static final String L_MENU_HOVER   = "#EEF1FA";
    private static final String L_ROW_ALT      = "#F5F7FC";
    private static final String L_ROW_HOVER    = "#EBF0FF";

    // Consola siempre clara
    private static final String CON_BG     = "#FFFFFF";
    private static final String CON_BORDER = "#D1D5DB";
    private static final String CON_ERROR  = "#CC0000";
    private static final String CON_OK     = "#1A6B35";
    private static final String CON_INFO   = "#374151";
    private static final String CON_MUTED  = "#6B7280";

    private String bg()         { return modoOscuro ? D_BG         : L_BG;         }
    private String surface()    { return modoOscuro ? D_SURFACE     : L_SURFACE;    }
    private String surface2()   { return modoOscuro ? D_SURFACE2    : L_SURFACE2;   }
    private String border()     { return modoOscuro ? D_BORDER      : L_BORDER;     }
    private String accent()     { return modoOscuro ? D_ACCENT      : L_ACCENT;     }
    private String accentDark() { return modoOscuro ? D_ACCENT_DARK : L_ACCENT_DARK;}
    private String success()    { return modoOscuro ? D_SUCCESS     : L_SUCCESS;    }
    private String danger()     { return modoOscuro ? D_DANGER      : L_DANGER;     }
    private String text()       { return modoOscuro ? D_TEXT        : L_TEXT;       }
    private String textMuted()  { return modoOscuro ? D_TEXT_MUTED  : L_TEXT_MUTED; }
    private String textDim()    { return modoOscuro ? D_TEXT_DIM    : L_TEXT_DIM;   }
    private String menuHover()  { return modoOscuro ? D_MENU_HOVER  : L_MENU_HOVER; }
    private String rowAlt()     { return modoOscuro ? D_ROW_ALT     : L_ROW_ALT;    }
    private String rowHover()   { return modoOscuro ? D_ROW_HOVER   : L_ROW_HOVER;  }

    // ════════════════════════════════════════════════════════════════════════
    @Override
    public void start(Stage stage) throws IOException {
        crearUI();
        scene.getStylesheets().add(
                getClass().getResource("/css/main.css").toString());
        stage.setTitle("HML86 Compiler");
        stage.setMinWidth(900);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN PRINCIPAL
    // ════════════════════════════════════════════════════════════════════════
    private void crearUI() {
        cda_consola = new CodeArea();
        IniciarEditor();

        header    = crearHeader();
        cuerpo    = crearCuerpo();
        statusBar = crearStatusBar();

        VBox.setVgrow(cuerpo, Priority.ALWAYS);

        root = new VBox(header, cuerpo, statusBar);
        root.setStyle("-fx-background-color: " + bg() + ";");

        scene = new Scene(root, 1280, 620);

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (popupVisible && archivoPopup != null) {
                if (!esHijoDePopup((Node) e.getTarget())) cerrarMenuArchivo();
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HEADER
    // ════════════════════════════════════════════════════════════════════════
    private HBox crearHeader() {
        lblLogo = new Label("◈  HML86");
        lblLogo.setStyle(estiloLogo());

        lblSub = new Label("compiler");
        lblSub.setStyle(estiloLogoSub());

        HBox logo = new HBox(4, lblLogo, lblSub);
        logo.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn_archivo = crearBotonArchivo();

        btn_compilar_header = crearBtnCompilar();
        btn_compilar_header.setDisable(true);
        btn_compilar_header.setOnAction(e -> ejecutarCompilacion());

        mit_compilar = new MenuItem("Compilar");
        mit_compilar.setDisable(true);
        mit_compilar.setOnAction(e -> ejecutarCompilacion());
        mit_compilar.disableProperty().addListener(
                (obs, o, n) -> btn_compilar_header.setDisable(n));

        btn_modo = crearBtnModo();
        btn_modo.setOnAction(e -> toggleModo());

        HBox h = new HBox(12, logo, spacer, btn_archivo, btn_compilar_header, btn_modo);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(10, 16, 10, 16));
        h.setMinHeight(50);
        h.setMaxHeight(50);
        h.setStyle(estiloHeader());
        return h;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CUERPO PRINCIPAL
    //  HBox: [columna izquierda (editor+consola)] + [tabla de símbolos]
    // ════════════════════════════════════════════════════════════════════════
    private HBox crearCuerpo() {

        // ── Columna izquierda: editor arriba, consola abajo ───────────────
        VBox columnaIzq = crearColumnaEditor();
        HBox.setHgrow(columnaIzq, Priority.ALWAYS);

        // ── Divisor vertical ──────────────────────────────────────────────
        Separator divV = new Separator(Orientation.VERTICAL);
        divV.setStyle("-fx-background-color: " + border() + "; " +
                "-fx-pref-width: 1; -fx-min-width: 1; -fx-max-width: 1;");

        // ── Panel tabla de símbolos (derecha) ─────────────────────────────
        panelTabla = crearPanelTabla();
        panelTabla.setVisible(false);   // oculto hasta compilar
        panelTabla.setManaged(false);   // no ocupa espacio hasta ser visible

        HBox c = new HBox(columnaIzq, divV, panelTabla);
        VBox.setVgrow(columnaIzq, Priority.ALWAYS);
        return c;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  COLUMNA IZQUIERDA  (editor + consola apilados)
    // ════════════════════════════════════════════════════════════════════════
    private VBox crearColumnaEditor() {

        // ── Barra del editor ──────────────────────────────────────────────
        lbl_editorTitle = new Label("EDITOR");
        lbl_editorTitle.setStyle(estiloEtiquetaPanel());

        lbl_lang = new Label("HML86-lang");
        lbl_lang.setStyle(estiloLangBadge());

        Region sp1 = new Region();
        HBox.setHgrow(sp1, Priority.ALWAYS);

        barraEditor = new HBox(8, lbl_editorTitle, sp1, lbl_lang);
        barraEditor.setAlignment(Pos.CENTER_LEFT);
        barraEditor.setPadding(new Insets(6, 12, 6, 12));
        barraEditor.setMinHeight(32);
        barraEditor.setMaxHeight(32);
        barraEditor.setStyle(barraInternaCss());

        cda_consola.getStyleClass().add(modoOscuro ? "AreaEditableNoche" : "AreaEditable");
        configurarNumerosLinea();
        VBox.setVgrow(cda_consola, Priority.ALWAYS);

        panelEditor = new VBox(barraEditor, cda_consola);
        panelEditor.setStyle("-fx-background-color: " + surface() + ";");
        VBox.setVgrow(panelEditor, Priority.ALWAYS);
        VBox.setVgrow(cda_consola, Priority.ALWAYS);

        // ── Separador horizontal ──────────────────────────────────────────
        Separator sepH = new Separator(Orientation.HORIZONTAL);
        sepH.setStyle("-fx-background-color: " + border() + "; " +
                "-fx-pref-height: 1; -fx-min-height: 1; -fx-max-height: 1;");

        // ── Panel consola ─────────────────────────────────────────────────
        lbl_consTitle = new Label("CONSOLA");
        lbl_consTitle.setStyle(estiloEtiquetaPanel());

        lbl_statusDot = new Label("●");
        lbl_statusDot.setStyle("-fx-text-fill: " + success() + "; -fx-font-size: 9px;");

        lbl_statusText = new Label("Esperando entrada…");
        lbl_statusText.setStyle(estiloStatusText());

        Region sp2 = new Region();
        HBox.setHgrow(sp2, Priority.ALWAYS);

        Button btn_limpiar = new Button("Limpiar");
        btn_limpiar.setStyle(estiloBtnLimpiar());
        btn_limpiar.setOnAction(e -> limpiarConsola());

        barraConsola = new HBox(6, lbl_consTitle, lbl_statusDot, lbl_statusText, sp2, btn_limpiar);
        barraConsola.setAlignment(Pos.CENTER_LEFT);
        barraConsola.setPadding(new Insets(6, 12, 6, 12));
        barraConsola.setMinHeight(32);
        barraConsola.setMaxHeight(32);
        barraConsola.setStyle(barraConsolaFijaCss());

        vbox_consola = new VBox(2);
        vbox_consola.setPadding(new Insets(8, 12, 8, 12));
        vbox_consola.setStyle("-fx-background-color: " + CON_BG + ";");

        scroll_consola = new ScrollPane(vbox_consola);
        scroll_consola.setFitToWidth(true);
        scroll_consola.setStyle(
                "-fx-background: " + CON_BG + "; " +
                        "-fx-background-color: " + CON_BG + "; " +
                        "-fx-border-color: transparent;");
        scroll_consola.setMinHeight(140);
        scroll_consola.setPrefHeight(170);
        scroll_consola.setMaxHeight(210);

        panelConsola = new VBox(barraConsola, scroll_consola);
        panelConsola.setStyle("-fx-background-color: " + CON_BG + "; " +
                "-fx-border-color: " + CON_BORDER + "; " +
                "-fx-border-width: 1 0 0 0;");
        VBox.setVgrow(panelConsola, Priority.NEVER);

        VBox col = new VBox(panelEditor, sepH, panelConsola);
        VBox.setVgrow(panelEditor, Priority.ALWAYS);
        return col;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PANEL TABLA DE SÍMBOLOS
    // ════════════════════════════════════════════════════════════════════════
    private VBox crearPanelTabla() {

        // ── Barra superior ────────────────────────────────────────────────
        lbl_tablaTitle = new Label("TABLA DE SÍMBOLOS");
        lbl_tablaTitle.setStyle(estiloEtiquetaPanel());

        lbl_tablaBadge = new Label("0 entradas");
        lbl_tablaBadge.setStyle(estiloBadgeTabla());

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        barraTabla = new HBox(8, lbl_tablaTitle, sp, lbl_tablaBadge);
        barraTabla.setAlignment(Pos.CENTER_LEFT);
        barraTabla.setPadding(new Insets(6, 12, 6, 12));
        barraTabla.setMinHeight(32);
        barraTabla.setMaxHeight(32);
        barraTabla.setStyle(barraInternaCss());

        // ── Cabecera de columnas ──────────────────────────────────────────
        HBox cabecera = crearCabeceraTabla();

        // ── Filas de datos ────────────────────────────────────────────────
        vbox_filas = new VBox(0);
        vbox_filas.setStyle("-fx-background-color: " + surface() + ";");

        scroll_tabla = new ScrollPane(vbox_filas);
        scroll_tabla.setFitToWidth(true);
        scroll_tabla.setStyle(
                "-fx-background: " + surface() + "; " +
                        "-fx-background-color: " + surface() + "; " +
                        "-fx-border-color: transparent;");
        VBox.setVgrow(scroll_tabla, Priority.ALWAYS);

        VBox panel = new VBox(barraTabla, cabecera, scroll_tabla);
        panel.setPrefWidth(300);
        panel.setMinWidth(240);
        panel.setMaxWidth(360);
        panel.setStyle("-fx-background-color: " + surface() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 0 0 0 1;");
        VBox.setVgrow(scroll_tabla, Priority.ALWAYS);
        return panel;
    }

    private HBox crearCabeceraTabla() {
        Label colId    = lblCol("Identificador", true);
        Label colTipo  = lblCol("Tipo",          false);
        Label colValor = lblCol("Valor",         false);

        HBox.setHgrow(colId,    Priority.ALWAYS);
        HBox.setHgrow(colTipo,  Priority.NEVER);
        HBox.setHgrow(colValor, Priority.NEVER);

        colTipo.setPrefWidth(72);
        colValor.setPrefWidth(80);

        HBox cab = new HBox(0, colId, colTipo, colValor);
        cab.setStyle("-fx-background-color: " + surface2() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 0 0 1 0;");
        cab.setMinHeight(28);
        cab.setMaxHeight(28);
        return cab;
    }

    private Label lblCol(String texto, boolean primero) {
        Label l = new Label(texto);
        l.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                        "-fx-font-weight: bold; -fx-text-fill: " + textMuted() + "; " +
                        "-fx-padding: 0 8 0 " + (primero ? "12" : "8") + "; " +
                        "-fx-alignment: center-left;");
        l.setMaxHeight(Double.MAX_VALUE);
        return l;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  POBLAR TABLA DE SÍMBOLOS
    // ════════════════════════════════════════════════════════════════════════
    private void mostrarTablaSimbolos() {
        Platform.runLater(() -> {
            Simbolo[] simbolos = TablaSimbolos.getAll();
            vbox_filas.getChildren().clear();

            if (simbolos == null || simbolos.length == 0) {
                Label vacio = new Label("Sin símbolos declarados.");
                vacio.setStyle(
                        "-fx-font-family: 'Consolas'; -fx-font-size: 11px; " +
                                "-fx-text-fill: " + textMuted() + "; -fx-padding: 12;");
                vbox_filas.getChildren().add(vacio);
                lbl_tablaBadge.setText("0 entradas");
            } else {
                for (int i = 0; i < simbolos.length; i++) {
                    vbox_filas.getChildren().add(crearFilaSimbolo(simbolos[i], i));
                }
                lbl_tablaBadge.setText(simbolos.length + " entrada" +
                        (simbolos.length == 1 ? "" : "s"));
            }

            // Mostrar panel con animación
            if (!panelTabla.isVisible()) {
                panelTabla.setVisible(true);
                panelTabla.setManaged(true);
                FadeTransition ft = new FadeTransition(Duration.millis(220), panelTabla);
                ft.setFromValue(0); ft.setToValue(1);
                ft.play();
            }

            lbl_tablaBadge.setStyle(estiloBadgeTabla());
        });
    }

    private HBox crearFilaSimbolo(Simbolo sim, int indice) {
        String bgBase  = (indice % 2 == 0) ? surface() : rowAlt();
        String valorStr = obtenerValorSimbolo(sim);
        String tipoStr  = sim.getTipo() != null ? sim.getTipo().toString() : "—";
        String nombreStr = sim.getNombre() != null
                ? sim.getNombre().replace("$", "") : "—";

        // Celda identificador
        Label cId = new Label(nombreStr);
        cId.setStyle(estiloFilaCelda(true, accent()));
        HBox.setHgrow(cId, Priority.ALWAYS);
        cId.setMaxWidth(Double.MAX_VALUE);

        // Celda tipo (badge coloreado)
        Label cTipo = new Label(tipoStr);
        cTipo.setStyle(estiloFilaTipoBadge(tipoStr));
        cTipo.setPrefWidth(72);

        // Celda valor
        Label cValor = new Label(valorStr);
        cValor.setStyle(estiloFilaCelda(false, text()));
        cValor.setPrefWidth(80);

        HBox fila = new HBox(0, cId, cTipo, cValor);
        fila.setMinHeight(30);
        fila.setMaxHeight(30);
        fila.setStyle("-fx-background-color: " + bgBase + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 0 0 1 0;");
        fila.setAlignment(Pos.CENTER_LEFT);

        fila.setOnMouseEntered(e -> fila.setStyle(
                "-fx-background-color: " + rowHover() + "; " +
                        "-fx-border-color: " + border() + "; -fx-border-width: 0 0 1 0;"));
        fila.setOnMouseExited(e -> fila.setStyle(
                "-fx-background-color: " + bgBase + "; " +
                        "-fx-border-color: " + border() + "; -fx-border-width: 0 0 1 0;"));

        return fila;
    }

    private String obtenerValorSimbolo(Simbolo sim) {
        if (sim == null || sim.getTipo() == null) return "—";
        try {
            switch (sim.getTipo()) {
                case STRING: {
                    String v = sim.getValorStr();
                    return v != null ? v : "—";
                }
                case INT:
                case UINT: {
                    double n = sim.getValorInt();
                    return String.valueOf((int) n);
                }
                case FIXED:
                case UFIXED: {
                    double n = sim.getValorFix();
                    return String.valueOf(n);
                }
                default: return "—";
            }
        } catch (Exception e) {
            return "—";
        }
    }

    /** Color del badge de tipo según el tipo de dato. */
    private String estiloFilaTipoBadge(String tipo) {
        String color;
        switch (tipo.toUpperCase()) {
            case "INT":    color = "#5B8DEF"; break;
            case "UINT":   color = "#3A9BD5"; break;
            case "FIXED":  color = "#C79B56"; break;
            case "UFIXED": color = "#D4A54A"; break;
            case "STRING": color = "#3DDC84"; break;
            default:       color = textMuted(); break;
        }
        return "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + color + "; " +
                "-fx-padding: 0 8 0 8; -fx-alignment: center-left;";
    }

    private String estiloFilaCelda(boolean primero, String colorTexto) {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 11px; " +
                "-fx-text-fill: " + colorTexto + "; " +
                "-fx-padding: 0 8 0 " + (primero ? "12" : "8") + "; " +
                "-fx-alignment: center-left;";
    }

    private String estiloBadgeTabla() {
        return "-fx-background-color: " + accent() + "22; " +
                "-fx-text-fill: " + accent() + "; " +
                "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-padding: 2 8 2 8; -fx-background-radius: 20;";
    }

    private void ocultarTablaSimbolos() {
        panelTabla.setVisible(false);
        panelTabla.setManaged(false);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STATUS BAR
    // ════════════════════════════════════════════════════════════════════════
    private HBox crearStatusBar() {
        lbl_lineCount  = new Label("Líneas: 0");
        lbl_tokenCount = new Label("Tokens: 0");
        lbl_version    = new Label("v0.1-alpha");

        lbl_lineCount .setStyle(estiloStatusItem());
        lbl_tokenCount.setStyle(estiloStatusItem());
        lbl_version   .setStyle(estiloStatusItem());

        Separator sep1 = new Separator(Orientation.VERTICAL);
        Separator sep2 = new Separator(Orientation.VERTICAL);
        Region spacer  = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(14, lbl_lineCount, sep1, lbl_tokenCount, spacer, sep2, lbl_version);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(4, 16, 4, 16));
        bar.setMinHeight(24);
        bar.setMaxHeight(24);
        bar.setStyle(estiloStatusBar());
        return bar;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  NÚMEROS DE LÍNEA
    // ════════════════════════════════════════════════════════════════════════
    private void configurarNumerosLinea() {
        IntFunction<Node> base = LineNumberFactory.get(cda_consola);
        cda_consola.setParagraphGraphicFactory(linea -> {
            Node node = base.apply(linea);
            if (node instanceof Label lbl) {
                lbl.setStyle(
                        "-fx-font-family: 'Consolas'; -fx-font-size: 13px; " +
                                "-fx-text-fill: " + (modoOscuro ? "#3E4A66" : "#94A3B8") + "; " +
                                "-fx-alignment: center-right; -fx-padding: 0 8 0 8; " +
                                "-fx-min-width: 40px; " +
                                "-fx-background-color: " + (modoOscuro ? D_SURFACE2 : L_SURFACE2) + ";");
            }
            return node;
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSOLA — escritura con colores
    // ════════════════════════════════════════════════════════════════════════
    private void limpiarConsola() {
        Platform.runLater(() -> vbox_consola.getChildren().clear());
    }

    private void agregarLineaConsola(String prefijo, String mensaje, String color) {
        Platform.runLater(() -> {
            Text txtPrefijo = new Text(prefijo + " ");
            txtPrefijo.setStyle(
                    "-fx-font-family: 'Consolas'; -fx-font-size: 13px; " +
                            "-fx-font-weight: bold; -fx-fill: " + CON_MUTED + ";");

            Text txtMsg = new Text(mensaje + "\n");
            txtMsg.setStyle(
                    "-fx-font-family: 'Consolas'; -fx-font-size: 13px; " +
                            "-fx-fill: " + color + ";" +
                            (color.equals(CON_ERROR) ? "-fx-font-weight: bold;" : ""));

            TextFlow tf = new TextFlow(txtPrefijo, txtMsg);
            tf.setStyle("-fx-background-color: transparent;");
            vbox_consola.getChildren().add(tf);
            scroll_consola.layout();
            scroll_consola.setVvalue(1.0);
        });
    }

    private void consolaOk(String m)    { agregarLineaConsola("<CORE>:", m, CON_OK);    }
    private void consolaError(String m) { agregarLineaConsola("<CORE>:", m, CON_ERROR); }
    private void consolaInfo(String m)  { agregarLineaConsola("",        m, CON_MUTED); }

    private int obtenerNumeroLinea(String token, String codigo) {
        String[] lineas = codigo.split("\n", -1);
        for (int i = 0; i < lineas.length; i++)
            if (lineas[i].contains(token)) return i + 1;
        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BOTÓN ARCHIVO CON MENÚ POPUP ANIMADO
    // ════════════════════════════════════════════════════════════════════════
    private Button crearBotonArchivo() {
        SVGPath ico = new SVGPath();
        ico.setContent(
                "M1 3.5A1.5 1.5 0 012.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 " +
                        "3.648 8.378 4 9 4h4.5A1.5 1.5 0 0115 5.5v1H1v-3zM0 8a1 1 0 011-1h14" +
                        "a1 1 0 011 1v5.5A1.5 1.5 0 0114.5 15h-13A1.5 1.5 0 010 13.5V8z");
        ico.setFill(Color.web(text()));
        ico.setStyle("-fx-scale-x:0.85; -fx-scale-y:0.85;");

        Label flecha   = new Label("▾");
        flecha.setStyle("-fx-font-size:10px; -fx-text-fill:" + textMuted() + ";");
        Label textoBtn = new Label("Archivo");
        textoBtn.setStyle("-fx-font-family:'Consolas'; -fx-font-size:12px; -fx-text-fill:" + text() + ";");

        HBox contenido = new HBox(7, ico, textoBtn, flecha);
        contenido.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setGraphic(contenido);
        btn.setStyle(estiloBotonArchivo(false));
        btn.setOnMouseEntered(e -> btn.setStyle(estiloBotonArchivo(true)));
        btn.setOnMouseExited(e  -> { if (!popupVisible) btn.setStyle(estiloBotonArchivo(false)); });
        btn.setOnAction(e -> {
            if (popupVisible) { cerrarMenuArchivo(); btn.setStyle(estiloBotonArchivo(false)); }
            else              { abrirMenuArchivo(btn); btn.setStyle(estiloBotonArchivo(true)); }
        });
        construirPopupArchivo(btn, flecha, ico);
        return btn;
    }

    private void construirPopupArchivo(Button btnRef, Label flecha, SVGPath ico) {
        HBox i1 = crearItemMenu("📄", "Nuevo archivo", "Ctrl+N", e -> {});
        HBox i2 = crearItemMenu("📂", "Abrir archivo", "Ctrl+O", e -> {
            try {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Archivos HML86","*.hml86"),
                        new FileChooser.ExtensionFilter("Archivos TXT","*.txt"));
                File f = fc.showOpenDialog(new Stage());
                if (f != null) cda_consola.replaceText(Files.readString(f.toPath()));
                cerrarMenuArchivo();
            } catch (IOException ex) { mostrarAlertaError(); }
        });

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color:" + border() + ";");
        VBox.setMargin(sep, new Insets(4,0,4,0));

        HBox i3 = crearItemMenu("💾", "Guardar", "Ctrl+S", e -> {
            try {
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter fH = new FileChooser.ExtensionFilter("Archivos HML86","*.hml86");
                FileChooser.ExtensionFilter fT = new FileChooser.ExtensionFilter("Archivos de texto","*.txt","*.md");
                fc.getExtensionFilters().addAll(fH, fT);
                File f = fc.showSaveDialog(new Stage());
                if (f != null) {
                    String ext = fc.getSelectedExtensionFilter() == fH ? ".hml86" : ".txt";
                    if (!f.getName().endsWith(ext)) f = new File(f.getAbsolutePath() + ext);
                    Files.writeString(f.toPath(), cda_consola.getText());
                    cerrarMenuArchivo();
                }
            } catch (IOException ex) { mostrarAlertaError(); }
        });

        HBox i4 = crearItemMenu("▶", "Compilar", "F5", e -> { cerrarMenuArchivo(); ejecutarCompilacion(); });

        archivoMenu = new VBox(4, i1, i2, sep, i3, i4);
        archivoMenu.setPadding(new Insets(8));
        archivoMenu.setStyle(estiloPopupMenu());
        archivoMenu.setOpacity(0);
        archivoMenu.setTranslateY(-8);

        archivoPopup = new Popup();
        archivoPopup.getContent().add(archivoMenu);
        archivoPopup.setAutoHide(false);
        archivoPopup.setOnHidden(e -> {
            popupVisible = false;
            btnRef.setStyle(estiloBotonArchivo(false));
            flecha.setText("▾");
        });
    }

    private void mostrarAlertaError() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText("Algo salió mal");
        a.setContentText("Ocurrió un error al procesar la operación.");
        a.showAndWait();
    }

    private HBox crearItemMenu(String icono, String texto, String atajo,
                               javafx.event.EventHandler<MouseEvent> h) {
        Label ico  = new Label(icono);
        ico.setStyle("-fx-font-size:13px; -fx-min-width:22; -fx-text-fill:" + text() + ";");
        Label lbl  = new Label(texto);
        lbl.setStyle("-fx-font-family:'Consolas'; -fx-font-size:12px; -fx-text-fill:" + text() + ";");
        Region sp  = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label sc   = new Label(atajo);
        sc.setStyle("-fx-font-family:'Consolas'; -fx-font-size:10px; -fx-text-fill:" + textMuted() + ";");
        HBox item  = new HBox(10, ico, lbl, sp, sc);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(7,12,7,10));
        item.setStyle("-fx-background-radius:6; -fx-cursor:hand;");
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color:" + menuHover() + "; -fx-background-radius:6; -fx-cursor:hand;"));
        item.setOnMouseExited(e  -> item.setStyle("-fx-background-radius:6; -fx-cursor:hand;"));
        item.setOnMouseClicked(h);
        return item;
    }

    private void abrirMenuArchivo(Button ref) {
        if (archivoPopup == null) return;
        double x = ref.localToScreen(0,0).getX();
        double y = ref.localToScreen(0,0).getY() + ref.getHeight() + 6;
        archivoMenu.setStyle(estiloPopupMenu());
        archivoPopup.show(ref, x, y);
        popupVisible = true;
        FadeTransition ft = new FadeTransition(Duration.millis(180), archivoMenu);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
        TranslateTransition tt = new TranslateTransition(Duration.millis(180), archivoMenu);
        tt.setFromY(-10); tt.setToY(0); tt.play();
    }

    private void cerrarMenuArchivo() {
        if (!popupVisible || archivoPopup == null) return;
        FadeTransition ft = new FadeTransition(Duration.millis(140), archivoMenu);
        ft.setFromValue(1); ft.setToValue(0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(140), archivoMenu);
        tt.setFromY(0); tt.setToY(-8);
        ft.setOnFinished(e -> { archivoPopup.hide(); popupVisible = false; });
        ft.play(); tt.play();
    }

    private boolean esHijoDePopup(Node node) {
        Node n = node;
        while (n != null) { if (n == archivoMenu) return true; n = n.getParent(); }
        return false;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BOTÓN MODO
    // ════════════════════════════════════════════════════════════════════════
    private Button crearBtnModo() {
        Button btn = new Button();
        btn.setGraphic(contenidoBtnModo());
        btn.setStyle(estiloBtnModo());
        btn.setOnMouseEntered(e -> btn.setStyle(estiloBtnModoHover()));
        btn.setOnMouseExited(e  -> btn.setStyle(estiloBtnModo()));
        return btn;
    }

    private HBox contenidoBtnModo() {
        Label ic  = new Label(modoOscuro ? "☀" : "🌙");
        ic.setStyle("-fx-font-size:14px;");
        Label txt = new Label(modoOscuro ? "Modo Luz" : "Modo Oscuro");
        txt.setStyle("-fx-font-family:'Consolas'; -fx-font-size:11px; " +
                "-fx-text-fill:" + (modoOscuro ? D_TEXT : L_TEXT) + ";");
        HBox c = new HBox(7, ic, txt); c.setAlignment(Pos.CENTER);
        return c;
    }

    private String estiloBtnModo() {
        return modoOscuro
                ? "-fx-background-color:#1E2A42; -fx-border-color:#3A4A6A; " +
                "-fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 12; -fx-cursor:hand;"
                : "-fx-background-color:#E0E8FF; -fx-border-color:#9AB0E0; " +
                "-fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 12; -fx-cursor:hand;";
    }

    private String estiloBtnModoHover() {
        return modoOscuro
                ? "-fx-background-color:#26345A; -fx-border-color:#5B8DEF; " +
                "-fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 12; -fx-cursor:hand;"
                : "-fx-background-color:#C8D8FF; -fx-border-color:#3A6FD8; " +
                "-fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 12; -fx-cursor:hand;";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TOGGLE MODO
    // ════════════════════════════════════════════════════════════════════════
    private void toggleModo() {
        modoOscuro = !modoOscuro;

        ScaleTransition st = new ScaleTransition(Duration.millis(120), btn_modo);
        st.setFromX(1.0); st.setFromY(1.0); st.setToX(0.92); st.setToY(0.92);
        st.setAutoReverse(true); st.setCycleCount(2); st.play();

        btn_modo.setGraphic(contenidoBtnModo());
        btn_modo.setStyle(estiloBtnModo());
        btn_modo.setOnMouseEntered(e -> btn_modo.setStyle(estiloBtnModoHover()));
        btn_modo.setOnMouseExited(e  -> btn_modo.setStyle(estiloBtnModo()));

        root.setStyle("-fx-background-color:" + bg() + ";");
        header.setStyle(estiloHeader());
        lblLogo.setStyle(estiloLogo());
        lblSub.setStyle(estiloLogoSub());

        panelEditor.setStyle("-fx-background-color:" + surface() + ";");
        barraEditor.setStyle(barraInternaCss());
        lbl_editorTitle.setStyle(estiloEtiquetaPanel());
        lbl_lang.setStyle(estiloLangBadge());

        barraConsola.setStyle(barraConsolaFijaCss());
        lbl_consTitle.setStyle(estiloEtiquetaPanel());

        // Tabla de símbolos
        if (panelTabla.isVisible()) {
            panelTabla.setStyle("-fx-background-color:" + surface() + "; " +
                    "-fx-border-color:" + border() + "; -fx-border-width:0 0 0 1;");
            barraTabla.setStyle(barraInternaCss());
            lbl_tablaTitle.setStyle(estiloEtiquetaPanel());
            lbl_tablaBadge.setStyle(estiloBadgeTabla());
            vbox_filas.setStyle("-fx-background-color:" + surface() + ";");
            scroll_tabla.setStyle("-fx-background:" + surface() + "; " +
                    "-fx-background-color:" + surface() + "; -fx-border-color:transparent;");
            // Re-renderizar filas con nuevos colores
            Simbolo[] simbolos = TablaSimbolos.getAll();
            vbox_filas.getChildren().clear();
            if (simbolos != null) {
                for (int i = 0; i < simbolos.length; i++)
                    vbox_filas.getChildren().add(crearFilaSimbolo(simbolos[i], i));
            }
        }

        lbl_version.setStyle(estiloStatusItem());
        lbl_lineCount.setStyle(estiloStatusItem());
        lbl_tokenCount.setStyle(estiloStatusItem());
        statusBar.setStyle(estiloStatusBar());

        if (modoOscuro) {
            cda_consola.getStyleClass().remove("AreaEditable");
            cda_consola.getStyleClass().add("AreaEditableNoche");
        } else {
            cda_consola.getStyleClass().remove("AreaEditableNoche");
            cda_consola.getStyleClass().add("AreaEditable");
        }

        configurarNumerosLinea();
        actualizarEstiloBtnCompilar();
        if (archivoPopup != null) { archivoPopup.hide(); popupVisible = false; }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INICIAR EDITOR
    // ════════════════════════════════════════════════════════════════════════
    private void IniciarEditor() {
        cda_consola.getStyleClass().add(modoOscuro ? "AreaEditableNoche" : "AreaEditable");
        configurarNumerosLinea();
        cda_consola.textProperty().addListener((obs, viejo, nuevo) -> {
            try {
                if (!nuevo.isEmpty() && !nuevo.isBlank())
                    cda_consola.setStyleSpans(0, identificarPalabras(nuevo));
                if (lbl_lineCount != null)
                    lbl_lineCount.setText("Líneas: " + (nuevo.isEmpty() ? 0 : nuevo.lines().count()));
            } catch (Exception ignored) {}
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  COMPILACIÓN
    // ════════════════════════════════════════════════════════════════════════
    private void ejecutarCompilacion() {
        limpiarConsola();
        ocultarTablaSimbolos();         // siempre ocultar al inicio
        TablaSimbolos.inicializar();    // limpiar tabla antes de compilar
        PilaErrores.dump();             // vaciar pila residual de análisis léxico

        String codigoFuente = cda_consola.getText();
        String[] codigo_limpio = Tokenizador.limpiar(limpiar(codigoArreglo));

        consolaInfo("── Iniciando compilación ──────────────────────");

        // ── FASE 1: Análisis Sintáctico ───────────────────────────────────
        if (!AutomataSintax.analizar(codigo_limpio)) {
            // Error sintáctico — NO mostrar tabla
            String errorSin = AutomataSintax.getError();
            setStatus(danger(), "Error sintáctico");
            consolaError("FASE 2 · Análisis Sintáctico — FALLIDO");

            if (errorSin != null && !errorSin.isBlank()) {
                // AutomataSintax puede devolver un mensaje con el token inesperado
                // Intentar extraer el token para buscar la línea
                int numLinea = obtenerNumeroLineaDeError(errorSin, codigoFuente);
                String sufijo = numLinea > 0 ? "  →  línea " + numLinea : "";
                consolaError("[E-SIN] " + errorSin + sufijo);
            } else {
                consolaError("[E-SIN] Error sintáctico inesperado.");
            }

            // Vaciar cualquier error que AutomataSintax haya empujado
            PilaErrores.dump();
            consolaInfo("── Compilación terminada con errores ──────────");
            return;   // ← salir sin tocar la tabla
        }

        consolaOk("FASE 2 · Análisis Sintáctico — OK ✓");

        // ── FASE 2: Análisis Semántico (AST) ─────────────────────────────
        AST ast = new AST();
        ast.crearAST(codigo_limpio);

        if (ast.validar(ast.getPrograma())) {
            // Compilación exitosa
            setStatus(success(), "Compilación exitosa");
            consolaOk("FASE 3 · Análisis Semántico — OK ✓");
            consolaOk("Compilación completada sin errores.");
            consolaInfo("── Compilación exitosa ────────────────────────");
            mostrarTablaSimbolos();     // tabla SOLO aquí

        } else {
            // Errores semánticos — mostrar tabla parcial (símbolos ya insertados)
            setStatus(danger(), "Error semántico");
            consolaError("FASE 3 · Análisis Semántico — FALLIDO");

            String[] erroresSem = PilaErrores.dump();
            if (erroresSem != null && erroresSem.length > 0) {
                for (String err : erroresSem) {
                    // Buscar número de línea del identificador mencionado en el error
                    int nl = obtenerNumeroLineaDeError(err, codigoFuente);
                    String sufijo = nl > 0 ? "  →  línea " + nl : "";
                    consolaError("[E-SEM] " + err + sufijo);
                }
            } else {
                consolaError("[E-SEM] Error semántico sin descripción adicional.");
            }

            consolaInfo("── Compilación terminada con errores ──────────");
            mostrarTablaSimbolos();     // tabla parcial (útil para depurar)
        }
    }

    /**
     * Intenta encontrar el número de línea relacionado con un mensaje de error.
     * Extrae tokens del mensaje y los busca en el código fuente línea por línea.
     */
    private int obtenerNumeroLineaDeError(String mensajeError, String codigoFuente) {
        if (mensajeError == null || codigoFuente == null) return -1;
        String[] lineas = codigoFuente.split("\n", -1);

        // Buscar palabras entre comillas simples o dobles dentro del mensaje
        java.util.regex.Pattern pat =
                java.util.regex.Pattern.compile("['\"]([^'\"]+)['\"]");
        java.util.regex.Matcher mat = pat.matcher(mensajeError);
        while (mat.find()) {
            String token = mat.group(1).trim();
            if (!token.isEmpty()) {
                for (int i = 0; i < lineas.length; i++) {
                    if (lineas[i].contains(token)) return i + 1;
                }
            }
        }

        // Si no hay token entre comillas, buscar cualquier palabra no común del mensaje
        String[] palabrasMensaje = mensajeError.split("\\s+");
        for (String p : palabrasMensaje) {
            // Ignorar palabras genéricas del mensaje de error
            if (p.length() <= 2 || p.equalsIgnoreCase("no") || p.equalsIgnoreCase("en")
                    || p.equalsIgnoreCase("el") || p.equalsIgnoreCase("la")
                    || p.equalsIgnoreCase("de") || p.equalsIgnoreCase("ya")
                    || p.equalsIgnoreCase("tipos") || p.equalsIgnoreCase("tipo")
                    || p.equalsIgnoreCase("error") || p.equalsIgnoreCase("identificador"))
                continue;
            for (int i = 0; i < lineas.length; i++) {
                if (lineas[i].contains(p)) return i + 1;
            }
        }
        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ANÁLISIS LÉXICO
    // ════════════════════════════════════════════════════════════════════════
    private StyleSpans<Collection<String>> identificarPalabras(String codigo) {
        StyleSpansBuilder<Collection<String>> csb = new StyleSpansBuilder<>();
        codigoArreglo = Tokenizador.tokenizar(codigo);
        error_lexico  = false;
        int tokenCount = 0;
        List<String> erroresLex = new ArrayList<>();

        for (String palabra : codigoArreglo) {
            int len = palabra.length();
            if (AutomataPalabrasReservadas.analizar(palabra)) {
                tokenCount++;
                switch (AutomataPalabrasReservadas.getPalabra()) {
                    case PR04: case PR05: case PR06: case PR07: case PR08:
                        csb.add(Collections.singleton("tiposDatos"), len); break;
                    case PR12: case PR13:
                        csb.add(Collections.singleton("booleans"), len); break;
                    default:
                        csb.add(Collections.singleton("palabraReservada"), len);
                }
            } else if (AutomataCadena.analizar(palabra)) {
                tokenCount++; csb.add(Collections.singleton("cadena"), len);
            } else if (AutomataNumero.analizar(palabra)) {
                tokenCount++; csb.add(Collections.singleton("default"), len);
            } else if (AutomataID.analizar(palabra)) {
                tokenCount++; csb.add(Collections.singleton("identificador"), len);
            } else if (!palabra.isEmpty()
                    && (palabra.charAt(0) == ' '  || palabra.charAt(0) == '\n'
                    || palabra.charAt(0) == '\t'  || palabra.charAt(0) == '{'
                    || palabra.charAt(0) == '}'   || palabra.charAt(0) == '('
                    || palabra.charAt(0) == ')'   || palabra.charAt(0) == ';'
                    || palabra.charAt(0) == '+'   || palabra.charAt(0) == '-'
                    || palabra.charAt(0) == '*'   || palabra.charAt(0) == '/'
                    || palabra.charAt(0) == '<'   || palabra.charAt(0) == '>'
                    || palabra.charAt(0) == '='   || palabra.equals("||")
                    || palabra.equals("&&")        || palabra.equals("<=")
                    || palabra.equals(">=")        || palabra.equals("!="))) {
                csb.add(Collections.singleton("default"), len);
            } else if (palabra.charAt(0) == '%') {
                tokenCount++; csb.add(Collections.singleton("comentario"), len);
            } else {
                csb.add(Collections.singleton("error"), len);
                error_lexico = true;
                int nl = obtenerNumeroLinea(palabra, cda_consola.getText());
                erroresLex.add("[E-L01] Token inválido '" + palabra + "'" +
                        (nl > 0 ? " en línea " + nl : ""));
                PilaErrores.push("[E-L01] Token inválido '" + palabra + "'" +
                        (nl > 0 ? " en línea " + nl : ""));
            }
        }

        final int tc = tokenCount;
        final List<String> copia = new ArrayList<>(erroresLex);

        Platform.runLater(() -> {
            if (lbl_tokenCount != null) lbl_tokenCount.setText("Tokens: " + tc);
        });

        if (error_lexico) {
            mit_compilar.setDisable(true);
            setStatus(danger(), "Error léxico detectado");
            // Mostrar errores léxicos en tiempo real mientras el usuario escribe
            // Se limpia y redibuja en cada pulsación para reflejar el estado actual
            limpiarConsola();
            consolaError("FASE 1 · Análisis Léxico — FALLIDO");
            copia.forEach(this::consolaError);
            // NO vaciar PilaErrores aquí con dump() — dejar que ejecutarCompilacion lo haga
        } else {
            mit_compilar.setDisable(false);
            setStatus(success(), "Todo en orden");
            limpiarConsola();
            consolaOk("FASE 1 · Análisis Léxico — OK ✓");
            consolaOk("Sin errores léxicos detectados.");
        }

        return csb.create();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ESTILOS DINÁMICOS
    // ════════════════════════════════════════════════════════════════════════
    private String estiloHeader()      { return "-fx-background-color:" + surface() + "; -fx-border-color:" + border() + "; -fx-border-width:0 0 1 0;"; }
    private String estiloLogo()        { return "-fx-font-family:'Consolas'; -fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:" + text() + ";"; }
    private String estiloLogoSub()     { return "-fx-font-family:'Consolas'; -fx-font-size:11px; -fx-text-fill:" + textMuted() + "; -fx-padding:2 0 0 4;"; }
    private String estiloEtiquetaPanel(){ return "-fx-font-family:'Consolas'; -fx-font-size:10px; -fx-font-weight:bold; -fx-text-fill:" + textMuted() + ";"; }
    private String estiloLangBadge()   { return "-fx-background-color:" + accent() + "22; -fx-text-fill:" + accent() + "; -fx-font-family:'Consolas'; -fx-font-size:10px; -fx-padding:2 8 2 8; -fx-background-radius:20;"; }
    private String barraInternaCss()   { return "-fx-background-color:" + surface2() + "; -fx-border-color:" + border() + "; -fx-border-width:0 0 1 0;"; }
    private String barraConsolaFijaCss(){ return "-fx-background-color:#F8F9FB; -fx-border-color:" + CON_BORDER + "; -fx-border-width:0 0 1 0;"; }
    private String estiloStatusBar()   { return "-fx-background-color:" + surface2() + "; -fx-border-color:" + border() + "; -fx-border-width:1 0 0 0;"; }
    private String estiloStatusItem()  { return "-fx-font-family:'Consolas'; -fx-font-size:10px; -fx-text-fill:" + textMuted() + ";"; }
    private String estiloStatusText()  { return "-fx-font-family:'Consolas'; -fx-font-size:10px; -fx-text-fill:" + textMuted() + ";"; }
    private String estiloBtnLimpiar()  { return "-fx-background-color:transparent; -fx-text-fill:" + CON_MUTED + "; -fx-font-family:'Consolas'; -fx-font-size:10px; -fx-border-color:" + CON_BORDER + "; -fx-border-radius:4; -fx-background-radius:4; -fx-padding:2 8 2 8; -fx-cursor:hand;"; }
    private String estiloBotonArchivo(boolean hover) {
        return hover
                ? (modoOscuro ? "-fx-background-color:" + D_MENU_HOVER + "; -fx-border-color:" + D_BORDER + "; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 10; -fx-cursor:hand;"
                : "-fx-background-color:" + L_MENU_HOVER + "; -fx-border-color:" + L_BORDER + "; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 10; -fx-cursor:hand;")
                : (modoOscuro ? "-fx-background-color:" + D_SURFACE2   + "; -fx-border-color:" + D_BORDER + "; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 10; -fx-cursor:hand;"
                : "-fx-background-color:" + L_SURFACE2   + "; -fx-border-color:" + L_BORDER + "; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 14 6 10; -fx-cursor:hand;");
    }
    private String estiloPopupMenu()   { return "-fx-background-color:" + surface() + "; -fx-border-color:" + border() + "; -fx-border-radius:8; -fx-background-radius:8; -fx-effect:dropshadow(gaussian,rgba(0,0,0,0.35),12,0,0,4); -fx-min-width:200;"; }

    // ════════════════════════════════════════════════════════════════════════
    //  BOTÓN COMPILAR
    // ════════════════════════════════════════════════════════════════════════
    private Button crearBtnCompilar() {
        Button btn = new Button("▶  Compilar");
        aplicarEstiloBtnCompilar(btn);
        return btn;
    }

    private void aplicarEstiloBtnCompilar(Button btn) {
        String base  = "-fx-background-color:" + accent() + "; -fx-text-fill:white; -fx-font-family:'Consolas'; -fx-font-size:11px; -fx-font-weight:bold; -fx-padding:6 16 6 16; -fx-background-radius:6; -fx-cursor:hand;";
        String hover = "-fx-background-color:" + accentDark() + "; -fx-text-fill:white; -fx-font-family:'Consolas'; -fx-font-size:11px; -fx-font-weight:bold; -fx-padding:6 16 6 16; -fx-background-radius:6; -fx-cursor:hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
    }

    private void actualizarEstiloBtnCompilar() {
        if (btn_compilar_header != null) aplicarEstiloBtnCompilar(btn_compilar_header);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private void setStatus(String color, String mensaje) {
        Platform.runLater(() -> {
            if (lbl_statusDot  != null) lbl_statusDot.setStyle("-fx-text-fill:" + color + "; -fx-font-size:9px;");
            if (lbl_statusText != null) lbl_statusText.setText(mensaje);
        });
    }

    private String[] limpiar(String[] entrada) {
        for (int i = 0; i < entrada.length; i++) {
            char c = entrada[i].charAt(0);
            if (c == '\n' || c == '\t' || entrada[i].equals(" ")) entrada[i] = " ";
        }
        return entrada;
    }

    public static void setErrorSemantico(boolean flag) { error_semantico = flag; }
    public static void main(String[] args) { launch(); }
}


// ════════════════════════════════════════════════════════════════════════════
//  TOKENIZADOR
// ════════════════════════════════════════════════════════════════════════════
class Tokenizador {
    private static String[] tokens;

    public static String[] tokenizar(String texto) {
        tokens = new String[1];
        char caracter;
        String espacio = "", palabra = "", comentario = "", cadena = "";
        boolean flag_cadena = false, flag_comentario = false;
        for (int i = 0; i < texto.length(); i++) {
            caracter = texto.charAt(i);
            switch (caracter) {
                case ' ': case '\n': case '\t':
                    if (flag_comentario)       { comentario += caracter; if (i==texto.length()-1) addToken(comentario); }
                    else if (flag_cadena)      { cadena    += caracter; if (i==texto.length()-1) addToken(cadena); }
                    else { espacio += caracter; if (!palabra.isEmpty()) { addToken(palabra); palabra=""; } else if (tokens[0]==null) { tokens[0]=palabra; palabra=""; } }
                    break;
                case '{': case '}': case '(': case ')':
                case ';': case '+': case '-': case '*': case '/': case '=':
                    if (flag_comentario) { comentario += caracter; if (i==texto.length()-1) addToken(comentario); }
                    else if (!palabra.isEmpty()) { addToken(palabra); palabra=""; }
                    palabra = caracter + ""; addToken(palabra); palabra = "";
                    break;
                case '%':
                    if (!palabra.isEmpty()) { addToken(palabra); palabra=""; }
                    if (!espacio.isEmpty()) { addToken(espacio); espacio=""; }
                    if (flag_comentario) { flag_comentario=false; comentario+=caracter; addToken(comentario); comentario=""; }
                    else { flag_comentario=true; comentario+=caracter; }
                    break;
                case '|': case '&':
                    if (flag_comentario) { comentario+=caracter; if (i==texto.length()-1) addToken(comentario); }
                    else { if (!palabra.isEmpty() && palabra.charAt(0)!='|' && palabra.charAt(0)!='&') { addToken(palabra); palabra=""; } palabra+=caracter; if (!espacio.isEmpty()) { addToken(espacio); espacio=""; } else if (tokens[0]==null) { tokens[0]=espacio; espacio=""; } }
                    break;
                case '"':
                    if (!palabra.isEmpty()) { addToken(palabra); palabra=""; }
                    if (!espacio.isEmpty()) { addToken(espacio); espacio=""; }
                    if (flag_cadena) { flag_cadena=false; cadena+=caracter; addToken(cadena); cadena=""; }
                    else { flag_cadena=true; cadena+=caracter; }
                    break;
                default:
                    if (flag_comentario)  { comentario+=caracter; if (i==texto.length()-1) addToken(comentario); }
                    else if (flag_cadena) { cadena+=caracter; if (i==texto.length()-1) addToken(cadena); }
                    else { if (palabra.contains("|") || palabra.contains("&")) { addToken(palabra); palabra=""; } palabra+=caracter; if (!espacio.isEmpty()) { addToken(espacio); espacio=""; } else if (tokens[0]==null) { tokens[0]=espacio; espacio=""; } }
            }
            if (i == texto.length()-1) { if (!espacio.isEmpty()) addToken(espacio); else if (!palabra.isEmpty()) addToken(palabra); }
        }
        String[] aux = new String[tokens.length-1];
        for (int i=1; i<tokens.length; i++) aux[i-1]=tokens[i];
        tokens=aux;
        return unirOperadores(tokens);
    }

    private static void addToken(String token) {
        String[] aux = tokens==null ? new String[1] : tokens;
        tokens = new String[aux.length+1];
        for (int i=0; i<aux.length; i++) if (aux[i]!=null) tokens[i]=aux[i];
        tokens[aux.length]=token;
    }

    public static String[] limpiar(String[] tokens) {
        int cant=0; for (String t:tokens) if (!t.isBlank()) cant++;
        String[] nuevo=new String[cant];
        for (int j=0,i=0; i<tokens.length; i++) if (!tokens[i].isBlank()) { nuevo[j]=tokens[i]; j++; }
        return nuevo;
    }

    public static String[] unirOperadores(String[] tokens) {
        List<String> res=new ArrayList<>();
        for (int i=0; i<tokens.length; i++) {
            String a=tokens[i];
            if ((a.equals("=")||a.equals("!")||a.equals("<")||a.equals(">")) && i+1<tokens.length && tokens[i+1].equals("=")) { res.add(a+"="); i++; }
            else res.add(a);
        }
        return res.toArray(new String[0]);
    }
}

class CorrectorLexico extends Thread { public void checar() {} }