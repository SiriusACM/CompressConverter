module com.sens.tools.compressconverter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.sens.tools.compressconverter to javafx.fxml;
    exports com.sens.tools.compressconverter;
}