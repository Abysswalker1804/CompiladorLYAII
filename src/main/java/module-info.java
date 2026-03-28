module org.example.lyaii {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;

    opens org.example.lyaii to javafx.fxml;
    exports org.example.lyaii;
}