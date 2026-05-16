module org.example.lyaii {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;

    requires  com.brunomnsilva.smartgraph;

    opens org.example.lyaii to javafx.fxml;
    exports org.example.lyaii;

    exports org.example.lyaii.Graficos;
    opens org.example.lyaii.Graficos to javafx.graphics;
}