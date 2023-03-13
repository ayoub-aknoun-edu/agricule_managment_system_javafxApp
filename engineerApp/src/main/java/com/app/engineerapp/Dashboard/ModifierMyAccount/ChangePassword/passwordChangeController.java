package com.app.engineerapp.Dashboard.ModifierMyAccount.ChangePassword;

import com.app.engineerapp.Dashboard.ModifierMyAccount.ModifierAccountController;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Personne;
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

public class passwordChangeController implements Initializable {

    @FXML
    private JFXButton confirmBtn;
    @FXML
    private ImageView loadingimj;
    @FXML
    private Label incorrectLabel;
    @FXML
    private JFXTextField password,passwordc;

    private boolean isPassNull = true, isPasscNull = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmBtn.setDisable(true);
        loadingimj.setVisible(false);
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
        loadingimj.setVisible(true);
        Task<Integer> task=new Task<Integer>(){
            @Override
            protected Integer call() throws Exception {
                return Integer.parseInt(String.valueOf(validateCode(password.getText(),passwordc.getText())));
            }
        };
        task.setOnSucceeded((e)->{
            loadingimj.setVisible(false);
            PauseTransition pause;
            switch (task.getValue()){
                case 2:
                    incorrectLabel.setText(" Les mot de pass doit etre plus que 8 car et contain");
                    //   closeBtn.setFocusTraversable(false);
                    incorrectLabel.setVisible(true);
                    pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> incorrectLabel.setVisible(false));
                    pause.play();
                    break;
                case 3:
                    incorrectLabel.setText(" la configuration est inccorect");
                    //   closeBtn.setFocusTraversable(false);
                    incorrectLabel.setVisible(true);
                    pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> incorrectLabel.setVisible(false));
                    pause.play();
                    break;
                case 4:
                    menuBack();
                    break;
            }
        });
        new Thread(task).start();
    }

    Personne returnPerson;
    String returCni;
    public void getemailfromothercontroller(String cni, Personne personne){
        returCni=cni;
        returnPerson=personne;
    }
    public void handleChange(String pass) {
        String salt=function.salt_gen(10);
        function.setPassword("responsable",returnPerson.getCni(),function.hashing(pass+salt),salt);
    }

    private int validateCode(String pass,String passcf){
        if(pass.equals(passcf)){
            if (function.ispasswordvalide(pass)){
                handleChange(password.getText());
                System.out.println("ret 4");
                return 4;//tous pass bien
            }else {
                System.out.println("ret 2");
                return 2;//Les mot de pass doit etre plus que 8 car et contain
            }
        }else
        {
            System.out.println("ret 3");
            return 3;//la configuration est inccorect
        }
    }

    @FXML
    private void menuBack(){
        Parent root;
        try {
            FXMLLoader loader=new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource("Dashboard/ModifierMyAccount/ModifierAccount.fxml")));
            root=loader.load();
            ModifierAccountController controller=loader.getController();
            controller.getObjectFromDashboard(returCni,returnPerson);
            new WindowOManager().makeAndHideWindow(root,(Stage) confirmBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }
}
