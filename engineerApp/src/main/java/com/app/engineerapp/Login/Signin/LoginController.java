package com.app.engineerapp.Login.Signin;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

//import Dashboard.HomeContainer.DashboardContainerController;
import com.app.engineerapp.Dashboard.Home.HomePageController;
import com.app.engineerapp.DataBaseUtil.dbConnection;
//import com.app.engineerapp.FunctionUtile.function;
//import com.app.engineerapp.Login.Verification.codeVerifyConnectController;
import com.app.engineerapp.FunctionUtil.Variables;
import com.app.engineerapp.FunctionUtil.function;
import com.app.engineerapp.HelloApplication;
import com.app.engineerapp.Login.Verification.codeVerifyConnectController;
import com.app.engineerapp.WindowManager.WindowOManager;
import com.jfoenix.controls.*;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class LoginController implements Initializable {
	@FXML
	private AnchorPane pane ;
	@FXML
	private JFXTextField inpName,inpPasswordShower;
	@FXML
	private JFXPasswordField inpPassword;
	@FXML
	private Label incorrectLabel;
	@FXML
	private JFXButton crossBtn,minusBtn;
	@FXML
	private JFXButton openBtn,btnSignIn,closeBtn,showPassword,hidePassword;
	@FXML
	private ImageView loadingimj;
	@FXML
	private StackPane dialogContainer;
	@FXML
	private JFXDialog exitDialog;
	@FXML
	private JFXButton acceptButton;
	@FXML
	private Pane paneForgot;
	@FXML
	private Text txtForgot;
	@FXML
	private JFXComboBox<String>  combo;

	@FXML
	private JFXButton declineButton;

	private boolean isNameNull = true;
	private boolean isPassNull = true;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paneForgot.setEffect(new DropShadow(2.0, Color.GRAY));
		exitDialog.setDialogContainer(dialogContainer);
		pane.setTranslateX(-450);
		hidePassword.setVisible(false);
		inpPasswordShower.setVisible(false);
		loadingimj.setVisible(false);
		incorrectLabel.setVisible(false);
		closeBtn.setVisible(false);
		inpName.textProperty().addListener((observable, oldValue, newValue) -> {
            incorrectLabel.setVisible(false);
            isNameNull = newValue.equals("");
			updateConnectBtn();
		});
		showPassword.setOnMouseClicked(e-> showHidePassOption(true));
		hidePassword.setOnMouseClicked(e-> showHidePassOption(false));
		inpPasswordShower.textProperty().addListener((observable, oldValue, newValue) -> {
			incorrectLabel.setVisible(false);
			isPassNull = newValue.equals("");
			inpPassword.setText(inpPasswordShower.getText());
			updateConnectBtn();
		});
		inpPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            incorrectLabel.setVisible(false);
            isPassNull = newValue.equals("");
            inpPasswordShower.setText(inpPassword.getText());
			updateConnectBtn();
		});
		crossBtn.setOnMouseClicked(e-> exitDialog.show());
		declineButton.setOnAction(actionEvent -> exitDialog.close());
		acceptButton.setOnAction(actionEvent -> {
			exitDialog.close();
			System.exit(0);
		});
		minusBtn.setOnMouseClicked( event -> {
			Stage stage = (Stage) minusBtn.getScene().getWindow();
			stage.setIconified(true);
		});
		combo.getItems().addAll("ADMIN","INGENIEUR");
		combo.setValue("INGENIEUR");

	}

	private void showHidePassOption(boolean whatToDo){
		inpPasswordShower.setVisible(whatToDo);
		inpPassword.setVisible(!whatToDo);
		showPassword.setVisible(!whatToDo);
		hidePassword.setVisible(whatToDo);
	}
	private void updateConnectBtn() {
		btnSignIn.setDisable(isNameNull || isPassNull);
	}
	@FXML
	public void openSlider(){
		// declare slide
		openBtn.setVisible(false);
		TranslateTransition slide = new TranslateTransition();
		// duration of the transition
		slide.setDuration(Duration.seconds(0.7));
		// slide responsable of slider setX to 0 then start
		slide.setNode(pane);
		slide.setToX(0);
		slide.play();
		slide.setOnFinished(e->closeBtn.setVisible(true));
	}
	@FXML
	public void closeSlider(){
		closeBtn.setVisible(false);
		openBtn.setVisible(true);
		// declare slide
		TranslateTransition slide = new TranslateTransition();
		// duration of the transition
		slide.setDuration(Duration.seconds(0.7));
		// slide responsable of slider setX to 0 then start
		slide.setNode(pane);
		slide.setToX(-400);
		slide.play();
	}
	public void tryConnect() {

		loadingimj.setVisible(true);
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				return Integer.parseInt(String.valueOf(handleSignIn()));
			}
		};
		task.setOnSucceeded((e)->{
			loadingimj.setVisible(false);
			PauseTransition pause;
			switch (task.getValue()){
				case -1:
					incorrectLabel.setText("permession refusé");
					closeBtn.setFocusTraversable(false);
					incorrectLabel.setVisible(true);
					pause = new PauseTransition(Duration.seconds(2));
					pause.setOnFinished(event -> incorrectLabel.setVisible(false));
					pause.play();
					break;
				case 1:
					callMainController();
					break;
				case 2:
					callVerifyWindow();
					break;
				case 3:
					incorrectLabel.setText("email ou mot de pass incorrect");
					closeBtn.setFocusTraversable(false);
					incorrectLabel.setVisible(true);
					pause = new PauseTransition(Duration.seconds(2));
					pause.setOnFinished(event -> incorrectLabel.setVisible(false));
					pause.play();
					break;
				case 4:
					incorrectLabel.setText("compte verrouillé, contactez l'administrateur");
					closeBtn.setFocusTraversable(false);
					incorrectLabel.setVisible(true);
					pause = new PauseTransition(Duration.seconds(2));
					pause.setOnFinished(event -> incorrectLabel.setVisible(false));
					pause.play();
					break;
			}

		});
		new Thread(task).start();
	}

	public int handleSignIn() {
		int etat =connexion(inpName.getText(),inpPassword.getText(),combo.getValue());
		try {
			if (etat == 1){
				System.out.println(1);
				return 1;
			}else if (etat == 2){
				System.out.println(2);
				function.forgotpassword(inpName.getText());
				return 2;
			}else if (etat==3){

				return 3;
			}else if (etat==-1){

				return -1;
			} else if (etat==4) {

				return 4;
			}
		}catch (Exception exception) { exception.printStackTrace(); }
		return 0;
	}
	private void callMainController(){
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Dashboard/Home/homePage.fxml"));
			root = loader.load();
			HomePageController dcc = loader.getController();
			dcc.getidfc(function.getcni(inpName.getText()));
			new WindowOManager().makeAndHideWindow(root,(Stage)btnSignIn.getScene().getWindow());
		} catch (IOException e) {
			System.out.println("something went wrong");
			e.printStackTrace();
		}
	}

	private void callVerifyWindow(){
		Parent root;
		try {
			FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("Login/Verification/codeVerifyConnect.fxml"));
			root = loader.load();
			codeVerifyConnectController cvcc=loader.getController();
			cvcc.getemailfromothercontroller(inpName.getText());
			new WindowOManager().makeAndHideWindow(root,(Stage)btnSignIn.getScene().getWindow());
		} catch (IOException e) {
			System.out.println("something went wrong");
			e.printStackTrace();
		}
	}
	@FXML
	private void forgotHover(){
		txtForgot.setStyle("-fx-font: 13.7 arial;");
	}
	@FXML
	private void forgotHoverExited(){
		txtForgot.setStyle("-fx-font: 13 arial;");
	}
	@FXML
	private void passwordForgottenWindow(){
		Parent root;
		try {
			root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Login/Verification/demandeEmail.fxml")));
			new WindowOManager().makeAndHideWindow(root,(Stage) paneForgot.getScene().getWindow());
		} catch (IOException e) {
			System.out.println("something went wrong");
			e.printStackTrace();
		}
	}
	public  int connexion(String email,String password,String type){
		if (function.ifemailexist(email)) {
			String sql="select r.*,p.email from responsable r join person p on p.cni = r.cni where email like ?";
			try {
				PreparedStatement pstat= dbConnection.getConnexion().prepareStatement(sql);
				pstat.setString(1,email);
				ResultSet rs= pstat.executeQuery();
				rs.setFetchSize(50);
				if (rs.next())
					if (rs.getString("password").equals(function.hashing(password+rs.getString("salt")))){
						System.out.println("password correct");
						if (type.equals("INGENIEUR")){
							if (rs.getInt("is_locked")==0){
								if (rs.getInt("is_verified")==1) {
									Variables.type=type;
									return 1;
								}else {
									System.out.println("account not verified try to verifie your account");
									return 2;
								}
							}else {
								System.out.println("account locked");
								return 4;
							}
						}else if (rs.getString("type").equals("ADMIN")){
							if (rs.getInt("is_verified")==1) {
								Variables.type=type;
								return 1;
							}else {
								System.out.println("account not verified try to verifie your account");
								return 2;
							}
						}else {
							System.out.println("permession refusé");
							return -1;
						}
				}else{
					System.out.println("password incorrect");
					return 3;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}}else
			System.out.println("email not found ");
		return 3;
	}
}