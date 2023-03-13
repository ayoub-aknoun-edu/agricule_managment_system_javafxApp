package com.app.engineerapp.Login.ForgetPassword;

import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ForgetPasswordController implements Initializable {
    @FXML
    private Pane root;
    @FXML
    private JFXButton confirmBtn;
    @FXML
    private ImageView loadingimj,loadingimj1;
    @FXML
    private Label incorrectLabel;
    @FXML
    private JFXTextField password,passwordc;
    @FXML
    private JFXDialog exitDialog;
    @FXML
    private StackPane dialogContainer;
    private boolean isPassNull = true;
    private boolean isPasscNull = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitDialog.setDialogContainer(dialogContainer);
        confirmBtn.setDisable(true);
        loadingimj.setVisible(false);
        loadingimj1.setVisible(false);
        incorrectLabel.setVisible(false);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            isPassNull = newValue.equals("");
            updateBtn();
        });
        passwordc.textProperty().addListener((observable, oldValue, newValue) -> {
            isPasscNull = newValue.equals("");
            updateBtn();
        });
    }

    private void updateBtn() {
        confirmBtn.setDisable(isPassNull || isPasscNull);
    }

    @FXML
    private void check(){
        boolean checksCorrect;
        loadingimj.setVisible(true);
        // tchecki lpassword wl confirm wach bhal bhal
        // tchecki lpassword wach fih r9oma w taille dyalo hiya hadik
        //  f kol check bdel dik incorrect : incorrectLabel.setText("")
        // l resultat d had checks dirhom fl boolean dyal checksCorrect
        //hadi ghi d test

        checksCorrect =validateCode(password.getText(),passwordc.getText());
        PauseTransition pause1 = new PauseTransition(Duration.seconds(3));
        boolean finalChecksCorrect = checksCorrect;
        pause1.setOnFinished(event -> {
            loadingimj.setVisible(false);
            if(!checksCorrect){
                incorrectLabel.setVisible(true);
                passwordc.setText("");
                password.requestFocus();
            }
        });
        pause1.play();

        // had lcondition ghi tan9ad biha chno ayw9a3 ila kano dok tcheck ghalat
        // so tatbdel lcondition b chi boolean
        // ila bantlk chi haja tbdel f scene wla f logic dont ask me ask china
        // nb : bdelha rask ze3ma
        // nb : mhi had lcmnts
        if(!checksCorrect){
            PauseTransition pause2 = new PauseTransition(Duration.seconds(5));
            pause2.setOnFinished(event ->{
                loadingimj.setVisible(false);
                passwordc.setText("");
                incorrectLabel.setVisible(false);
            });
            pause2.play();
        }else{
            loadingimj.setVisible(true);
            PauseTransition pause4 = new PauseTransition(Duration.seconds(2));
            pause4.setOnFinished(event ->{
                loadingimj.setVisible(false);
                handleChange(password.getText());
            });
            pause4.play();

        }
    }
    public  StringBuffer emailformc;
    public void getemailfromothercontroller(String email){
        emailformc=new StringBuffer(email);
    }
    public void handleChange(String pass) {
        String salt=function.salt_gen(10);
        function.setPassword("responsable",function.getcni(emailformc.toString()),function.hashing(pass+salt),salt);
        GaussianBlur blur = new GaussianBlur(10);
        root.setEffect(blur);
        loadingimj1.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event ->{
            loadingimj1.setVisible(false);
            dialogContainer.toFront();
            exitDialog.show();
            });
        pause.play();
    }

    private boolean validateCode(String pass,String passcf){
        if(pass.equals(passcf)){
            if (function.ispasswordvalide(pass)){
               incorrectLabel.setVisible(false);
                incorrectLabel.setText("");
                return true;
            }else {
                incorrectLabel.setText("Les mot de pass doit etre plus que 8 car et contain ");
                return false;
            }
        }else
        {
            incorrectLabel.setText("la configuration est inccorect ");
            return false;
        }
    }

    @FXML
    private void menuBack(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Signin/Login.fxml")));
            new WindowOManager().makeAndHideWindow(root,(Stage) confirmBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }
}
