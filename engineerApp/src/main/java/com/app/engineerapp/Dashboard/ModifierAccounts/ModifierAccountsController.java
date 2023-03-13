package com.app.engineerapp.Dashboard.ModifierAccounts;

import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.Dashboard.ModifierAccounts.ChangePassword.PasswordChangeController;
import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Genre;
import com.app.engineerapp.Models.Personne;
import com.app.engineerapp.Models.Responsable;
import com.app.engineerapp.Models.Technicien;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
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
import java.util.HashMap;
import java.util.ResourceBundle;
@SuppressWarnings({"Duplicates","unused"})
public class ModifierAccountsController implements Initializable {

    @FXML
    private Label incorrectLabel;
    @FXML
    private  Label respoLbl;
    @FXML
    private JFXButton createAccount;

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
    private ImageView loadingimj;
    @FXML
    private JFXTextField inpEmail;

    @FXML
    private Label forgetPassword;
    @FXML
    private JFXComboBox<String> responsableName;

    //boolean to verify if field are empty
    private boolean isGenreNull,isCNINull,isNameNull,isPenomNull,isTeleNull,isEmailNull;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isGenreNull=isCNINull=isNameNull=isPenomNull=isTeleNull=isEmailNull=true;
        incorrectLabel.setVisible(false);
        createAccount.setDisable(true);
        loadingimj.setVisible(false);
        createAccount.setOnMouseClicked(e-> creationProcess());

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
        //if any field disable the createAccount button

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
        createAccount.setDisable(  isGenreNull || isCNINull || isNameNull||isPenomNull||isTeleNull||isEmailNull);
    }
    static String genvercod;

    @FXML
    private int modifierAccountHandler(String accountToVerf){
        boolean isemailchaged=false;
        if (!inpEmail.getText().toLowerCase().equals(function.getEmailByCni(((Personne)account).getCni()).toLowerCase())){
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

        String requete1="update person set cni=?,nom=?,prenom=?,date_naiss=?,date_embau=?,genre=?,tele=?,email=? where cni=? ";
        String requete2="update technicien set respoId=? where cni=?";
        String requete3="update "+table+" set is_verified= 0";
        try {
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
            pstat.setString(9,((Personne)account).getCni());
            pstat.executeUpdate();
            if (isemailchaged) connection.createStatement().executeUpdate(requete1);
            if (Variables.chosenCas.equals("tecadmin")){
                PreparedStatement pstat2=connection.prepareStatement(requete3);
                pstat2.setString(1, function.getKey(hashMap,responsableName.getValue()).replaceAll("Optional\\[","").replaceAll("\\]",""));
                pstat2.setString(2,((Personne)account).getCni());
                pstat2.executeUpdate();
            }
            connection.commit();
            connection.close();
            return 0;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

        
    }


    public void creationProcess(){
        loadingimj.setVisible(true);
        Task<Integer> task =new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return modifierAccountHandler("test");
            }
        };
        task.setOnSucceeded(e->{
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
            }

        });
        new Thread(task).start();
    }

    String table;
    HashMap<String,String> hashMap;
    String cni;
    String cas;
    Object account;
    public void getAccounttoModifier(String cni ,String cas, Object object ){
        account=object;
        this.cni=cni;
        this.cas=cas;
       if (object instanceof Technicien) {
           Technicien acc = (Technicien) object;
           setData(acc,cas);
       }else {
           Responsable acc = (Responsable) object;
           setData(acc,cas);
       }
    }
public void setData(Personne personne,String cas){// cas = "tecadmin"=> admin add a tec ,"ing"=> admin add a ing,"tecing"=> ing add a tec
    inpCNI.setText(personne.getCni());
    inpName.setText(personne.getNom());
    inpPrenom.setText(personne.getPrenom());
    inpTele.setText(personne.getTele());
    datePicker1.setValue(LocalDate.parse(personne.getDate_naiss().toString()));
    datePicker2.setValue(LocalDate.parse(personne.getDate_embau().toString()));
    if (personne.getGenre()== Genre.Homme)chk1.setSelected(true); else chk2.setSelected(true);
    inpEmail.setText(personne.getEmail());
    boolean isCmbbxDisp=Variables.type.equals("ADMIN") && cas.equals("tecadmin") ;
    table= cas.equals("ing")? "responsable":"technicien";
    responsableName.setVisible(isCmbbxDisp);
    respoLbl.setVisible(isCmbbxDisp);
    if(isCmbbxDisp){
        hashMap=function.getRespoNamesCni("responsable");
        responsableName.getItems().addAll(hashMap.values());
    }
}
    @FXML
    void forgetPasswordEvent(MouseEvent event) {
        Parent root ;
        try {
            FXMLLoader loader= new FXMLLoader(HelloApplication.class.getResource("Dashboard/ModifierAccounts/ChangePassword/passwordChange.fxml"));
            root=loader.load();
            PasswordChangeController passwordChangeController=loader.getController();
            passwordChangeController.getemailfromothercontroller(cni,account);
            new WindowOManager().makeAndHideWindow(root,(Stage) createAccount.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void menuBack(){
        Parent root;
        try {
            FXMLLoader loader=new  FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml"));
            root=loader.load();
            HomePageController controller=loader.getController();
            controller.getidfc(cni);
            new WindowOManager().makeAndHideWindow(root,(Stage) createAccount.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }


}
