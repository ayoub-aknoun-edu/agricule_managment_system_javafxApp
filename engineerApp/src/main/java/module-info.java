module com.app.engineerapp {
    requires javafx.controls;
    requires com.jfoenix;
    requires java.mail;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.json;

    opens com.app.engineerapp to javafx.fxml,java.base;
    exports com.app.engineerapp;

    opens com.app.engineerapp.Login.Signin to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Login.Signin;

    opens com.app.engineerapp.Dashboard.CreateAccount to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.CreateAccount;

    opens com.app.engineerapp.Dashboard.Rapports to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.Rapports;

    opens com.app.engineerapp.Dashboard.CreateZone to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.CreateZone;

    opens com.app.engineerapp.Dashboard.ModifierZone to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.ModifierZone;

    opens com.app.engineerapp.Dashboard.ModifierMyAccount to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.ModifierMyAccount;

    opens  com.app.engineerapp.Dashboard.ModifierAccounts to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.ModifierAccounts;

    opens com.app.engineerapp.Dashboard.ModifierMyAccount.ChangePassword to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.ModifierMyAccount.ChangePassword;

    opens com.app.engineerapp.Dashboard.ModifierAccounts.ChangePassword to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.ModifierAccounts.ChangePassword;

    opens com.app.engineerapp.Login.Verification to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Login.Verification;

    opens com.app.engineerapp.Login.ForgetPassword to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Login.ForgetPassword;

    opens com.app.engineerapp.Login.Verification.CodeVerufyChangePass to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Login.Verification.CodeVerufyChangePass;

    opens com.app.engineerapp.Dashboard.Home to javafx.controls,java.base,javafx.fxml,javafx.graphics;
    exports com.app.engineerapp.Dashboard.Home;


}