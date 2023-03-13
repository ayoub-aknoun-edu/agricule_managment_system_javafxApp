package com.app.engineerapp.Dashboard.CreateAccount;


import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.FunctionUtil.JavaMailUtil;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;

import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Responsable;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.*;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

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
    private JFXTextField inpPassword;
    @FXML
    private JFXTextField inpPassver;
    @FXML
    private JFXComboBox<String> responsableName;

    //boolean to verify if field are empty
    private boolean isGenreNull,isCNINull,isNameNull,isPenomNull,isTeleNull,isEmailNull,isPasswordNull,isPasswordverNull;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isGenreNull=isCNINull=isNameNull=isPenomNull=isTeleNull=isEmailNull=isPasswordNull=isPasswordverNull=true;
       incorrectLabel.setVisible(false);
        createAccount.setDisable(true);
        loadingimj.setVisible(false);
        createAccount.setOnMouseClicked(e-> {
          creationProcess();
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
        inpPassword.textProperty().addListener((observable, oldValue, newValue)-> {
            isPasswordNull=newValue.equals("");
            incorrectLabel.setVisible(false);
            updatecreateBtn();
        });
        inpPassver.textProperty().addListener((observable, oldValue, newValue)-> {
            isPasswordverNull=newValue.equals("");
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
        createAccount.setDisable(  isGenreNull || isCNINull || isNameNull||isPenomNull||isTeleNull||isEmailNull||isPasswordNull||isPasswordverNull);
    }
   static String genvercod;

    @FXML
    private int createAccountHandler(){
        if (inpPassword.getText().equals(inpPassver.getText())){
        if(function.isemailvalid(inpEmail.getText())){
            if (!function.ifemailexist(inpEmail.getText())){
                if (function.ispasswordvalide(inpPassword.getText())){
                    String cni=inpCNI.getText();
                    boolean isResponsable=this.table.equals("responsable");
                    String requette=isResponsable
                            ? "insert into responsable(password,salt,is_verified,cni,is_locked,type,verification_code)values(?,?,?,?,?,?,?)":
                            "insert into technicien(password,salt,is_verified,cni,is_locked,respoid)values(?,?,?,?,?,?) ";
                    String requtte2= "insert into person(cni,nom,prenom,tele,date_embau,date_naiss,genre,email)values(?,?,?,?,?,?,?,?)";
                    try {
                        Connection connection =dbConnection.getConnexion();
                        connection.setAutoCommit(false);
                        PreparedStatement st= connection.prepareStatement(requette);
                        PreparedStatement st2=connection.prepareStatement(requtte2);
                       // st.setString(1,inpEmail.getText());
                        String salt=function.salt_gen(10);
                        genvercod=function.veriificationCodegen();
                        st.setString(1,function.hashing(inpPassword.getText()+salt));
                        st.setString(2,salt);
                        st.setInt(3,0);
                        st.setString(4,cni);
                        st.setInt(5,0);
                        if(isResponsable){
                            st.setString(6,"INGENIEUR");
                            st.setString(7,genvercod);
                        }else {
                            String respoName=Variables.type.equals("ADMIN")? function.getKey(hashMap,responsableName.getValue()).replaceAll("Optional\\[","").replaceAll("\\]","") :responsable.getCni();
                            System.out.println(respoName);
                            st.setString(6,respoName);
                        }
                        st2.setString(1,cni);
                        st2.setString(2,inpName.getText());
                        st2.setString(3,inpPrenom.getText());
                        st2.setString(4,inpTele.getText());
                        st2.setDate(5,Date.valueOf(datePicker2.getValue()));
                        st2.setDate(6, Date.valueOf(datePicker1.getValue()));
                        String genre = chk1.isSelected() ? "Homme" : "Femme";
                        st2.setString(7,genre);
                        st2.setString(8,inpEmail.getText());
                        st2.executeUpdate();
                        st.executeUpdate();
                        connection.commit();
                        if(isResponsable)JavaMailUtil.sendmail(inpEmail.getText(),genvercod);
                        return 1;//connection sucess

                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    return 2;
                }
            }else {
                return 3;

            }
        }else {
            return 4;

        }}else {
            return 5;
        }
        return 0;
    }


    public void creationProcess(){
        loadingimj.setVisible(true);
        Task<Integer> task =new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return createAccountHandler();
            }
        };
        task.setOnSucceeded(e->{
            loadingimj.setVisible(false);
             switch (task.getValue())
             {
                 case 1:
                     menuBack();
                     break;
                 case 2:
                     incorrectLabel.setText("le mot de pass est faible");
                     incorrectLabel.setVisible(true);
                     break;
                 case 3:
                     incorrectLabel.setText("email est deja utilise");
                     incorrectLabel.setVisible(true);
                     break;
                 case 4:
                     incorrectLabel.setText("email invalide");
                     incorrectLabel.setVisible(true);
                     break;
                 case 5:
                     incorrectLabel.setText("confirmation code incorrect");
                     incorrectLabel.setVisible(true);
                     break;
             }

        });
        new Thread(task).start();
    }

    Responsable responsable;
    String table;

    HashMap<String,String> hashMap;
    public void getRespoFromOtherCntrl(Responsable responsable,String cas){// cas = "tecadmin"=> admin add a tec ,"ing"=> admin add a ing,"tecing"=> ing add a tec
        this.responsable=responsable;
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
    private void menuBack(){
        Parent root;
        try {
            FXMLLoader loader=new  FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml"));
            root=loader.load();
            HomePageController controller=loader.getController();
            controller.getidfc(responsable.getCni());
            new WindowOManager().makeAndHideWindow(root,(Stage) createAccount.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }

}
