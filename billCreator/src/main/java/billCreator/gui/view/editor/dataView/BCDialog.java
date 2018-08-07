package billCreator.gui.view.editor.dataView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import billCreator.gui.WorkBenchWindow;
import billCreator.gui.view.editor.model.ColumnModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BCDialog extends Stage {

	Button button = new Button("Apply");
	TextField refrence = new TextField();
	TextField expressionValue = new TextField();
	ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("$Date::now", "$Increment::+1"));

	public BCDialog(BCTableColumn bcTableColumn, ColumnModel columnModel) {
		setResizable(false);
		setTitle("Edit Column Proproties");
		initModality(Modality.WINDOW_MODAL);
		initOwner(WorkBenchWindow.primaryStage);

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20, 20, 20, 20));
		gridPane.addColumn(0, new Label("Reference"), new Label("Expression Value"));

		refrence.setText(columnModel.getReference().get());
		columnModel.getReference().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				refrence.setText(newValue);
				bcTableColumn.setText(newValue);
			}
		});

		expressionValue.setText(columnModel.getExpressionValue().get());
		columnModel.getExpressionValue().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				expressionValue.setText(newValue);
			}
		});

		comboBox.setOnAction(value -> {
			expressionValue.setText(comboBox.getSelectionModel().getSelectedItem());
		});
		comboBox.setMaxWidth(10);
		HBox hBox = new HBox(expressionValue, comboBox);
		hBox.setSpacing(5);

		//columnModel.getExpressionValue().set(bcTableColumn.getText());

		button.setOnAction(value -> {
			columnModel.getReference().set(refrence.getText());
			columnModel.getExpressionValue().set(expressionValue.getText());
			close();

		});
		gridPane.addColumn(1, refrence, hBox, button);
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		Scene scene = new Scene(gridPane);
		setScene(scene);
	}
}
