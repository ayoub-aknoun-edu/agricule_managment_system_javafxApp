package com.app.engineerapp.Dashboard.Home;

import com.app.engineerapp.Models.Zone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemZoneController {
    @FXML
    private Label zoneId;

    @FXML
    private Label tecName;

    @FXML
    private Label examDate;

    private Zone zone ;
    ItemZoneListner listner;


    @FXML
    void click(ActionEvent event) {
        listner.onClickListner(zone);
    }
    public void  setData(Zone zone, ItemZoneListner listner){
        this.zone=zone;
        this.listner=listner;
        zoneId.setText(zoneId.getText()+" "+zone.getZoneId());
        tecName.setText(zone.getIsExamineur());
        examDate.setText(String.valueOf(zone.getLast_Examin()));
    }






}
