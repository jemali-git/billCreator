package billCreator.gui.view.editor;

import billCreator.gui.view.editor.dataView.BCTableView;
import billCreator.gui.view.explorer.model.TemplateModel;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EditorTab extends Tab {

	public EditorTab(TemplateModel templateModel) {
		setText(templateModel.getTemplateFile().getName());
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10, 10, 50, 10));
		vBox.setSpacing(20);
		setContent(vBox);

		BCTableView bcTableView = new BCTableView( templateModel);
		VBox.setVgrow(bcTableView, Priority.SOMETIMES);
		vBox.getChildren().add(bcTableView);
	}
}
