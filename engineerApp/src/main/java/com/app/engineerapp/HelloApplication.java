package com.app.engineerapp;

import com.app.engineerapp.WindowManager.WindowOManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Signin/Login.fxml")));
       // Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Verification/codeVerify.fxml")));
        //Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/SignUp/CreateAccount.fxml")));
        new WindowOManager().makeWindowTrans(root);
    }
    public static void main(String[] args) {
        launch();
    }
}