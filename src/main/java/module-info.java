module pl.edu.pw.ee.egzamin_app_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens pl.edu.pw.ee.egzamin_app_project to javafx.fxml, com.fasterxml.jackson.databind;
    exports pl.edu.pw.ee.egzamin_app_project;
}