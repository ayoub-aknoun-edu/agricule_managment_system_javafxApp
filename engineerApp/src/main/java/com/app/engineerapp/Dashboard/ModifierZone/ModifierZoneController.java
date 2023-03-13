package com.app.engineerapp.Dashboard.ModifierZone;

import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Zone;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifierZoneController implements Initializable {

    @FXML
    private AnchorPane all;

    @FXML
    private AnchorPane slider;

    @FXML
    private JFXTextField proprietaireText;

    @FXML
    private JFXTextField surfaceText;

    @FXML
    private JFXTextField adresseText;

    @FXML
    private JFXTextField produitsText;

    @FXML
    private JFXComboBox<String> responsableCombo;

    @FXML
    private Label respoLbl;

    @FXML
    private JFXComboBox<String> examinateurCombo;


    @FXML
    private JFXButton valideBtn;

    @FXML
    private JFXButton suivBtn;

    @FXML
    private JFXButton closeBtn;
    @FXML
    private JFXToggleButton isNeedExam;
    @FXML
    private ImageView loadingimj;
    protected boolean isProprietaireNull, isSurafaceNull, isAdresseNull, isProdutsNull, isExaminateurNull, isResoponsableNull;

     Zone zone;
     String cni;
    HashMap<String, String> responsablesMap;
    HashMap<String, String> examinateurMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isProprietaireNull= isSurafaceNull= isAdresseNull= isProdutsNull= isExaminateurNull= isResoponsableNull=true;
        loadingimj.setVisible(false);
        remButtons();

        produitsText.textProperty().addListener((observable, oldValue, newValue)-> {
            isProdutsNull=newValue.equals("");
            remButtons();
        });
        proprietaireText.textProperty().addListener((observable, oldValue, newValue)-> {
            isProprietaireNull=newValue.equals("");
            remButtons();
        });
        surfaceText.textProperty().addListener((observable, oldValue, newValue)-> {
            isSurafaceNull=newValue.equals("");
            remButtons();
        });
        surfaceText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (surfaceText.getLength() <= 8) {
                if (!newValue.matches("\\d*")) {
                    surfaceText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            } else {
                surfaceText.setText(newValue.substring(0, 10));
            }
        });

        adresseText.textProperty().addListener((observable, oldValue, newValue)-> {
            isAdresseNull=newValue.equals("");
            remButtons();
        });

    }

    @FXML
    void menuBack() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml")));
            root = loader.load();
            HomePageController homePageController=loader.getController();
            homePageController.getidfc(cni);
            new WindowOManager().makeAndHideWindow(root,(Stage)closeBtn.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void remButtons(){
        valideBtn.setDisable(isProprietaireNull|| isSurafaceNull|| isAdresseNull|| isProdutsNull);
        suivBtn.setDisable(isProprietaireNull|| isSurafaceNull|| isAdresseNull|| isProdutsNull);
    }

    public int modificationProcess(){
     String requette = "update zoneagricole set adresse=?,surface=?,idrespo=?,examineur=?,needexamin=?,proprietaire=?,produit=? where zoneid=?";
        try {
            PreparedStatement st = dbConnection.getConnexion().prepareStatement(requette);
            st.setString(1,adresseText.getText());
            st.setDouble(2,Double.parseDouble(surfaceText.getText()));
            st.setString(3,!Variables.type.equals("ADMIN")?zone.getIdResponsable():function.getKey(responsablesMap,responsableCombo.getValue()).replaceAll("Optional\\[","").replaceAll("\\]","") );
            st.setString(4,function.getKey(examinateurMap,examinateurCombo.getValue()).replaceAll("Optional\\[","").replaceAll("\\]",""));
            st.setInt(5,isNeedExam.isSelected()?1:0);
            st.setString(6,proprietaireText.getText());
            st.setString(7, produitsText.getText());
            st.setInt(8,zone.getZoneId());
            st.executeUpdate();
            return 1;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return 0;
    }
    @FXML
    void valide(ActionEvent event) {
        loadingimj.setVisible(true);
        Task<Integer> task= new Task<>() {
            @Override
            protected Integer call() throws Exception {
                return modificationProcess();
            }
        };
        task.setOnSucceeded(pr->{
            loadingimj.setVisible(false);
            if (task.getValue()==1)menuBack();
        });
        new Thread(task).start();

    }

    public void getObjectFromController(String cni , Zone zone){
        this.cni=cni;
        this.zone=zone;
        proprietaireText.setText(zone.getProprietaire());
        produitsText.setText(zone.getProduit());
        surfaceText.setText(""+zone.getSurface());
        adresseText.setText(zone.getAdress());
        isNeedExam.selectedProperty().setValue(zone.getNeedExamin()==1);
        boolean isAdmin= Variables.type.equals("ADMIN");
        respoLbl.setVisible(isAdmin);
        responsableCombo.setVisible(isAdmin);
        if (isAdmin){
            examinateurMap= function.getRespoNamesCni("technicien");
            responsablesMap=function.getRespoNamesCni("responsable");
            responsableCombo.getItems().addAll(responsablesMap.values());
            responsableCombo.getSelectionModel().select(responsablesMap.get(zone.getIdResponsable()));

        }else
            examinateurMap= function.getTechNamesCni(cni);
        examinateurCombo.getItems().addAll(examinateurMap.values());
        examinateurCombo.getSelectionModel().select(examinateurMap.get(zone.getIsExamineur()));

    }


}
