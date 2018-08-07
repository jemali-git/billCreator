package billCreator.gui.view.editor.dataView.menu;

import billCreator.gui.view.editor.dataView.BCTableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class RowMenu extends ContextMenu {
	public RowMenu(BCTableView bcTableView) {
		MenuItem newRowItem = new MenuItem("New");
		newRowItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newRowItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bcTableView.addRow();
			}
		});
		MenuItem getPdfItem = new MenuItem("Get Pdf");
		getPdfItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// bcTableView.getPdf();
			}
		});
		MenuItem deleteRowItem = new MenuItem("Delete");
		deleteRowItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		deleteRowItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bcTableView.deleteRow();
			}
		});
		getItems().addAll(newRowItem, getPdfItem, deleteRowItem);
	}
}
