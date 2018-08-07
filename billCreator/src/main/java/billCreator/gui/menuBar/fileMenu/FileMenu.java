package billCreator.gui.menuBar.fileMenu;

import billCreator.gui.view.explorer.Explorer;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class FileMenu extends Menu {
	public FileMenu() {
		setText("File");
		setMnemonicParsing(false);

		MenuItem newTemplate = new MenuItem("New Template");
		getItems().add(newTemplate);
		newTemplate.setMnemonicParsing(false);
		newTemplate.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newTemplate.setOnAction(value -> {
			Explorer.addTemplate();
		});

		MenuItem save = new MenuItem("Save");
		getItems().add(save);
		save.setMnemonicParsing(false);
		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		save.setOnAction(value -> {
			Explorer.saveData();
		});
	}

}
