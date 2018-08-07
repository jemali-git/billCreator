package billCreator.gui.view.editor.dataView;

import billCreator.gui.view.editor.model.ColumnModel;
import billCreator.gui.view.editor.model.RowModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Callback;

public class BCTableColumn extends TableColumn<RowModel, String> {

	BCDialog bcDialog;
	ColumnModel columnModel;

	public BCTableColumn(ColumnModel columnModel) {

		this.columnModel = columnModel;
		setText(columnModel.getReference().get());
		setEditable(true);
		setMinWidth(100);
		addMenu();
		setCellValueFactory(new Callback<CellDataFeatures<RowModel, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<RowModel, String> p) {
				return p.getValue().getValues().get(columnModel.getId()).getValue();
			}
		});

		setCellFactory(TextFieldTableCell.forTableColumn());
		setOnEditCommit(new EventHandler<CellEditEvent<RowModel, String>>() {
			@Override
			public void handle(CellEditEvent<RowModel, String> t) {
				t.getRowValue().getValues().get(columnModel.getId()).getValue().set(t.getNewValue());
			}
		});
		bcDialog = new BCDialog(this, columnModel);
	}

	private void addMenu() {
		MenuItem newRow = new MenuItem("Edit");
		newRow.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		newRow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bcDialog.showAndWait();
			}
		});
		setContextMenu(new ContextMenu(newRow));
	}

	public ColumnModel getColumnModel() {
		return columnModel;
	}

	public void setColumnModel(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}

}
