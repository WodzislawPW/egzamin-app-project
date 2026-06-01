module pl.edu.pw.ee.egzamin_app_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.poi.ooxml;
    requires org.docx4j.core;
    requires jakarta.xml.bind;


    opens pl.edu.pw.ee.egzamin_app_project to javafx.fxml, com.fasterxml.jackson.databind, jakarta.xml.bind;
    exports pl.edu.pw.ee.egzamin_app_project;
}