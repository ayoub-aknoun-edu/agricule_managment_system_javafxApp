package com.app.engineerapp.Dashboard.CreateZone;


import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.FunctionUtil.JsonFormnParser;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Models.Section;
import com.app.engineerapp.Models.Zone;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CreateZoneController implements Initializable {
    @FXML
    private AnchorPane all;

    @FXML
    private AnchorPane slider;

    @FXML
    private JFXButton preBtn;

    @FXML
    private JFXButton suivBtn;
    @FXML
    private JFXButton valideBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private VBox sectionsVbox;
    @FXML
    private JFXButton closeBtn;
    @FXML
    private ImageView loadingimj;
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


    private int currentView = 1;
    protected boolean isProprietaireNull, isSurafaceNull, isAdresseNull, isProdutsNull, isExaminateurNull, isResoponsableNull;
    Image imageMinus=new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("img/minusBtn.png")));
    Image imageAdd=new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("img/addBtn.png")));
    String cni;
    HashMap<String, String> responsablesMap;
    HashMap<String, String> examinateurMap;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isProprietaireNull= isSurafaceNull= isAdresseNull= isProdutsNull= isExaminateurNull= isResoponsableNull=true;
        loadingimj.setVisible(false);
        preBtn.setVisible(false);
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
    void addSectionAction(ActionEvent event) {
        try {
            FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Dashboard/createZone/sectionCard.fxml"));
            Pane pane=loader.load();
          pane.getChildren().add(addButton(imageMinus,509d,"minus"));
            pane.getChildren().add(addButton(imageAdd,489d,"add"));
            sectionsVbox.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public JFXButton addButton(Image image,Double x,String type){
        JFXButton minusBtn=new JFXButton();
        ImageView imageView=new ImageView(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(22);
        minusBtn.setLayoutX(x);
        minusBtn.setLayoutY(1);
        minusBtn.setPrefHeight(25);
        minusBtn.setPrefWidth(31);
        minusBtn.setGraphic(imageView);
        minusBtn.setStyle("-fx-text-fill: transparent;");
        if (type.equals("minus")){
            minusBtn.setOnAction(event1 -> {
                sectionsVbox.getChildren().remove(minusBtn.getParent());
            });
        } else if (type.equals("minus-minus")) {
            minusBtn.setOnAction(event1 -> {
                Pane pane = (Pane) (minusBtn.getParent()).getParent().getParent();
                pane.getChildren().remove(minusBtn.getParent().getParent());
            });
        } else {
            minusBtn.setOnAction(event1 -> {
                ((Pane)minusBtn.getParent()).getChildren().forEach(e->{
                    VBox vBox= e instanceof VBox ? ((VBox) e) : null;
                    if (vBox!=null){
                        VBox sousVbox=new VBox();
                        sousVbox.setPrefWidth(543);
                        sousVbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        HBox hBox =new HBox();
                        hBox.setPrefWidth(543);
                        hBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        hBox.setAlignment(Pos.CENTER);
                        hBox.setSpacing(3);
                        hBox.setId("firstHbox");
                        JFXTextField textField=new JFXTextField();
                        textField.setPromptText("Qustion");
                        textField.setPrefWidth(330d);
                        JFXComboBox<String> comboBox=new JFXComboBox<>();
                        comboBox.getItems().addAll("yesNo","Text","CHECKBOX");
                        comboBox.setPromptText("Type");
                        comboBox.setPrefWidth(300d);
                        comboBox.getSelectionModel().selectedItemProperty().addListener((options,oldvalue,newvalue)->{
                           try {
                                if (newvalue.equals("CHECKBOX") && (oldvalue == null || oldvalue != null)) {
                                    Pane parent = (Pane) comboBox.getParent().getParent();
                                    JFXTextField checktextField = new JFXTextField();
                                    checktextField.setPromptText("entrer options separer par ;");
                                    checktextField.setPrefWidth(330d);
                                    HBox checkhBox = new HBox();
                                    checkhBox.setId("checkHbox");
                                    checkhBox.setPadding(new Insets(0, 0, 0, 30));
                                    checkhBox.setPrefWidth(543);
                                    checkhBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                    checkhBox.setAlignment(Pos.CENTER_LEFT);
                                    checkhBox.getChildren().add(checktextField);
                                    parent.getChildren().add(checkhBox);
                                } else if (oldvalue.equals("CHECKBOX") && !newvalue.equals("CHECKBOX") && newvalue != null) {
                                    Pane parent = (Pane) comboBox.getParent().getParent();
                                    parent.getChildren().remove(1);
                                } else if (oldvalue == null && !newvalue.equals("CHECKBOX")) {
                                }
                            }catch (Exception Ex){
                               System.out.println();
                           }
                        });
                        comboBox.setPrefWidth(100d);
                        hBox.getChildren().addAll(textField,comboBox,addButton(imageMinus,509d,"minus-minus"));
                        sousVbox.getChildren().add(hBox);
                        vBox.getChildren().add(sousVbox);
                    }

                });
            });
        }

        return minusBtn;
    }


    @FXML
    private void moveForward(){
        currentView++;
        if (currentView == 2) moveScene(583);
        updateArrowBtn();
    }
    @FXML
    private void moveBackward(){
        currentView--;
        if (currentView == 1) moveScene(0);
        updateArrowBtn();
    }
    private void updateArrowBtn(){
        switch (currentView) {
            case 1 :
                preBtn.setVisible(false);
                suivBtn.setVisible(true);
                valideBtn.setText("default form");
            break;
            case 2 :
                preBtn.setVisible(true);
                suivBtn.setVisible(false);
                valideBtn.setText("valider");
            break;
        }
    }
    private void moveScene(int step){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(slider);
        slide.setToX(-step);
        slide.play();
    }


    public void passeur(String cni){
        this.cni=cni;
        boolean isAdmin=Variables.type.equals("ADMIN");
        respoLbl.setVisible(isAdmin);
        responsableCombo.setVisible(isAdmin);
        if (isAdmin){
            examinateurMap= function.getRespoNamesCni("technicien");
            responsablesMap=function.getRespoNamesCni("responsable");
            responsableCombo.getItems().addAll(responsablesMap.values());

        }else
            examinateurMap= function.getTechNamesCni(cni);
        examinateurCombo.getItems().addAll(examinateurMap.values());
    }


    private void remButtons(){
        valideBtn.setDisable(isProprietaireNull|| isSurafaceNull|| isAdresseNull|| isProdutsNull);
        suivBtn.setDisable(isProprietaireNull|| isSurafaceNull|| isAdresseNull|| isProdutsNull);
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
    ArrayList<Section> sections = new ArrayList<>();
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> types = new ArrayList<>();
    ArrayList<ArrayList<String>> options= new ArrayList<>();
    Section section ;

    private ArrayList<Section> fetchSection(){
        try{
        sectionsVbox.getChildren().forEach(pane -> {
            Pane sectionPane = pane instanceof Pane ? ((Pane)pane):null;
            section=new Section();
            if (sectionPane!=null)
                sectionPane.getChildren().forEach(element ->{
                    if (element instanceof JFXTextField){
                        String title=  ((JFXTextField)element).getText();
                        section.setTitle(title);
                    }
                    VBox superVbox = element instanceof VBox ? ((VBox) element) : null;
                    if (superVbox != null) {
                        questions = new ArrayList<>();
                        types = new ArrayList<>();
                        options = new ArrayList<>();
                        superVbox.getChildren().forEach(v_box -> {
                            VBox vbox = v_box instanceof VBox ? ((VBox) v_box) : null;
                            if (vbox != null) {
                                vbox.getChildren().forEach(h_box -> {
                                    HBox continer = h_box instanceof HBox ? ((HBox) h_box) : null;
                                    if (continer != null) {
                                        if (continer.getId().equals("firstHbox")) {
                                            continer.getChildren().forEach(traite -> {

                                                    if (traite instanceof JFXTextField) {
                                                        questions.add(((JFXTextField) traite).getText());
                                                    } else if (traite instanceof JFXComboBox){
                                                        if (((JFXComboBox) traite).getValue()==null)((JFXComboBox) traite).getSelectionModel().selectFirst();
                                                        types.add(((JFXComboBox) traite).getValue().toString());
                                                    }


                                            });
                                        }
                                        if (continer.getId().equals("checkHbox")) {
                                            String st= ((JFXTextField) continer.getChildren().get(0)).getText();
                                            ArrayList<String> list = new ArrayList<>();
                                            list.addAll(Arrays.asList(st.split(";")));
                                            options.add(list);
                                        }
                                    }
                                });
                                section.setQuestions(questions);
                                section.setTypes(types);
                                section.setOptions(options);
                            }
                        });
                    }
                });
            sections.add(section);

        });
        }catch (Exception e){
            System.out.println("");
        }
        return sections;
    }


    @FXML
    private void valide(){
        String tech=function.getKey(examinateurMap,examinateurCombo.getValue()).replaceAll("Optional\\[","").replaceAll("\\]","");
        String respo= Variables.type.equals("ADMIN")?function.getKey(responsablesMap,responsableCombo.getValue()).replaceAll("Optional\\[","").replaceAll("\\]",""):this.cni;
        Zone zone=new Zone(adresseText.getText(), Double.parseDouble(surfaceText.getText()), respo, tech, Date.valueOf(LocalDate.now()), 1, null, produitsText.getText(), proprietaireText.getText());
        loadingimj.setVisible(true);
        ArrayList<Section> fetchedSections = fetchSection();
        Task<Integer> task =new Task<>() {
            @Override
            protected Integer call() throws Exception {
                return JsonFormnParser.json_form_maker(zone, fetchedSections);

            }
        };
        task.setOnSucceeded(e-> {
                loadingimj.setVisible(false);
                menuBack();
        });
        new Thread(task).start();
    }
}
