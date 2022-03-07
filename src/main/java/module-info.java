module org.kalbinvv.tsclient {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires transitive TsCore;

    opens org.kalbinvv.tsclient to javafx.fxml;
    opens org.kalbinvv.tsclient.controllers to javafx.fxml;
    exports org.kalbinvv.tsclient;
}
