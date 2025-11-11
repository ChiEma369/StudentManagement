module com.gm.student_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.gm.student_management to javafx.fxml;
    exports com.gm.student_management;
}