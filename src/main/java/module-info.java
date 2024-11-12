module pl.lcc.talisman {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens pl.lcc.talisman to javafx.fxml;
    exports pl.lcc.talisman;
    exports pl.lcc.talisman.controller;
    opens pl.lcc.talisman.controller to javafx.fxml;

}