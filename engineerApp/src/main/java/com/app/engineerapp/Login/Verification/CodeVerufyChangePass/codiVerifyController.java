package com.app.engineerapp.Login.Verification.CodeVerufyChangePass;


import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Login.ForgetPassword.ForgetPasswordController;
import com.app.engineerapp.Login.Verification.Format;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class codiVerifyController implements Initializable {
     @FXML
     private Pane root;
     @FXML
     private JFXButton verifyBtn;
     @FXML
     private ImageView loadingimj,loadingimj1;
     @FXML
     private Label incorrectLabel;
     @FXML
     private TextField code1,code2,code3,code4,code5,code6;
     private boolean is1null,is2null,is3null,is4null,is5null,is6null;


     @Override
     public void initialize(URL url, ResourceBundle resourceBundle) {
         is1null = is2null = is3null = is4null = is5null = is6null = true;
         loadingimj.setVisible(false);
         loadingimj1.setVisible(false);
         incorrectLabel.setVisible(false);
         verifyBtn.setDisable(true);
         ///////////////////////////////////////////
         code1.textProperty().addListener((observable, oldValue, newValue) -> {
             is1null = newValue.equals("");updateBtn();
             if (!is1null)
                 code2.requestFocus();
         });
         code2.textProperty().addListener((observable, oldValue,newValue) -> {
             is2null = newValue.equals("");updateBtn();
             if (!is2null)
                 code3.requestFocus();
         });
         code3.textProperty().addListener((observable, oldValue,newValue) -> {
             is3null = newValue.equals("");updateBtn();
             if (!is3null)
                 code4.requestFocus();
         });
         code4.textProperty().addListener((observable, oldValue,newValue) -> {
             is4null = newValue.equals("");updateBtn();
             if (!is4null)
                 code5.requestFocus();
         });
         code5.textProperty().addListener((observable, oldValue,newValue) -> {
             is5null = newValue.equals("");updateBtn();
             if (!is5null)
                 code6.requestFocus();
         });
         code6.textProperty().addListener((observable, oldValue,newValue) -> {
             is6null = newValue.equals("");updateBtn();
         });
         ///////////////////////////////////////////
         code1.setTextFormatter(new Format(code1).format());
         code2.setTextFormatter(new Format(code2).format());
         code3.setTextFormatter(new Format(code3).format());
         code4.setTextFormatter(new Format(code4).format());
         code5.setTextFormatter(new Format(code5).format());
         code6.setTextFormatter(new Format(code6).format());
     }

     private void updateBtn() {
         verifyBtn.setDisable(is1null || is2null || is3null || is4null || is5null || is6null);
     }


    @FXML
    private void checkCode() {
        int userCode = Integer.parseInt(code1.getText() + code2.getText() +
                code3.getText() + code4.getText() +
                code5.getText() + code6.getText());
        loadingimj.setVisible(true);
        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                String cni=function.getcni(emailformc.toString());
                return function.checkverfcode(cni, String.valueOf(userCode));
            }
        };
        task.setOnSucceeded(e->{
            loadingimj.setVisible(false);
            if (task.getValue())
               passwordChanger();
            else{

                incorrectLabel.setVisible(true);
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event ->
                        incorrectLabel.setVisible(false));
                pause.play();
                code1.setText("");
                code2.setText("");
                code3.setText("");
                code4.setText("");
                code5.setText("");
                code6.setText("");
            }
        });
        new Thread(task).start();
    }
     public  StringBuffer emailformc;
     public void getemailfromothercontroller(String email){
         emailformc=new StringBuffer(email);
     }

    private void passwordChanger(){
         Parent root;
         try {
             FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Login/ForgetPassword/forgetPassword.fxml"));
             root=loader.load();
             ForgetPasswordController ps=loader.getController();
             ps.getemailfromothercontroller(emailformc.toString());
             new WindowOManager().makeAndHideWindow(root,(Stage) verifyBtn.getScene().getWindow());
         } catch (IOException e) {
             System.out.println("something went wrong");
             e.printStackTrace();
         }
     }
    @FXML
    private void menuBack(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Signin/Login.fxml")));
            new WindowOManager().makeAndHideWindow(root,(Stage) verifyBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }
}
