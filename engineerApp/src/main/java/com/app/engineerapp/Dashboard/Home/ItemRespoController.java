package com.app.engineerapp.Dashboard.Home;

import com.app.engineerapp.Models.Responsable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemRespoController {

    @FXML
    private Label respoId;
    @FXML
    private Label nomLbl;

    @FXML
    private Label prenomLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label teleLbl;

    private Responsable responsable;
    ItemRespoListner listner;


    @FXML
    void click(ActionEvent event) {
        listner.onClickListner(responsable);
    }

    public void  setData(Responsable responsable, ItemRespoListner listner){

        this.responsable=responsable;
        this.listner=listner;
        System.out.println(responsable.getCni());
        respoId.setText(respoId.getText()+" "+responsable.getCni());
        nomLbl.setText(responsable.getNom());
        prenomLbl.setText(responsable.getPrenom());
        emailLbl.setText(responsable.getEmail());
        teleLbl.setText(responsable.getTele());
    }
}
