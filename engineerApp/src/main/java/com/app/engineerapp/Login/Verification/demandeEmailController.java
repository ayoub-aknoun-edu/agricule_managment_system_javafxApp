package com.app.engineerapp.Login.Verification;

import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Login.Verification.CodeVerufyChangePass.codiVerifyController;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class demandeEmailController implements Initializable {

    @FXML
    private ImageView loadingimj;
    @FXML
    private Label incorrectLabel;
    @FXML
    private JFXTextField inpEmail;
    @FXML
    private JFXButton VerifierBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VerifierBtn.setVisible(true);
        loadingimj.setVisible(false);
        incorrectLabel.setVisible(false);
    }

    @FXML
    private void tryConnect() {
        loadingimj.setVisible(true);
        inpEmail.setDisable(true);
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return Integer.parseInt(String.valueOf(contenue(inpEmail.getText())));
            }
        };
        task.setOnSucceeded((e)->{
            loadingimj.setVisible(false);
            PauseTransition pause;
            if (task.getValue()==1){
                incorrectLabel.setText("email n'exist pas ");
                incorrectLabel.setVisible(true);
                pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event ->{
                    incorrectLabel.setVisible(false);
                    inpEmail.setDisable(false);
                });
                pause.play();
            }else if (task.getValue()==2){
                toVerifie(inpEmail.getText());
            }
        });
        new Thread(task).start();

    }


    private void toVerifie(String email){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Login/Verification/CodeVerufyChangePass/codeVerify.fxml"));
            root=loader.load();
            codiVerifyController passemail=loader.getController();
            passemail.getemailfromothercontroller(email);
            new WindowOManager().makeAndHideWindow(root,(Stage) VerifierBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("some thing went wrong");
            e.printStackTrace();
        }
    }

    private int contenue(String email){
        if(function.ifemailexist(email)){
            function.forgotpassword(email);
            return 2;//tous va bien

        }else{
            return 1;//email not found
        }
    }

    @FXML
    private void menuBack(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Signin/Login.fxml")));
            new WindowOManager().makeAndHideWindow(root,(Stage) VerifierBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("some thing went wrong");
            e.printStackTrace();
        }
    }
}
