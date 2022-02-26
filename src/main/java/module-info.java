module org.kalbinvv.tsclient {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires TSCore;

    opens org.kalbinvv.tsclient to javafx.fxml;
    exports org.kalbinvv.tsclient;
}
