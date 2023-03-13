package com.app.engineerapp.Dashboard.Rapports;

import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Rapport;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class RapportListController implements Initializable {
    @FXML
    private ImageView closeImg;

    @FXML
    private Pane all,everything;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private GridPane grid;
    private ListenerDelete listenerDelete;
    private ListenerOpen listenerOpen;

    private List<Rapport> rapports = new ArrayList<>();

    private String cni;
    private String zoneId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void fillWithRapports(String zoneId){

      rapports.clear();
      grid.getChildren().clear();
      rapports.addAll(function.getRappportsZone(zoneId));
      if (rapports.size() > 0) {
          listenerDelete = this::delete;
          listenerOpen = this::openRapport;
      }
      //Collections.sort(rapports);
      int row = 1;
      try {
          for (Rapport rapp : rapports) {
              FXMLLoader fxmlLoader = new FXMLLoader();
              fxmlLoader.setLocation(HelloApplication.class.getResource("Dashboard/Rapports/Rapport.fxml"));
              AnchorPane anchorPane = fxmlLoader.load();
              RapportController rapportController = fxmlLoader.getController();
              rapportController.setData(rapp, listenerDelete,listenerOpen);
              row++;
              grid.add(anchorPane, 0, row); //(child,column,row)
              //set grid width
              grid.setMinWidth(Region.USE_COMPUTED_SIZE);
              grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
              grid.setMaxWidth(Region.USE_PREF_SIZE);

              //set grid height
              grid.setMinHeight(Region.USE_COMPUTED_SIZE);
              grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
              grid.setMaxHeight(Region.USE_PREF_SIZE);

              GridPane.setMargin(anchorPane, new Insets(10));
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

    private void delete(Rapport rapport){
        function.deleteRapport(rapport.getIdZone(),rapport.getGenDate());
        fillWithRapports(zoneId);
    }

    private void openRapport(Rapport rapport){
        FileChooser fileChooser=new FileChooser();
        Window stage= closeBtn.getScene().getWindow();
        fileChooser.setTitle("enregistrer le rapport ");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF FILE","*.pdf"));
        fileChooser.setInitialFileName("rapport"+rapport.getIdZone()+(rapport.getGenDate().toString().replaceAll("-","")));
            try{
                  File file = fileChooser.showSaveDialog(stage);
                  System.out.println(file.getAbsolutePath());
                  FileOutputStream fos = new FileOutputStream(file);
                  byte[] data=function.getRapportFile(rapport.getIdZone(),rapport.getGenDate());
                  System.out.println(data.length);
                  fos.write(data, 0, data.length);
                  fos.close();
              }catch(Exception e)
              {
                  System.out.println("error while creating into file ");
              }
     //  DirectoryChooser choseer = new DirectoryChooser();
     //  choseer.setTitle("save in ...");
     //  File selectedDirectory=choseer.showDialog(stage);
     //  File file=new File(selectedDirectory.getAbsolutePath()+"/rapport"+rapport.getIdZone()+(rapport.getGenDate().toString().replaceAll("-","")));
     //  byte[] data=function.getRapportFile(rapport.getIdZone(),rapport.getGenDate());
     //  try {
     //      FileOutputStream fos  = new FileOutputStream(file);
     //      fos.write(data,0,data.length);
     //      fos.close();
     //      String change= file.getAbsolutePath();
     //      file.renameTo(new File(change+".pdf"));
     //  } catch (Exception e) {
     //      throw new RuntimeException(e);
     //  }


    }


    @FXML
    private void Back(){
        Parent root;
        try {
            FXMLLoader loader=new  FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml"));
            root=loader.load();
            HomePageController controller=loader.getController();
            controller.getidfc(this.cni);
            new WindowOManager().makeAndHideWindow(root,(Stage) closeBtn.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("something went wrong");
            e.printStackTrace();
        }
    }

    public void getidfc(String cni,String zoneId) {
        this.cni = cni;
        this.zoneId=zoneId;
        fillWithRapports(zoneId);
    }
}
