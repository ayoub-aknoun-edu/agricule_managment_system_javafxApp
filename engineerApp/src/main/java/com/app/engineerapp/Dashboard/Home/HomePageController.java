package com.app.engineerapp.Dashboard.Home;

import com.app.engineerapp.Dashboard.CreateAccount.CreateAccountController;
import com.app.engineerapp.Dashboard.CreateZone.CreateZoneController;
import com.app.engineerapp.Dashboard.ModifierAccounts.ModifierAccountsController;
import com.app.engineerapp.Dashboard.ModifierMyAccount.ModifierAccountController;
import com.app.engineerapp.Dashboard.ModifierZone.ModifierZoneController;
import com.app.engineerapp.Dashboard.Rapports.RapportListController;
import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Responsable;
import com.app.engineerapp.Models.Technicien;
import com.app.engineerapp.Models.Zone;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private AnchorPane everything;

    @FXML
    private JFXButton crossBtn;

    @FXML
    private JFXButton minusBtn,acceptButton,declineButton;
    @FXML
    private JFXButton addZoneBtn;

    @FXML
    private JFXButton addIngBtn;

    @FXML
    private JFXButton addTechBtn;

    @FXML
    private StackPane dialogContainer;
    @FXML
    private JFXDialog exitDialog;
    @FXML
    private ImageView accountImg;
    @FXML
    private MenuItem IngMenuItem;
    @FXML
    private Label NameLbl;

    @FXML
    private VBox chosenZoneCard;

    @FXML
    private Label zoneId;

    @FXML
    private Label adressLbl1;

    @FXML
    private Label dataExaminLbl;
    @FXML
    private Label proprNom;
    @FXML
    private Label produitNom;

    @FXML
    private Label surfaceLbl;

    @FXML
    private JFXButton checkZone;

    private StringBuilder cnicont ;
    @FXML
    private Label contextTitle;

    @FXML
    private ImageView loading;

    @FXML
    private Label aucuneLbl;

    @FXML
    private ImageView backBtn;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private Pane chozenRespoCardContainer;

    @FXML
    private VBox chosenRespoCard;

    @FXML
    private Label ingenId;

    @FXML
    private Label nameChosen;

    @FXML
    private Label emailChosen;

    @FXML
    private Label teleChosen;

    @FXML
    private Label dateEmbChosen;

    @FXML
    private Label dataNaissChosen;

    @FXML
    private Label compteStChosen;
    @FXML
    private Label responsableTxf;

    @FXML
    private Label respoName;
    Responsable responsable;
    private ItemZoneListner zoneListner;
    private ItemRespoListner respoListner;
    private ItemTechListner techListner;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loading.setVisible(true);
        aucuneLbl.setVisible(false);
        backBtn.setVisible(false);
        addTechBtn.setVisible(false);
        addIngBtn.setVisible(false);
        chosenZoneCard.setVisible(false);
        chosenRespoCard.setVisible(false);
        exitDialog.setDialogContainer(dialogContainer);
        crossBtn.setOnMouseClicked(e-> {
            dialogContainer.toFront();
            exitDialog.show();
        });
        declineButton.setOnAction(actionEvent -> {
            dialogContainer.toBack();
            exitDialog.close();
        });
        acceptButton.setOnAction(actionEvent -> {
            exitDialog.close();
            System.exit(0);
        });
        minusBtn.setOnMouseClicked( event -> {
            Stage stage = (Stage) minusBtn.getScene().getWindow();
            stage.setIconified(true);
        });
        everything.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("styles/style.css")).toExternalForm());
    }

    public void fillListWithZones(){

      Task<List<Zone>> task=new Task<>() {
            @Override
            protected List<Zone> call()  {
                return function.getZones(responsable.getCni(),responsable.getType());
            }
        };


        task.setOnSucceeded((t)->{
            loading.setVisible(false);
            if (task.getValue().size()==0){
                aucuneLbl.setVisible(true);
            }else  fillZoneCards(task.getValue());
        });

        new Thread(task).start();
    }

    public void fillListWithResp(){
        Task<List<Responsable>> task=new Task<>() {
            @Override
            protected List<Responsable> call()  {
                return function.getResponsablesExcept(responsable.getCni());
            }
        };


        task.setOnSucceeded((t)->{
            loading.setVisible(false);
            if (task.getValue().size()==0){
                aucuneLbl.setVisible(true);
            }else  fillRespoCards(task.getValue());
        });

        new Thread(task).start();
    }
    public void fillListWithTech(){
        Task<List<Technicien>> task=new Task<>() {
            @Override
            protected List<Technicien> call()  {
                return function.getTechicienCni(responsable.getCni());
            }
        };


        task.setOnSucceeded((t)->{
            loading.setVisible(false);
            if (task.getValue().size()==0){
                aucuneLbl.setVisible(true);
            }else  fillTechCards(task.getValue());
        });

        new Thread(task).start();
    }

    public  void fillRespoCards(List<Responsable> listRespo){
        respoListner=this::setChosenRespo;
        int col=0,row=1;
        try {
            for (Responsable respo :listRespo){
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/itemRespo.fxml"));
                AnchorPane anchorPane= loader.load();
                ItemRespoController itemRespoController=loader.getController();
                itemRespoController.setData(respo,respoListner);
                if (col==3){
                    col=0;
                    row++;
                }
                grid.add(anchorPane,col++,row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                grid.setMargin(anchorPane, new Insets(10, 30, 40,30));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  void fillTechCards(List<Technicien> listTech){
        techListner=this::setChosenTech;
        int col=0,row=1;
        try {
            for (Technicien tech :listTech){
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/itemTech.fxml"));
                AnchorPane anchorPane= loader.load();
                ItemTechController itemTechController=loader.getController();
                itemTechController.setData(tech,techListner);
                if (col==3){
                    col=0;
                    row++;
                }
                grid.add(anchorPane,col++,row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                grid.setMargin(anchorPane, new Insets(10, 30, 40,30));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public  void fillZoneCards(List<Zone> listZones){
       zoneListner=this::setChosenZone;

       int col=0,row=1;
        try {
            for (Zone zone :listZones){
           FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/itemZone.fxml"));
               AnchorPane anchorPane= loader.load();
               ItemZoneController itemZoneController =loader.getController();
               itemZoneController.setData(zone,zoneListner);
               if (col==3){
                   col=0;
                   row++;
               }
               grid.add(anchorPane,col++,row);
               grid.setMinWidth(Region.USE_COMPUTED_SIZE);
               grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
               grid.setMaxWidth(Region.USE_PREF_SIZE);

               //set grid height
               grid.setMinHeight(Region.USE_COMPUTED_SIZE);
               grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
               grid.setMaxHeight(Region.USE_PREF_SIZE);
                                                       // top , right , bottom , noice
               grid.setMargin(anchorPane, new Insets(10, 30, 40,30));
            }
           } catch (IOException e) {
               throw new RuntimeException(e);
           }

    }

    public void setChosenZone(Zone zone){
        chosenZoneCard.setVisible(true);
        Variables.chosenZone=zone;
        zoneId.setText(zoneId.getText()+" "+zone.getZoneId());
        adressLbl1.setText(zone.getAdress());
        dataExaminLbl.setText(String.valueOf(zone.getLast_Examin()));
        surfaceLbl.setText(String.valueOf(zone.getSurface()));
        produitNom.setText(zone.getProduit());
        proprNom.setText(zone.getProprietaire());
    }
    public void setChosenRespo(Responsable respo){
        Variables.chosenObj=respo;
        Variables.chosenCas="ing";
        chosenRespoCard.setVisible(true);
        ingenId.setText("Ingenieur" +
                " "+respo.getCni());
        nameChosen.setText(respo.getNom()+" "+respo.getPrenom());
        emailChosen.setText(respo.getEmail());
        teleChosen.setText(respo.getTele());
        dateEmbChosen.setText(String.valueOf(respo.getDate_embau()));
        dataNaissChosen.setText(String.valueOf(respo.getDate_naiss()));
        compteStChosen.setText(respo.getIs_verified()==1?"verifier":"non verifier");
        responsableTxf.setText("");
        respoName.setText("");
    }

    public void setChosenTech(Technicien tech){
        Variables.chosenCas=Variables.type.equals("ADMIN")?"tecadmin":"tecing";
        Variables.chosenObj=tech;
        chosenRespoCard.setVisible(true);
        ingenId.setText("Technicien" +
                " "+tech.getCni());
        nameChosen.setText(tech.getNom()+" "+tech.getPrenom());
        emailChosen.setText(tech.getEmail());
        teleChosen.setText(tech.getTele());
        dateEmbChosen.setText(String.valueOf(tech.getDate_embau()));
        dataNaissChosen.setText(String.valueOf(tech.getDate_naiss()));
        compteStChosen.setText(tech.getIs_verified()==1?"verifier":"non verifier");
        if(tech.getResponsableName().equals("none")){
            responsableTxf.setVisible(false);
            respoName.setVisible(false);
        }else {
            responsableTxf.setText("Responsable:");
            respoName.setText(tech.getResponsableName());
        }

    }

    @FXML
    void Logout(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Login/Signin/Login.fxml"));
            root = loader.load();
            new WindowOManager().makeWindowTrans(root);
            crossBtn.getScene().getWindow().hide();
        } catch (IOException e) {
            System.out.println("something went wrong");
        }
    }

    @FXML
    void MonCompte(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Dashboard/ModifierMyAccount/ModifierAccount.fxml"));
            root=loader.load();
            ModifierAccountController controller=loader.getController();
            controller.getObjectFromDashboard(responsable.getCni(),responsable);
            new WindowOManager().makeAndHideWindow(root,(Stage)crossBtn.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getidfc(String cni){
        cnicont = new StringBuilder(cni);
        responsable = function.responsableObjet(cni);
        NameLbl.setText(responsable.getNom()+" "+responsable.getPrenom());
        if (!Variables.type.equals("ADMIN")) IngMenuItem.setVisible(false);
        fillListWithZones();
    }

    @FXML
    void IngenierM(ActionEvent event) {
        aucuneLbl.setVisible(false);
        loading.setVisible(true);
        addTechBtn.setVisible(false);
        addIngBtn.setVisible(true);
        addZoneBtn.setVisible(false);
        chosenZoneCard.setVisible(false);
        chosenRespoCard.setVisible(false);
        chozenRespoCardContainer.toFront();
        chosenRespoCard.toFront();
        contextTitle.setText("Ingenieurs");
        grid.getChildren().clear();
        fillListWithResp();
    }

    @FXML
    void ZonesM(ActionEvent event) {
        aucuneLbl.setVisible(false);
        loading.setVisible(true);
        addTechBtn.setVisible(false);
        addIngBtn.setVisible(false);
        addZoneBtn.setVisible(true);
        chosenZoneCard.setVisible(false);
        chosenRespoCard.setVisible(false);
        contextTitle.setText("Zones");
        grid.getChildren().clear();
        fillListWithZones();
    }

    @FXML
    void techniciensM(ActionEvent event) {
        aucuneLbl.setVisible(false);
        loading.setVisible(true);
        addTechBtn.setVisible(true);
        addIngBtn.setVisible(false);
        addZoneBtn.setVisible(false);
        chosenZoneCard.setVisible(false);
        chosenRespoCard.setVisible(false);
        chozenRespoCardContainer.toFront();
        chosenRespoCard.toFront();
        contextTitle.setText("Techniciens");
        grid.getChildren().clear();
        fillListWithTech();
    }

    @FXML
    void addIngAction(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource("Dashboard/CreateAccount/CreateAccount.fxml")));
            root= loader.load();
            CreateAccountController createAccountController=loader.getController();
            createAccountController.getRespoFromOtherCntrl(responsable,"ing");
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }

    }

    @FXML
    void addTechAction(ActionEvent event) {
        String cas = Variables.type.equals("ADMIN")?"tecadmin":"tecing";
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource("Dashboard/CreateAccount/CreateAccount.fxml")));
            root= loader.load();
            CreateAccountController createAccountController=loader.getController();
            createAccountController.getRespoFromOtherCntrl(responsable,cas);
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }


    }

    @FXML
    void addZoneAction(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource("Dashboard/CreateZone/createZone.fxml")));
            root= loader.load();
            CreateZoneController createZoneController=loader.getController();
            createZoneController.passeur(cnicont.toString());
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }

    }

    @FXML
    void modifierAccountAction(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Dashboard/ModifierAccounts/modifierAccounts.fxml"));
            root=loader.load();
            ModifierAccountsController modifierAccountsController=loader.getController();
            modifierAccountsController.getAccounttoModifier(cnicont.toString(),Variables.chosenCas,Variables.chosenObj);
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void showRapports(){
        Parent root;
        try {
            FXMLLoader loader =new FXMLLoader(HelloApplication.class.getResource("Dashboard/Rapports/RapportList.fxml"));
             root=loader.load();
             RapportListController rapportListController=loader.getController();

            rapportListController.getidfc(cnicont.toString(),String.valueOf(Variables.chosenZone.getZoneId()));
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (Exception e) {
            throw new RuntimeException(e);

    }
}

    @FXML
    void ModifierZone(ActionEvent event) {
        Parent root ;
        try {
            FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Dashboard/ModifierZone/ModifierZone.fxml"));
            root=loader.load();
            ModifierZoneController modifierZoneController=loader.getController();
            modifierZoneController.getObjectFromController(cnicont.toString(),Variables.chosenZone);
            new WindowOManager().makeAndHideWindow(root,(Stage) minusBtn.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
