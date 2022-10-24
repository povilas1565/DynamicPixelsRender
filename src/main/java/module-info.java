module com.example.dynamicpixelsrender {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.dynamicpixelsrender to javafx.fxml;
    exports com.example.dynamicpixelsrender;
}