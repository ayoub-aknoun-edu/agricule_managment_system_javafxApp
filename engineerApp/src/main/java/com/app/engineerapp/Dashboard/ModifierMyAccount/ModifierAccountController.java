package com.app.engineerapp.Dashboard.ModifierMyAccount;

import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.Dashboard.ModifierMyAccount.ChangePassword.passwordChangeController;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Genre;
import com.app.engineerapp.Models.Personne;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifierAccountController implements Initializable {

    @FXML
    private Label incorrectLabel;
    @FXML
    private JFXButton modifAccount;
    @FXML
    private DatePicker datePicker1,datePicker2;
    @FXML
    private JFXTextField inpCNI;
    @FXML
    private JFXTextField inpName;
    @FXML
    private JFXTextField inpPrenom;
    @FXML
    private JFXTextField inpTele;
    @FXML
    private JFXCheckBox chk1,chk2;
    @FXML
    private JFXTextField inpEmail;
    @FXML
    private Label forgetPassword;
    @FXML
    private ImageView loadingimj;


    //boolean to verify if field are empty
    private boolean isGenreNull,isCNINull,isNameNull,isPenomNull,isTeleNull,isEmailNull;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isGenreNull=isCNINull=isNameNull=isPenomNull=isTeleNull=isEmailNull=true;
        incorrectLabel.setVisible(false);
        loadingimj.setVisible(false);
        modifAccount.setDisable(true);
        modifAccount.setOnMouseClicked(e-> {
            modifAccountHandler();
        });


        //pour accepte suelement les nombre
        inpTele.textProperty().addListener((observable, oldValue, newValue) -> {
            if (inpTele.getLength() <= 10) {
                if (!newValue.matches("\\d*")) {
                    inpTele.setText(newValue.replaceAll("[^\\d]", ""));
                }
            } else {
                inpTele.setText(newValue.substring(0, 10));
            }
        });
        //if any field disable the modifAccount button

        inpCNI.textProperty().addListener((observable, oldValue, newValue)-> {
            isCNINull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });
        inpName.textProperty().addListener((observable, oldValue, newValue)-> {
            isNameNull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });
        inpPrenom.textProperty().addListener((observable, oldValue, newValue)-> {
            isPenomNull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });
        inpTele.textProperty().addListener((observable, oldValue, newValue)-> {
            isTeleNull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });

        inpEmail.textProperty().addListener((observable, oldValue, newValue)-> {
            isEmailNull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });

        chk1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                chk2.setSelected(!newValue);
                isGenreNull = !chk1.isSelected() && !chk2.isSelected();
                updatecreateBtn();
            }
        });

        chk2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                chk1.setSelected(!newValue);
                isGenreNull = !chk1.isSelected() && !chk2.isSelected();
                updatecreateBtn();
            }
        });
    }

    private void updatecreateBtn() {
        modifAccount.setDisable(  isGenreNull || isCNINull || isNameNull||isPenomNull||isTeleNull||isEmailNull);
    }
    static String genvercod;

    private int modifAccountHandler(){
        boolean isemailchaged=false;
        if (!inpEmail.getText().toLowerCase().equals(function.getEmailByCni(returnPerson.getCni()).toLowerCase())){
            if(function.isemailvalid(inpEmail.getText())){
                if (!function.ifemailexist(inpEmail.getText())){
                    isemailchaged=true;
                }else {
                    return 1;//email deja utilisée
                }
            }else{
                return 2;//email invalid
            }
        }
        String requete2="update responsable set is_verified= 0";
        String requete1="update person set cni=?,nom=?,prenom=?,date_naiss=?,date_embau=?,genre=?,tele=?,email=? where cni=?";

        try {
            int returnValue=0;
            Connection connection =dbConnection.getConnexion();
            connection.setAutoCommit(false);
            PreparedStatement pstat= connection.prepareStatement(requete1);
            pstat.setString(1,inpCNI.getText());
            pstat.setString(2,inpName.getText());
            pstat.setString(3,inpPrenom.getText());
            pstat.setDate(4,Date.valueOf(datePicker1.getValue()));
            pstat.setDate(5,Date.valueOf(datePicker2.getValue()));
            pstat.setString(6,chk1.isSelected() ? "Homme" : "Femme");
            pstat.setString(7,inpTele.getText());
            pstat.setString(8,inpEmail.getText());
            pstat.setString(9,returnPerson.getCni());
            pstat.executeUpdate();
            if (isemailchaged) {connection.createStatement().executeUpdate(requete2); returnValue=3;}
            connection.commit();
            connection.close();
            return returnValue;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
     public void modifierAccountEvent(){
        loadingimj.setVisible(true);
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return Integer.parseInt(String.valueOf(modifAccountHandler()));
            }
        };
        task.setOnSucceeded((e)->{
            loadingimj.setVisible(false);
            PauseTransition pause;
            switch (task.getValue()){
                case 1:
                        incorrectLabel.setText("email est deja utilise");
                 //       closeBtn.setFocusTraversable(false);
                        incorrectLabel.setVisible(true);
                        pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(event -> incorrectLabel.setVisible(false));
                        pause.play();
                        break;
                    case 2:
                        incorrectLabel.setText("email invalide");
                        //   closeBtn.setFocusTraversable(false);
                        incorrectLabel.setVisible(true);
                        pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(event -> incorrectLabel.setVisible(false));
                        pause.play();
                    break;
                    case 0:
                        menuBack();
                    break;
                    case 3:
                        toLogin();
                    break;
            }
        });
        new Thread(task).start();
    }

    Personne returnPerson;
    String returncni;
public void getObjectFromDashboard( String cni,Personne personne){
        inpCNI.setText(personne.getCni());
        inpEmail.setText(personne.getEmail());
        inpName.setText(personne.getNom());
        inpPrenom.setText(personne.getPrenom());
        inpTele.setText(personne.getTele());
        datePicker1.setValue(LocalDate.parse(personne.getDate_naiss().toString()));
        datePicker2.setValue(LocalDate.parse(personne.getDate_embau().toString()));
        if (personne.getGenre()== Genre.Homme)chk1.setSelected(true); else chk2.setSelected(true);
        returnPerson=personne;
        returncni=cni;
}
    @FXML
    private void menuBack(){
        Parent root;
        try {
            FXMLLoader loader=new  FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml"));
            root=loader.load();
            HomePageController controller=loader.getController();
            controller.getidfc(returncni);
            new WindowOManager().makeAndHideWindow(root,(Stage) modifAccount.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }
    private void toLogin(){
        Parent root;
        try {
            FXMLLoader loader=new  FXMLLoader(HelloApplication.class.getResource("Login/Signin/Login.fxml"));
            root=loader.load();
            new WindowOManager().makeAndHideWindow(root,(Stage) modifAccount.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }
    @FXML
    void forgetPasswordEvent(MouseEvent event) {
    Parent root;
        try {
            FXMLLoader loader= new FXMLLoader(HelloApplication.class.getResource("Dashboard/ModifierMyAccount/ChangePassword/passwordChange.fxml"));
            root=loader.load();
            passwordChangeController controller=loader.getController();
            controller.getemailfromothercontroller(returncni,returnPerson);
            new WindowOManager().makeAndHideWindow(root,(Stage) modifAccount.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
