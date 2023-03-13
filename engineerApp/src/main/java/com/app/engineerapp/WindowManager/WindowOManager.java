package com.app.engineerapp.WindowManager;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

public class WindowOManager {
    static HashMap<String, Stage> StagesArchive = new HashMap<>();
    public Stage makeWindowTrans(Parent root) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        //stage.getIcons().add(new Image("Dashboard/img/wheel_icow.png"));
        stage.show();
        return stage;
    }
    public void makeAndHideWindow(Parent root,Stage stageToHide){
        stageToHide.hide();
        makeWindowTrans(root);
    }
    public void hideWindow(Stage stageToHide){
        stageToHide.hide();
    }
    public void makeAndSave(String title,Parent root){
        Stage stgSaved = makeWindowTrans(root);
        StagesArchive.put(title,stgSaved);
    }

    public void showAgain(String title){
        StagesArchive.get(title).toFront();
    }
    public void hideWindow(String title){
        hideWindow(StagesArchive.get(title));
    }
}
