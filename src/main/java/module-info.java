module pl.edu.pw.ee.egzamin_app_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens pl.edu.pw.ee.egzamin_app_project to javafx.fxml;
    exports pl.edu.pw.ee.egzamin_app_project;
}