package fractal.view;

import java.util.ArrayList;

import fractal.MainApp;
import fractal.model.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class VariableEditDialogController {

	@FXML
	private ChoiceBox<String> functionOfBox;
	@FXML
	private ChoiceBox<String> iterableBox;

	private MainApp mainApp;
	private Stage dialogStage;
	private boolean okClicked = false;
	private final ObservableList<String> variableChoice = FXCollections.observableList(new ArrayList<>());
	private final ObservableList<String> constantChoice = FXCollections.observableList(new ArrayList<>());

	@FXML
	private void init() {
		mainApp.getConstantData().clear();
		for (Variable variable : mainApp.getVariableData()) {
			variableChoice.add(variable.getName());
			constantChoice.add(variable.getName());
		}
		functionOfBox.setItems(variableChoice);
		iterableBox.setItems(variableChoice);

		functionOfBox.setOnAction((event) -> {
			constantChoice.clear();
			constantChoice.addAll(variableChoice);
			for (String variable : variableChoice) {
				if (functionOfBox.getSelectionModel().getSelectedItem().equals(variable))
					constantChoice.remove(variable);
				if (!iterableBox.getSelectionModel().isEmpty())
					if (iterableBox.getSelectionModel().getSelectedItem().equals(variable))
						constantChoice.remove(variable);
			}
		});

		iterableBox.setOnAction((event) -> {
			constantChoice.clear();
			constantChoice.addAll(variableChoice);
			for (String variable : variableChoice) {
				if (functionOfBox.getSelectionModel().getSelectedItem().equals(variable))
					constantChoice.remove(variable);
				if (!functionOfBox.getSelectionModel().isEmpty())
					if (iterableBox.getSelectionModel().getSelectedItem().equals(variable))
						constantChoice.remove(variable);
			}
		});
	}

	public void setMainApp(MainApp mainApp) {
		mainApp.getPrimaryStage().setResizable(false);
		this.mainApp = mainApp;
		init();
	}

	public void setDialogStage(Stage dialogStage) {
		dialogStage.setResizable(false);
		this.dialogStage = dialogStage;
	}

	public boolean isOKClicked() {
		return okClicked;
	}

	@FXML
	private void handleOK() {
		try {
			for (Variable variable : mainApp.getVariableData()) {
				if (variable.getName().equals(functionOfBox.getSelectionModel().getSelectedItem())) {
					// TODO: an indicator to show that the variable is the functionOf
					variable.setInput(true);
				}
				if (variable.getName().equals(iterableBox.getSelectionModel().getSelectedItem())) {
					variable.setIterable(true);
				}
			}
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Complex Number");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
