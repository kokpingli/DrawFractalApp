package fractal;

import java.io.IOException;
import fractal.model.Variable;
import fractal.view.FractalOverviewController;
import fractal.view.VariableEditDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private ObservableList<Variable> variableData = FXCollections.observableArrayList();
	private ObservableList<Variable> constantData = FXCollections.observableArrayList();

	public ObservableList<Variable> getVariableData() {
		return variableData;
	}

	public void setVariableData(ObservableList<Variable> data) {
		variableData = data;
	}

	public ObservableList<Variable> getConstantData() {
		return constantData;
	}

	public void setConstantData(ObservableList<Variable> data) {
		constantData = data;
	}

	private void showFractalOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../FractalOverview.fxml"));
			AnchorPane fractalOverview = (AnchorPane) loader.load();

			FractalOverviewController controller = loader.getController();
			controller.setMainApp(this);

			Scene scene = new Scene(fractalOverview);
			primaryStage.setScene(scene);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showVariableEditDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../VariableEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Variable");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			VariableEditDialogController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			return controller.isOKClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("DrawFractalApp");

		this.primaryStage.getIcons().add(new Image("file:resources/images/Fractal-Symmetric-Icon-02.jpg"));

		showFractalOverview();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
