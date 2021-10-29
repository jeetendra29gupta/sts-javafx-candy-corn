package org.juju.work;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@SpringBootApplication
public class StsJavafxCandyCornApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public static BorderPane root = new BorderPane();
	private int width = 8;
	private String[] colors = { "blue", "orange", "purple", "red", "yellow", "green" };
	private List<String> candys = getSugarCandy();
	private int score = 0;

	private List<String> getSugarCandy() {
		Random rn = new Random();
		List<String> sugarCandy = new ArrayList<>();
		for (int i = 0; i < width * width; i++) {
			int answer = rn.nextInt(5) + 1;
			String randomColor = colors[answer];
			sugarCandy.add(randomColor);
		}
		return sugarCandy;
	}

	@Override
	public void start(Stage primaryStage) {
		root.setCenter(getCandyBox());
		primaryStage.setTitle("Candy Corn");
		primaryStage.setResizable(false);
		Scene scene = new Scene(root, 700, 700, Color.BEIGE);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("icon.png"));

		primaryStage.setOnShown(event -> {
			checkForColumnOfFour();
			checkForRowOfFour();
			checkForColumnOfThree();
			checkForRowOfThree();
			moveIntoSquareBelow();
			root.getChildren().clear();
			root.setTop(getScore());
			root.setCenter(getCandyBox());
		});

		primaryStage.show();
	}

	private HBox getScore() {
		Label label = new Label(score + "");
		label.setStyle("-fx-padding: 10.0px;-fx-font-weight: bold;");
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setStyle("-fx-background-color: #008080;");
		hBox.getChildren().add(label);
		return hBox;
	}

	private FlowPane getCandyBox() {
		FlowPane flowpane = new FlowPane(Orientation.HORIZONTAL);
		flowpane.getChildren().clear();
		for (int i = 0; i < width * width; i++) {
			Image image = new Image(candys.get(i) + "-candy.png");
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(70);
			imageView.setPreserveRatio(true);
			imageView.setId(i + "");
			imageView.setOnDragDetected(event -> {
				Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(imageView.getId());
				db.setContent(content);
				event.consume();
			});
			imageView.setOnMouseDragged(event -> {
				event.setDragDetect(true);
			});
			imageView.setOnDragOver(event -> {
				if (event.getGestureSource() != imageView && event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			});
			imageView.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasString()) {
					int sourceId = Integer.parseInt(db.getString());
					String sourceColour = candys.get(sourceId);

					int targetId = Integer.parseInt(imageView.getId());
					String targetColour = candys.get(targetId);
					candys.set(sourceId, targetColour);
					candys.set(targetId, sourceColour);
					checkForColumnOfFour();
					checkForRowOfFour();
					checkForColumnOfThree();
					checkForRowOfThree();
					moveIntoSquareBelow();
					root.getChildren().clear();
					root.setTop(getScore());
					root.setCenter(getCandyBox());

					event.setDropCompleted(true);
				} else {
					event.setDropCompleted(false);
				}
				event.consume();
			});
			flowpane.getChildren().add(imageView);
		}
		flowpane.setPadding(new Insets(10));
		flowpane.setHgap(10);
		flowpane.setVgap(10);
		flowpane.setAlignment(Pos.CENTER);
		return flowpane;
	}

	private void checkForColumnOfFour() {
		for (int i = 0; i < 40; i++) {
			int[] columnOfFour = { i, i + width, i + width * 2, i + width * 3 };
			if (candys.get(columnOfFour[0]).equals(candys.get(columnOfFour[1]))
					&& candys.get(columnOfFour[1]).equals(candys.get(columnOfFour[2]))
					&& candys.get(columnOfFour[2]).equals(candys.get(columnOfFour[3]))) {
				candys.set(columnOfFour[0], "blank");
				candys.set(columnOfFour[1], "blank");
				candys.set(columnOfFour[2], "blank");
				candys.set(columnOfFour[3], "blank");
				score = score + 4;
			}
		}
	}

	private void checkForRowOfFour() {
		ArrayList<Integer> notValid = new ArrayList<>();
		notValid.add(5);
		notValid.add(6);
		notValid.add(7);
		notValid.add(13);
		notValid.add(14);
		notValid.add(15);
		notValid.add(21);
		notValid.add(22);
		notValid.add(23);
		notValid.add(29);
		notValid.add(30);
		notValid.add(31);
		notValid.add(37);
		notValid.add(38);
		notValid.add(39);
		notValid.add(45);
		notValid.add(46);
		notValid.add(47);
		notValid.add(53);
		notValid.add(54);
		notValid.add(55);
		notValid.add(62);
		notValid.add(63);
		notValid.add(64);

		for (int i = 0; i < 64; i++) {
			int[] rowOfFour = { i, i + 1, i + 2, i + 3 };
			if (!notValid.contains(i)) {
				// System.out.println(Arrays.toString(rowOfFour));
				if (candys.get(rowOfFour[0]).equals(candys.get(rowOfFour[1]))
						&& candys.get(rowOfFour[1]).equals(candys.get(rowOfFour[2]))
						&& candys.get(rowOfFour[2]).equals(candys.get(rowOfFour[3]))) {
					candys.set(rowOfFour[0], "blank");
					candys.set(rowOfFour[1], "blank");
					candys.set(rowOfFour[2], "blank");
					candys.set(rowOfFour[3], "blank");
					score = score + 4;
				}
			}
		}

	}

	private void checkForRowOfThree() {
		ArrayList<Integer> notValid = new ArrayList<>();
		notValid.add(6);
		notValid.add(7);
		notValid.add(14);
		notValid.add(15);
		notValid.add(22);
		notValid.add(23);
		notValid.add(30);
		notValid.add(31);
		notValid.add(38);
		notValid.add(39);
		notValid.add(46);
		notValid.add(47);
		notValid.add(54);
		notValid.add(55);
		notValid.add(63);
		notValid.add(64);
		for (int i = 0; i < 64; i++) {
			int[] rowOfThree = { i, i + 1, i + 2 };
			if (!notValid.contains(i)) {
				if (candys.get(rowOfThree[0]).equals(candys.get(rowOfThree[1]))
						&& candys.get(rowOfThree[1]).equals(candys.get(rowOfThree[2]))) {
					candys.set(rowOfThree[0], "blank");
					candys.set(rowOfThree[1], "blank");
					candys.set(rowOfThree[2], "blank");
					score = score + 3;
				}
			}
		}
	}

	private void checkForColumnOfThree() {
		for (int i = 0; i < 48; i++) {
			int[] columnOfThree = { i, i + width, i + width * 2 };
			if (candys.get(columnOfThree[0]).equals(candys.get(columnOfThree[1]))
					&& candys.get(columnOfThree[1]).equals(candys.get(columnOfThree[2]))) {
				candys.set(columnOfThree[0], "blank");
				candys.set(columnOfThree[1], "blank");
				candys.set(columnOfThree[2], "blank");
				score = score + 3;
			}
		}
	}

	private void moveIntoSquareBelow() {
		ArrayList<Integer> isFirstRow = new ArrayList<>();
		isFirstRow.add(0);
		isFirstRow.add(1);
		isFirstRow.add(2);
		isFirstRow.add(3);
		isFirstRow.add(4);
		isFirstRow.add(5);
		isFirstRow.add(6);
		isFirstRow.add(7);

		while (candys.contains("blank")) {
			for (int i = 0; i < 56; i++) {
				Random rn = new Random();
				if (isFirstRow.contains(i) && candys.get(i) == "blank") {
					int answer = rn.nextInt(5) + 1;
					String randomColor = colors[answer];
					candys.set(i, randomColor);
				}

				if ((candys.get(i + width)) == "blank") {
					candys.set(i + width, candys.get(i));
					candys.set(i, "blank");
				}
			}
		}
	}
}
