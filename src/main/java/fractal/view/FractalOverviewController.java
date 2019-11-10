package fractal.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fractal.MainApp;
import fractal.util.Dispatcher;
import fractal.model.Coordinate;
import fractal.model.Variable;
import fractal.util.StringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FractalOverviewController {
	@FXML
	private TextField equationField;
	@FXML
	private Button extractButton;

	@FXML
	private Canvas previewCanvas;
	@FXML
	private Button previewButton;

	@FXML
	private Canvas drawCanvas;
	@FXML
	private Button drawButton;

	private MainApp mainApp;
	private Timeline timeline;

	private Color[] palette = new Color[256 * 256 * 256];
	
	private ObservableList<String> variableChoice = FXCollections.observableList(new ArrayList<>());

	private void init() {
		Set<String[]> hexPalette = new LinkedHashSet<>();
		hexPalette.add(new String[] { "33", "19", "00" }); // dark brown
		hexPalette.add(new String[] { "7d", "52", "17" });
		hexPalette.add(new String[] { "94", "70", "2e" });
		hexPalette.add(new String[] { "ab", "8f", "45" });
		hexPalette.add(new String[] { "c2", "ad", "5c" });
		hexPalette.add(new String[] { "d9", "cc", "73" });
		hexPalette.add(new String[] { "f0", "eb", "8a" });
		hexPalette.add(new String[] { "ff", "ff", "99" });
		hexPalette.add(new String[] { "cc", "ff", "ff" });
		hexPalette.add(new String[] { "ad", "d9", "e8" });
		hexPalette.add(new String[] { "8f", "b2", "d1" });
		hexPalette.add(new String[] { "70", "8c", "ba" });
		hexPalette.add(new String[] { "52", "66", "a3" });
		hexPalette.add(new String[] { "33", "40", "8c" });
		hexPalette.add(new String[] { "1f", "26", "7d" });
		hexPalette.add(new String[] { "00", "00", "66" }); // dark blue

		Iterator<String[]> hexPaletteIt = hexPalette.iterator();

		for (int i = 0; i < hexPalette.size(); i++) {
			if (!hexPaletteIt.hasNext())
				hexPaletteIt = hexPalette.iterator();

			String[] colors = hexPaletteIt.next();
			int xColor = Integer.parseInt(colors[0], 16);
			int yColor = Integer.parseInt(colors[1], 16);
			int zColor = Integer.parseInt(colors[2], 16);
			palette[i] = Color.rgb(xColor, yColor, zColor);
		}
		
		previewButton.setDisable(true);
		drawButton.setDisable(true);
	}

	public void setMainApp(MainApp mainApp) {
		mainApp.getPrimaryStage().setResizable(false);
		this.mainApp = mainApp;
		init();
	}

	public String getEquation() {
		return equationField.getText().trim();
	}

	@FXML
	private void handleExtractEquation() {
		try {
			if (equationField.getText().length() == 0)
				throw new IllegalArgumentException("No variable to be extracted!");

			Set<Variable> variablesSet = new HashSet<>();
			variablesSet = StringUtil.extractVariables(equationField.getText());

			mainApp.setVariableData(FXCollections.observableArrayList(variablesSet));

			boolean okClicked = mainApp.showVariableEditDialog();
			
			previewButton.setDisable(false);
			drawButton.setDisable(false);

		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Equation");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	private void handlePreviewButton() {
		previewButton.setDisable(true);
		drawButton.setDisable(true);
		extractButton.setDisable(true);
		GraphicsContext gc = previewCanvas.getGraphicsContext2D();

		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		double[][] numIteration = null;
		
		Dispatcher dispatcher = new Dispatcher(gc, equationField, mainApp, numIteration);
		Thread tDispatcher = new Thread(dispatcher, "Dispatcher thread");
		tDispatcher.start();

		beginToDraw(gc, dispatcher);
	}

	@FXML
	private void handleDrawButton() {
		previewButton.setDisable(true);
		drawButton.setDisable(true);
		extractButton.setDisable(true);
		GraphicsContext gc = drawCanvas.getGraphicsContext2D();

		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		double[][] numIteration = null;
		
		Dispatcher dispatcher = new Dispatcher(gc, equationField, mainApp, numIteration);
		Thread tDispatcher = new Thread(dispatcher, "Dispatcher thread");
		tDispatcher.start();

		beginToDraw(gc, dispatcher);
	}

	private void beginToDraw(GraphicsContext gc, Dispatcher dispatcher) {
		timeline = new Timeline(
				new KeyFrame(Duration.millis(1000), ae -> drawCanvas(gc, dispatcher)));
		timeline.setCycleCount(60);
	
		timeline.setOnFinished(event -> {
			extractButton.setDisable(false);
		});
		
		timeline.play();
	}

	private void drawCanvas(GraphicsContext gc, Dispatcher dispatcher) {	
		double[][] numIteration = dispatcher.getNumIteration();

		Coordinate beginCoord = new Coordinate(0, 0);
		for (double xCoord = beginCoord.getX(); xCoord < gc.getCanvas().getWidth(); xCoord++) {
			for (double yCoord = beginCoord.getY(); yCoord < gc.getCanvas().getHeight(); yCoord++) {

				double iteration = numIteration[(int) xCoord][(int) yCoord];

				Color color1 = palette[(int) Math.floor(iteration) % 16];
				Color color2 = palette[(int) Math.floor(iteration + 1) % 16];
				double ratio = iteration % 1;
				double redInterpolated = color2.getRed() * ratio + color1.getRed() * (1 - ratio);
				double greenInterpolated = color2.getGreen() * ratio + color1.getGreen() * (1 - ratio);
				double blueInterpolated = color2.getBlue() * ratio + color1.getBlue() * (1 - ratio);

				Color colorToFill = Color.rgb((int) (redInterpolated * 255), (int) (greenInterpolated * 255),
						(int) (blueInterpolated * 255));
				gc.setFill(colorToFill);
				gc.fillRect(xCoord, yCoord, 1, 1);
			}
		}		
	}
}
