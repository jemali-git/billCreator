package billCreator.gui.view.explorer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import billCreator.gui.view.editor.model.ColumnModel;
import billCreator.gui.view.explorer.dataExplorer.TemplateItem;
import billCreator.gui.view.explorer.model.TemplateModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Explorer extends TabPane {

	static TreeItem<Object> templates = new TreeItem<>();

	public Explorer() {
		Tab tab = new Tab("Templates");
		tab.setClosable(false);

		TreeView<Object> treeView = new TreeView<Object>(templates);
		treeView.setContextMenu(addMenu(treeView));
		treeView.setShowRoot(false);
		tab.setContent(treeView);
		getTabs().add(tab);
		load();
	}

	public static void load() {
		try {
			FileInputStream fileIn = new FileInputStream("data/templates.bin");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			List<TemplateModel> templateModels = (ArrayList) in.readObject();
			in.close();
			fileIn.close();
			if (templateModels != null) {
				templateModels.forEach(templateModel -> {
					templateModel.load();
					TemplateItem templateItem = new TemplateItem(templateModel);
					templates.getChildren().add(templateItem);
				});
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
///
	public static void saveData() {
		try {
			File file = new File("data/templates.bin");
			file.getParentFile().mkdirs();
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			List<TemplateModel> templateModels = new ArrayList<>();
			templates.getChildren().forEach(templateItem -> {
				templateModels.add((TemplateModel) templateItem.getValue());
			});
			out.writeObject(templateModels);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static void addTemplate() {

		try {
			TemplateModel templateModel = new TemplateModel();

			File templateFile = new File("data/template.rtf");
			templateFile.getParentFile().mkdirs();
			int increment = 1;
			while (templateFile.exists()) {
				templateFile = new File("data/template" + "(" + increment + ")" + ".rtf");
				increment++;
			}
			FileOutputStream fileOutputStream = new FileOutputStream(templateFile);
			fileOutputStream.close();
			templateModel.setTemplateFile(templateFile);
			templateModel.setColumnModels(new ArrayList<>());
			for (char c = 'A'; c <= 'Z'; c++) {
				ColumnModel columnModel = new ColumnModel(c + "");
				templateModel.getColumnModels().add(columnModel);
			}
			Desktop desktop = Desktop.getDesktop();
			desktop.open(templateModel.getTemplateFile());
			TemplateItem templateItem = new TemplateItem(templateModel);
			templates.getChildren().add(templateItem);
			Explorer.saveData();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static void deleteTemplate(TreeItem<Object> treeItem) {
		templates.getChildren().remove(treeItem);
		Explorer.saveData();
	}

	public static void editTemplate(TreeItem<Object> treeItem) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(((TemplateModel) treeItem.getValue()).getTemplateFile());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private ContextMenu addMenu(TreeView<Object> treeView) {
		MenuItem newTemplate = new MenuItem("New");
		newTemplate.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newTemplate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Explorer.addTemplate();
			}
		});

		MenuItem openTemplate = new MenuItem("Open Template");
		openTemplate.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		openTemplate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				treeView.getSelectionModel().getSelectedItems().forEach(item -> {
					((TemplateItem) item).open();
				});

			}
		});

		MenuItem editTemplate = new MenuItem("Edit");

		editTemplate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				editTemplate(treeView.getSelectionModel().getSelectedItem());
			}
		});

		MenuItem deleteTemplate = new MenuItem("Delete");
		deleteTemplate.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		deleteTemplate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteTemplate(treeView.getSelectionModel().getSelectedItem());
			}
		});

		return (new ContextMenu(newTemplate, openTemplate, editTemplate, deleteTemplate));
	}
}
