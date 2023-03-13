package com.app.engineerapp.Dashboard.Rapports;

import com.app.engineerapp.Models.Rapport;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class RapportController {

    @FXML
    private Label nameLabel;

    @FXML
    private JFXButton openAnn;

    @FXML
    private Label dateLabel;
    private ListenerDelete listenerDelete;
    private ListenerOpen listenerOpen;
    private Rapport rapport;



    @FXML
    void delete(MouseEvent event) {
        listenerDelete.onClickListener(rapport);
    }

    @FXML
    void open(MouseEvent event) {
        listenerOpen.onClickListener(rapport);
    }

    public void setData(Rapport rapport,ListenerDelete listenerDelete,ListenerOpen listenerOpen){
        this.rapport=rapport;
        this.listenerOpen=listenerOpen;
        this.listenerDelete=listenerDelete;
        nameLabel.setText("R_Z"+rapport.getIdZone()+(rapport.getGenDate().toString().replaceAll("/","")));
        dateLabel.setText(rapport.getGenDate().toString());

    }

}
