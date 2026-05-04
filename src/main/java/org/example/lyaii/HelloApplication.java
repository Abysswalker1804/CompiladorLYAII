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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.lyaii.Assembly8086.GeneradorASM;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.example.lyaii.AST.AST;
import org.example.lyaii.Automatas.AutomataCadena;
import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Automatas.AutomataNumero;
import org.example.lyaii.Automatas.AutomataPalabrasReservadas;
import org.example.lyaii.Automatas.AutomataSintax;
import org.example.lyaii.Tools.PilaErrores;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HelloApplication extends Application {

    // ── Estado ───────────────────────────────────────────────────────────────
    private Scene    scene;
    private CodeArea cda_consola;
    private CodeArea cda_error;
    private boolean  error_lexico = false;
    private String[] codigoArreglo;
    private MenuItem mit_compilar;
    private static boolean error_semantico = false;

    // ── Modo actual ──────────────────────────────────────────────────────────
    private boolean modoOscuro = true;  // inicia en oscuro

    // ── Widgets de estado ────────────────────────────────────────────────────
    private Label lbl_statusDot;
    private Label lbl_statusText;
    private Label lbl_lineCount;
    private Label lbl_tokenCount;

    // ── Nodos que se reestilan al cambiar modo ───────────────────────────────
    private VBox  root;
    private HBox  header;
    private HBox  cuerpo;
    private HBox  statusBar;
    private HBox  barraEditor;
    private HBox  barraConsola;
    private VBox  panelEditor;
    private VBox  panelConsola;
    private Label lbl_editorTitle;
    private Label lbl_consTitle;
    private Label lbl_lang;
    private Label lbl_version;
    private Button btn_modo;
    private Button btn_compilar_header;
    private Label  lblLogo;
    private Label  lblSub;

    // ── Popup del menú archivo ───────────────────────────────────────────────
    private Popup   archivoPopup;
    private VBox    archivoMenu;
    private boolean popupVisible = false;

    // ══════════════════════════════════════════════════════════════════════
    //  PALETAS  (dark / light)
    // ══════════════════════════════════════════════════════════════════════

    // — DARK —
    private static final String D_BG          = "#0E1017";
    private static final String D_SURFACE      = "#161B27";
    private static final String D_SURFACE2     = "#1E2336";
    private static final String D_BORDER       = "#2A304A";
    private static final String D_ACCENT       = "#5B8DEF";
    private static final String D_ACCENT_DARK  = "#4A7ADE";
    private static final String D_SUCCESS      = "#3DDC84";
    private static final String D_DANGER       = "#FF6B6B";
    private static final String D_TEXT         = "#D0D8EC";
    private static final String D_TEXT_MUTED   = "#5E6E8C";
    private static final String D_TEXT_DIM     = "#2A3450";
    private static final String D_MENU_HOVER   = "#232B42";

    // — LIGHT —
    private static final String L_BG           = "#F1F3F8";
    private static final String L_SURFACE       = "#FFFFFF";
    private static final String L_SURFACE2      = "#E8EBF4";
    private static final String L_BORDER        = "#C8CEDF";
    private static final String L_ACCENT        = "#3A6FD8";
    private static final String L_ACCENT_DARK   = "#2A5CC4";
    private static final String L_SUCCESS       = "#1A9E5C";
    private static final String L_DANGER        = "#D63B3B";
    private static final String L_TEXT          = "#1A2035";
    private static final String L_TEXT_MUTED    = "#6B7490";
    private static final String L_TEXT_DIM      = "#B0B8CF";
    private static final String L_MENU_HOVER    = "#EEF1FA";

    // ── Getters dinámicos de color según modo ────────────────────────────────
    private String bg()         { return modoOscuro ? D_BG          : L_BG;          }
    private String surface()    { return modoOscuro ? D_SURFACE      : L_SURFACE;     }
    private String surface2()   { return modoOscuro ? D_SURFACE2     : L_SURFACE2;    }
    private String border()     { return modoOscuro ? D_BORDER       : L_BORDER;      }
    private String accent()     { return modoOscuro ? D_ACCENT       : L_ACCENT;      }
    private String accentDark() { return modoOscuro ? D_ACCENT_DARK  : L_ACCENT_DARK; }
    private String success()    { return modoOscuro ? D_SUCCESS      : L_SUCCESS;     }
    private String danger()     { return modoOscuro ? D_DANGER       : L_DANGER;      }
    private String text()       { return modoOscuro ? D_TEXT         : L_TEXT;        }
    private String textMuted()  { return modoOscuro ? D_TEXT_MUTED   : L_TEXT_MUTED;  }
    private String textDim()    { return modoOscuro ? D_TEXT_DIM     : L_TEXT_DIM;    }
    private String menuHover()  { return modoOscuro ? D_MENU_HOVER   : L_MENU_HOVER;  }

    // ════════════════════════════════════════════════════════════════════════
    @Override
    public void start(Stage stage) throws IOException {
        crearUI();
        scene.getStylesheets().add(
                getClass().getResource("/css/main.css").toString());
        stage.setTitle("CORE Compiler");
        stage.setMinWidth(860);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN PRINCIPAL
    // ════════════════════════════════════════════════════════════════════════
    private void crearUI() {
        cda_consola = new CodeArea();
        cda_error   = new CodeArea();
        cda_error.setEditable(false);
        cda_error.setWrapText(true);
        IniciarEditor();

        header    = crearHeader();
        cuerpo    = crearCuerpo();
        statusBar = crearStatusBar();

        VBox.setVgrow(cuerpo, Priority.ALWAYS);

        root = new VBox(header, cuerpo, statusBar);
        root.setStyle("-fx-background-color: " + bg() + ";");

        scene = new Scene(root, 1100, 680);

        // Cerrar popup al hacer clic fuera
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (popupVisible && archivoPopup != null) {
                Node src = (Node) e.getTarget();
                if (!esHijoDePopup(src)) cerrarMenuArchivo();
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HEADER
    // ════════════════════════════════════════════════════════════════════════
    private HBox crearHeader() {

        // ── Logo ──────────────────────────────────────────────────────────
        lblLogo = new Label("◈  CORE");
        lblLogo.setStyle(estiloLogo());

        lblSub = new Label("compiler");
        lblSub.setStyle(estiloLogoSub());

        HBox logo = new HBox(4, lblLogo, lblSub);
        logo.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ── Botón Archivo (personalizado con popup) ────────────────────────
        Button btn_archivo = crearBotonArchivo();

        // ── Botón Compilar ────────────────────────────────────────────────
        btn_compilar_header = crearBtnCompilar();
        btn_compilar_header.setDisable(true);
        btn_compilar_header.setOnAction(e -> ejecutarCompilacion());

        mit_compilar = new MenuItem("Compilar");
        mit_compilar.setDisable(true);
        mit_compilar.setOnAction(e -> ejecutarCompilacion());
        mit_compilar.disableProperty().addListener(
                (obs, o, n) -> btn_compilar_header.setDisable(n));

        // ── Botón Modo ────────────────────────────────────────────────────
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
    //  BOTÓN ARCHIVO CON MENÚ POPUP ANIMADO
    // ════════════════════════════════════════════════════════════════════════
    private Button crearBotonArchivo() {
        // Icono de carpeta (SVG path)
        SVGPath iconoCarpeta = new SVGPath();
        iconoCarpeta.setContent(
                "M1 3.5A1.5 1.5 0 012.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 " +
                        "3.648 8.378 4 9 4h4.5A1.5 1.5 0 0115 5.5v1H1v-3zM0 8a1 1 0 011-1h14" +
                        "a1 1 0 011 1v5.5A1.5 1.5 0 0114.5 15h-13A1.5 1.5 0 010 13.5V8z");
        iconoCarpeta.setScaleX(1.0);
        iconoCarpeta.setScaleY(1.0);

        // Flecha indicadora
        Label flecha = new Label("▾");
        flecha.setStyle(
                "-fx-font-size: 10px; -fx-text-fill: " + textMuted() + ";");

        Label textoBtn = new Label("Archivo");
        textoBtn.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 12px; " +
                        "-fx-text-fill: " + text() + ";");

        HBox contenidoBtn = new HBox(7, iconoCarpeta, textoBtn, flecha);
        contenidoBtn.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setGraphic(contenidoBtn);
        actualizarIconoCarpeta(iconoCarpeta, text());

        String estiloBase = estiloBotonArchivo(false);
        String estiloHover = estiloBotonArchivo(true);
        btn.setStyle(estiloBase);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e  -> {
            if (!popupVisible) btn.setStyle(estiloBase);
        });

        btn.setOnAction(e -> {
            if (popupVisible) {
                cerrarMenuArchivo();
                btn.setStyle(estiloBase);
            } else {
                abrirMenuArchivo(btn);
                btn.setStyle(estiloHover);
            }
        });

        // Construir el popup
        construirPopupArchivo(btn, textoBtn, flecha, iconoCarpeta);

        return btn;
    }

    private void actualizarIconoCarpeta(SVGPath path, String color) {
        path.setFill(Color.web(color));
        path.setStyle("-fx-scale-x: 0.85; -fx-scale-y: 0.85;");
    }

    private void construirPopupArchivo(Button btnRef, Label textoBtn,
                                       Label flecha, SVGPath iconoCarpeta) {
        // Ítem: Nuevo archivo
        HBox item1 = crearItemMenu("📄", "Nuevo archivo",
                "Ctrl+N", e -> {});

        // Ítem: Abrir archivo
        HBox item2 = crearItemMenu("📂", "Abrir archivo",
                "Ctrl+O", e -> cerrarMenuArchivo());

        // Separador
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + border() + "; -fx-padding: 0;");
        VBox.setMargin(sep, new Insets(4, 0, 4, 0));

        // Ítem: Guardar
        HBox item3 = crearItemMenu("💾", "Guardar",
                "Ctrl+S", e -> cerrarMenuArchivo());

        // Ítem: Compilar (refleja estado del mit_compilar)
        HBox item4 = crearItemMenu("▶", "Compilar",
                "F5", e -> {
                    cerrarMenuArchivo();
                    ejecutarCompilacion();
                });

        archivoMenu = new VBox(4, item1, item2, sep, item3, item4);
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

    private HBox crearItemMenu(String icono, String texto,
                               String atajo, javafx.event.EventHandler<MouseEvent> handler) {
        Label ico = new Label(icono);
        ico.setStyle(
                "-fx-font-size: 13px; -fx-min-width: 22; -fx-text-fill: " + text() + ";");

        Label lbl = new Label(texto);
        lbl.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 12px; " +
                        "-fx-text-fill: " + text() + ";");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label shortcut = new Label(atajo);
        shortcut.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                        "-fx-text-fill: " + textMuted() + ";");

        HBox item = new HBox(10, ico, lbl, sp, shortcut);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(7, 12, 7, 10));
        item.setStyle("-fx-background-radius: 6; -fx-cursor: hand;");

        item.setOnMouseEntered(e -> item.setStyle(
                "-fx-background-color: " + menuHover() + "; " +
                        "-fx-background-radius: 6; -fx-cursor: hand;"));
        item.setOnMouseExited(e -> item.setStyle(
                "-fx-background-radius: 6; -fx-cursor: hand;"));
        item.setOnMouseClicked(handler);

        return item;
    }

    private void abrirMenuArchivo(Button btnRef) {
        if (archivoPopup == null || archivoMenu == null) return;
        double x = btnRef.localToScreen(0, 0).getX();
        double y = btnRef.localToScreen(0, 0).getY() + btnRef.getHeight() + 6;
        archivoMenu.setStyle(estiloPopupMenu());
        archivoPopup.show(btnRef, x, y);
        popupVisible = true;

        // Animación: fade + slide hacia abajo
        FadeTransition fade = new FadeTransition(Duration.millis(180), archivoMenu);
        fade.setFromValue(0); fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(180), archivoMenu);
        slide.setFromY(-10); slide.setToY(0);

        fade.play();
        slide.play();
    }

    private void cerrarMenuArchivo() {
        if (!popupVisible || archivoPopup == null) return;
        FadeTransition fade = new FadeTransition(Duration.millis(140), archivoMenu);
        fade.setFromValue(1); fade.setToValue(0);
        TranslateTransition slide = new TranslateTransition(Duration.millis(140), archivoMenu);
        slide.setFromY(0); slide.setToY(-8);
        fade.setOnFinished(e -> {
            archivoPopup.hide();
            popupVisible = false;
        });
        fade.play();
        slide.play();
    }

    private boolean esHijoDePopup(Node node) {
        if (archivoMenu == null) return false;
        Node n = node;
        while (n != null) {
            if (n == archivoMenu) return true;
            n = n.getParent();
        }
        return false;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BOTÓN MODO OSCURO / LUZ
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
        Label icono = new Label(modoOscuro ? "☀" : "🌙");
        icono.setStyle("-fx-font-size: 14px;");

        Label texto = new Label(modoOscuro ? "Modo Luz" : "Modo Oscuro");
        texto.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 11px; " +
                        "-fx-text-fill: " + (modoOscuro ? D_TEXT : L_TEXT) + ";");

        HBox contenido = new HBox(7, icono, texto);
        contenido.setAlignment(Pos.CENTER);
        return contenido;
    }

    private String estiloBtnModo() {
        return modoOscuro
                ? "-fx-background-color: #1E2A42; -fx-border-color: #3A4A6A; " +
                "-fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6 14 6 12; -fx-cursor: hand;"
                : "-fx-background-color: #E0E8FF; -fx-border-color: #9AB0E0; " +
                "-fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6 14 6 12; -fx-cursor: hand;";
    }

    private String estiloBtnModoHover() {
        return modoOscuro
                ? "-fx-background-color: #26345A; -fx-border-color: #5B8DEF; " +
                "-fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6 14 6 12; -fx-cursor: hand;"
                : "-fx-background-color: #C8D8FF; -fx-border-color: #3A6FD8; " +
                "-fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6 14 6 12; -fx-cursor: hand;";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TOGGLE MODO — aplica paleta completa a todos los nodos
    // ════════════════════════════════════════════════════════════════════════
    private void toggleModo() {
        modoOscuro = !modoOscuro;

        // Animación de escala breve en el botón
        ScaleTransition st = new ScaleTransition(Duration.millis(120), btn_modo);
        st.setFromX(1.0); st.setFromY(1.0);
        st.setToX(0.92);  st.setToY(0.92);
        st.setAutoReverse(true); st.setCycleCount(2);
        st.play();

        // Actualizar icono y texto del botón
        btn_modo.setGraphic(contenidoBtnModo());
        btn_modo.setStyle(estiloBtnModo());
        btn_modo.setOnMouseEntered(e -> btn_modo.setStyle(estiloBtnModoHover()));
        btn_modo.setOnMouseExited(e  -> btn_modo.setStyle(estiloBtnModo()));

        // Paleta global
        root.setStyle("-fx-background-color: " + bg() + ";");
        header.setStyle(estiloHeader());
        lblLogo.setStyle(estiloLogo());
        lblSub.setStyle(estiloLogoSub());

        // Paneles
        panelEditor.setStyle("-fx-background-color: " + surface() + ";");
        panelConsola.setStyle("-fx-background-color: " + surface() + ";");
        barraEditor.setStyle(barraInternaCss());
        barraConsola.setStyle(barraInternaCss());

        // Etiquetas de panel
        lbl_editorTitle.setStyle(estiloEtiquetaPanel());
        lbl_consTitle.setStyle(estiloEtiquetaPanel());
        lbl_lang.setStyle(estiloLangBadge());
        lbl_version.setStyle(estiloStatusItem());
        lbl_lineCount.setStyle(estiloStatusItem());
        lbl_tokenCount.setStyle(estiloStatusItem());

        // Status bar
        statusBar.setStyle(estiloStatusBar());

        // CodeArea
        if (modoOscuro) {
            cda_consola.getStyleClass().remove("AreaEditable");
            cda_consola.getStyleClass().add("AreaEditableNoche");
        } else {
            cda_consola.getStyleClass().remove("AreaEditableNoche");
            cda_consola.getStyleClass().add("AreaEditable");
        }

        // Botón compilar
        actualizarEstiloBtnCompilar();

        // Reconstruir popup con nuevos colores
        if (archivoPopup != null) {
            archivoPopup.hide();
            popupVisible = false;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CUERPO
    // ════════════════════════════════════════════════════════════════════════
    private HBox crearCuerpo() {

        // ── Panel editor ──────────────────────────────────────────────────
        lbl_editorTitle = new Label("EDITOR");
        lbl_editorTitle.setStyle(estiloEtiquetaPanel());

        lbl_lang = new Label("CORE-lang");
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
        VBox.setVgrow(cda_consola, Priority.ALWAYS);

        panelEditor = new VBox(barraEditor, cda_consola);
        panelEditor.setStyle("-fx-background-color: " + surface() + ";");
        HBox.setHgrow(panelEditor, Priority.ALWAYS);

        // ── Divisor ───────────────────────────────────────────────────────
        Separator divisor = new Separator(Orientation.VERTICAL);
        divisor.setStyle(
                "-fx-background-color: " + border() + "; " +
                        "-fx-pref-width: 1; -fx-min-width: 1; -fx-max-width: 1;");

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
        btn_limpiar.setOnAction(e -> cda_error.clear());

        barraConsola = new HBox(6,
                lbl_consTitle, lbl_statusDot, lbl_statusText, sp2, btn_limpiar);
        barraConsola.setAlignment(Pos.CENTER_LEFT);
        barraConsola.setPadding(new Insets(6, 12, 6, 12));
        barraConsola.setMinHeight(32);
        barraConsola.setMaxHeight(32);
        barraConsola.setStyle(barraInternaCss());

        cda_error.getStyleClass().add("AreaError");
        VBox.setVgrow(cda_error, Priority.ALWAYS);

        panelConsola = new VBox(barraConsola, cda_error);
        panelConsola.setStyle("-fx-background-color: " + surface() + ";");
        panelConsola.setPrefWidth(340);
        panelConsola.setMinWidth(240);

        HBox c = new HBox(panelEditor, divisor, panelConsola);
        VBox.setVgrow(panelEditor,  Priority.ALWAYS);
        VBox.setVgrow(panelConsola, Priority.ALWAYS);
        return c;
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
        sep1.setStyle("-fx-background-color: " + textDim() + ";");
        Separator sep2 = new Separator(Orientation.VERTICAL);
        sep2.setStyle("-fx-background-color: " + textDim() + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(14,
                lbl_lineCount, sep1, lbl_tokenCount, spacer, sep2, lbl_version);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(4, 16, 4, 16));
        bar.setMinHeight(24);
        bar.setMaxHeight(24);
        bar.setStyle(estiloStatusBar());
        return bar;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ESTILOS DINÁMICOS (dependen del modo)
    // ════════════════════════════════════════════════════════════════════════
    private String estiloHeader() {
        return "-fx-background-color: " + surface() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 0 0 1 0;";
    }

    private String estiloLogo() {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + text() + ";";
    }

    private String estiloLogoSub() {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 11px; " +
                "-fx-text-fill: " + textMuted() + "; -fx-padding: 2 0 0 4;";
    }

    private String estiloEtiquetaPanel() {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + textMuted() + ";";
    }

    private String estiloLangBadge() {
        return "-fx-background-color: " + accent() + "22; " +
                "-fx-text-fill: " + accent() + "; " +
                "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-padding: 2 8 2 8; -fx-background-radius: 20;";
    }

    private String barraInternaCss() {
        return "-fx-background-color: " + surface2() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 0 0 1 0;";
    }

    private String estiloStatusBar() {
        return "-fx-background-color: " + surface2() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-width: 1 0 0 0;";
    }

    private String estiloStatusItem() {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-text-fill: " + textMuted() + ";";
    }

    private String estiloStatusText() {
        return "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-text-fill: " + textMuted() + ";";
    }

    private String estiloBtnLimpiar() {
        return "-fx-background-color: transparent; -fx-text-fill: " + textMuted() + "; " +
                "-fx-font-family: 'Consolas'; -fx-font-size: 10px; " +
                "-fx-border-color: " + border() + "; -fx-border-radius: 4; " +
                "-fx-background-radius: 4; -fx-padding: 2 8 2 8; -fx-cursor: hand;";
    }

    private String estiloBotonArchivo(boolean hover) {
        if (hover) {
            return modoOscuro
                    ? "-fx-background-color: " + D_MENU_HOVER + "; " +
                    "-fx-border-color: " + D_BORDER + "; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 6 14 6 10; -fx-cursor: hand;"
                    : "-fx-background-color: " + L_MENU_HOVER + "; " +
                    "-fx-border-color: " + L_BORDER + "; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 6 14 6 10; -fx-cursor: hand;";
        } else {
            return modoOscuro
                    ? "-fx-background-color: " + D_SURFACE2 + "; " +
                    "-fx-border-color: " + D_BORDER + "; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 6 14 6 10; -fx-cursor: hand;"
                    : "-fx-background-color: " + L_SURFACE2 + "; " +
                    "-fx-border-color: " + L_BORDER + "; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6; " +
                    "-fx-padding: 6 14 6 10; -fx-cursor: hand;";
        }
    }

    private String estiloPopupMenu() {
        return "-fx-background-color: " + surface() + "; " +
                "-fx-border-color: " + border() + "; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 12, 0, 0, 4); " +
                "-fx-min-width: 200;";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BOTÓN COMPILAR
    // ════════════════════════════════════════════════════════════════════════
    private Button crearBtnCompilar() {
        Button btn = new Button("▶  Compilar");
        aplicarEstiloBtnCompilar(btn);
        return btn;
    }

    private void aplicarEstiloBtnCompilar(Button btn) {
        String base =
                "-fx-background-color: " + accent() + "; -fx-text-fill: white; " +
                        "-fx-font-family: 'Consolas'; -fx-font-size: 11px; -fx-font-weight: bold; " +
                        "-fx-padding: 6 16 6 16; -fx-background-radius: 6; -fx-cursor: hand;";
        String hover =
                "-fx-background-color: " + accentDark() + "; -fx-text-fill: white; " +
                        "-fx-font-family: 'Consolas'; -fx-font-size: 11px; -fx-font-weight: bold; " +
                        "-fx-padding: 6 16 6 16; -fx-background-radius: 6; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
    }

    private void actualizarEstiloBtnCompilar() {
        if (btn_compilar_header != null)
            aplicarEstiloBtnCompilar(btn_compilar_header);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INICIAR EDITOR
    // ════════════════════════════════════════════════════════════════════════
    private void IniciarEditor() {
        cda_error.getStyleClass().add("AreaError");
        cda_consola.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!nuevo.isEmpty() && !nuevo.isBlank()) {
                cda_consola.setStyleSpans(0, identificarPalabras(nuevo));
            }
            if (lbl_lineCount != null) {
                long lineas = nuevo.isEmpty() ? 0 : nuevo.lines().count();
                lbl_lineCount.setText("Líneas: " + lineas);
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    //  COMPILACIÓN
    // ════════════════════════════════════════════════════════════════════════
    private void ejecutarCompilacion() {
        String[] codigo_limpio = Tokenizador.limpiar(limpiar(codigoArreglo));
        if (AutomataSintax.analizar(codigo_limpio)) {
            setStatus(success(), "Sintaxis correcta");
            cda_error.replaceText("<CORE>: Sintaxis correcta!\n");
            new Thread(() -> {
                try { Thread.sleep(3000); Platform.runLater(() -> {}); }
                catch (InterruptedException e) {}
            }).start();
            AST ast = new AST();
            ast.crearAST(codigo_limpio);
            if (ast.validar(ast.getPrograma())) {
                setStatus(success(), "Código validado");
                cda_error.replaceText("<CORE>: Código validado!\n");
                //Generación de código asm
                GeneradorASM.generar(codigo_limpio);
            } else {
                setStatus(danger(), "Error semántico");
                cda_error.replaceText("");
                for (String error : PilaErrores.dump())
                    cda_error.appendText("<CORE>: " + error + "\n");
            }
        } else {
            String error = AutomataSintax.getError();
            setStatus(danger(), "Error sintáctico");
            cda_error.replaceText(error == null ? "Error Sintáctico!" : error);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ANÁLISIS LÉXICO
    // ════════════════════════════════════════════════════════════════════════
    private StyleSpans<Collection<String>> identificarPalabras(String codigo) {
        StyleSpansBuilder<Collection<String>> creadorSpans = new StyleSpansBuilder<>();
        codigoArreglo = Tokenizador.tokenizar(codigo);
        error_lexico = false;
        int tokenCount = 0;

        for (String palabra : codigoArreglo) {
            int length = palabra.length();
            if (AutomataPalabrasReservadas.analizar(palabra)) {
                tokenCount++;
                switch (AutomataPalabrasReservadas.getPalabra()) {
                    case PR04: case PR05: case PR06: case PR07: case PR08:
                        creadorSpans.add(Collections.singleton("tiposDatos"), length); break;
                    case PR12: case PR13:
                        creadorSpans.add(Collections.singleton("booleans"), length); break;
                    default:
                        creadorSpans.add(Collections.singleton("palabraReservada"), length);
                }
            } else if (AutomataCadena.analizar(palabra)) {
                tokenCount++;
                creadorSpans.add(Collections.singleton("cadena"), length);
            } else if (AutomataNumero.analizar(palabra)) {
                tokenCount++;
                creadorSpans.add(Collections.singleton("default"), length);
            } else if (AutomataID.analizar(palabra)) {
                tokenCount++;
                creadorSpans.add(Collections.singleton("identificador"), length);
            } else if (!palabra.isEmpty()
                    && (palabra.charAt(0) == ' '  || palabra.charAt(0) == '\n'
                    || palabra.charAt(0) == '\t' || palabra.charAt(0) == '{'
                    || palabra.charAt(0) == '}'  || palabra.charAt(0) == '('
                    || palabra.charAt(0) == ')'  || palabra.charAt(0) == ';'
                    || palabra.charAt(0) == '+'  || palabra.charAt(0) == '-'
                    || palabra.charAt(0) == '*'  || palabra.charAt(0) == '/'
                    || palabra.charAt(0) == '='  || palabra.equals("||")
                    || palabra.equals("&&"))) {
                creadorSpans.add(Collections.singleton("default"), length);
            } else if (palabra.charAt(0) == '%') {
                tokenCount++;
                creadorSpans.add(Collections.singleton("comentario"), length);
            } else {
                creadorSpans.add(Collections.singleton("error"), length);
                error_lexico = true;
                PilaErrores.push(palabra + " no se reconoce!");
            }
        }

        final int tc = tokenCount;
        Platform.runLater(() -> {
            if (lbl_tokenCount != null) lbl_tokenCount.setText("Tokens: " + tc);
        });

        if (error_lexico) {
            mit_compilar.setDisable(true);
            setStatus(danger(), "Error léxico detectado");
            cda_error.replaceText("");
            for (String error : PilaErrores.dump())
                cda_error.appendText("<CORE>: " + error + "\n");
        } else {
            mit_compilar.setDisable(false);
            setStatus(success(), "Todo en orden");
            cda_error.replaceText("<CORE>: Todo en orden.\n");
        }

        return creadorSpans.create();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private void setStatus(String color, String mensaje) {
        Platform.runLater(() -> {
            if (lbl_statusDot  != null)
                lbl_statusDot.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 9px;");
            if (lbl_statusText != null)
                lbl_statusText.setText(mensaje);
        });
    }

    private String[] limpiar(String[] entrada) {
        for (int i = 0; i < entrada.length; i++) {
            char c = entrada[i].charAt(0);
            if (c == '\n' || c == '\t' || entrada[i].equals(" "))
                entrada[i] = " ";
        }
        return entrada;
    }

    public static void setErrorSemantico(boolean flag) { error_semantico = flag; }
    public static void main(String[] args) { launch(); }
}


// ════════════════════════════════════════════════════════════════════════════
//  TOKENIZADOR — sin cambios
// ════════════════════════════════════════════════════════════════════════════
class Tokenizador {
    private static String[] tokens;

    public static String[] tokenizar(String texto) {
        tokens = new String[1];
        char caracter;
        String espacio = "", palabra = "", comentario = "", cadena="";
        boolean flag_cadena=false,flag_comentario = false;
        for (int i = 0; i < texto.length(); i++) {
            caracter = texto.charAt(i);
            switch (caracter) {
                case ' ': case '\n': case '\t':
                    if (flag_comentario) {
                        comentario += caracter;
                        if (i == texto.length() - 1) addToken(comentario);
                    } else if(flag_cadena){
                        cadena += caracter;
                        if (i == texto.length() - 1) addToken(cadena);
                    }else{
                        espacio += caracter;
                        if (!palabra.isEmpty()) { addToken(palabra); palabra = ""; }
                        else if (tokens[0] == null) { tokens[0] = palabra; palabra = ""; }
                    }
                    break;
                case '{': case '}': case '(': case ')':
                case ';': case '+': case '-': case '*': case '/': case '=':
                    if (flag_comentario) {
                        comentario += caracter;
                        if (i == texto.length() - 1) addToken(comentario);
                    } else if (!palabra.isEmpty()) { addToken(palabra); palabra = ""; }
                    palabra = caracter + ""; addToken(palabra); palabra = "";
                    break;
                case '%':
                    if (!palabra.isEmpty()) { addToken(palabra); palabra = ""; }
                    if (!espacio.isEmpty()) { addToken(espacio); espacio = ""; }
                    if (flag_comentario) {
                        flag_comentario = false; comentario += caracter;
                        addToken(comentario); comentario = "";
                    } else { flag_comentario = true; comentario += caracter; }
                    break;
                case '|': case '&':
                    if (flag_comentario) {
                        comentario += caracter;
                        if (i == texto.length() - 1) addToken(comentario);
                    } else {
                        if (!palabra.isEmpty() && palabra.charAt(0) != '|'
                                && palabra.charAt(0) != '&') {
                            addToken(palabra); palabra = "";
                        }
                        palabra += caracter;
                        if (!espacio.isEmpty()) { addToken(espacio); espacio = ""; }
                        else if (tokens[0] == null) { tokens[0] = espacio; espacio = ""; }
                    }
                    break;
                case '"':
                    if (!palabra.isEmpty()) { addToken(palabra); palabra = ""; }
                    if (!espacio.isEmpty()) { addToken(espacio); espacio = ""; }
                    if (flag_cadena) {
                        flag_cadena = false; cadena += caracter;
                        addToken(cadena); cadena = "";
                    } else { flag_cadena = true; cadena += caracter; }
                    break;
                default:
                    if (flag_comentario) {
                        comentario += caracter;
                        if (i == texto.length() - 1) addToken(comentario);
                    } else if(flag_cadena){
                        cadena += caracter;
                        if (i == texto.length() - 1) addToken(cadena);
                    }else{
                        if (palabra.contains("|") || palabra.contains("&")) {
                            addToken(palabra); palabra = "";
                        }
                        palabra += caracter;
                        if (!espacio.isEmpty()) { addToken(espacio); espacio = ""; }
                        else if (tokens[0] == null) { tokens[0] = espacio; espacio = ""; }
                    }
            }
            if (i == texto.length() - 1) {
                if (!espacio.isEmpty()) addToken(espacio);
                else if (!palabra.isEmpty()) addToken(palabra);
            }
        }
        String[] aux = new String[tokens.length - 1];
        for (int i = 1; i < tokens.length; i++) aux[i - 1] = tokens[i];
        tokens = aux;
        return unirOperadores(tokens);
    }

    private static void addToken(String token) {
        String[] aux = tokens == null ? new String[1] : tokens;
        tokens = new String[aux.length + 1];
        for (int i = 0; i < aux.length; i++) if (aux[i] != null) tokens[i] = aux[i];
        tokens[aux.length] = token;
    }

    public static String[] limpiar(String[] tokens) {
        int cant = 0;
        for (String t : tokens) if (!t.isBlank()) cant++;
        String[] nuevo = new String[cant];
        for (int j = 0, i = 0; i < tokens.length; i++)
            if (!tokens[i].isBlank()) { nuevo[j] = tokens[i]; j++; }
        return nuevo;
    }

    public static String[] unirOperadores(String[] tokens) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            String actual = tokens[i];
            if ((actual.equals("=") || actual.equals("!")
                    || actual.equals("<") || actual.equals(">"))
                    && i + 1 < tokens.length && tokens[i + 1].equals("=")) {
                res.add(actual + "="); i++;
            } else res.add(actual);
        }
        return res.toArray(new String[0]);
    }
}

class CorrectorLexico extends Thread { public void checar() {} }