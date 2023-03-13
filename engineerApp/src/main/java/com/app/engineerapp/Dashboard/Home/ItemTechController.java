package com.app.engineerapp.Dashboard.Home;


import com.app.engineerapp.Models.Technicien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemTechController {

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

    private Technicien technicien;
    ItemTechListner listner;


    @FXML
    void click(ActionEvent event) {
        listner.onClickListner(technicien);
    }

    public void  setData(Technicien technicien, ItemTechListner listner){

        this.technicien=technicien;
        this.listner=listner;
        System.out.println(technicien.getCni());
        respoId.setText(respoId.getText()+" "+technicien.getCni());
        nomLbl.setText(technicien.getNom());
        prenomLbl.setText(technicien.getPrenom());
        emailLbl.setText(technicien.getEmail());
        teleLbl.setText(technicien.getTele());
    }
}
