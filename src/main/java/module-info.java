module com.gm.student_management {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gm.student_management to javafx.fxml;
    exports com.gm.student_management;
}