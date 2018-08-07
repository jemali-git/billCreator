package billCreator.gui.view.editor.dataView;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.jce.provider.WrapCipherSpi.RFC3211DESedeWrap;

import billCreator.gui.WorkBenchWindow;
import billCreator.gui.view.editor.model.CellModel;
import billCreator.gui.view.editor.model.ColumnModel;
import billCreator.gui.view.editor.model.RowModel;
import billCreator.gui.view.explorer.Explorer;
import billCreator.gui.view.explorer.model.TemplateModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

public class BCTableView extends TableView<RowModel> {

	TemplateModel templateModel;

	public BCTableView(TemplateModel templateModel) {

		addMenu();
		setEditable(true);
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		if (templateModel != null) {
			this.templateModel = templateModel;
			if (templateModel.getColumnModels() != null) {
				templateModel.getColumnModels().forEach(columnModel -> {
					BCTableColumn bCTableColumn = new BCTableColumn(columnModel);
					getColumns().add(bCTableColumn);
				});
			}
			if (templateModel.getRowModels() != null) {
				templateModel.getRowModels().forEach(rowModel -> {
					getItems().add(rowModel);
				});
			}
		}
	}

	public void addRow() {
		RowModel rowModel = new RowModel();

		getItems().forEach(rModel -> {
			if (rModel.getOrder() >= rowModel.getOrder()) {
				rowModel.setOrder(rModel.getOrder() + 1);
			}
		});

		getColumns().forEach(column -> {
			ColumnModel columnModel = ((BCTableColumn) column).getColumnModel();
			rowModel.getValues().put(columnModel.getId(), new CellModel());
		});
		getColumns().forEach(column -> {
			ColumnModel columnModel = ((BCTableColumn) column).getColumnModel();
			// TODO add in constructor
			rowModel.getValues().get(columnModel.getId()).create(rowModel, columnModel);
		});

		getItems().add(rowModel);

		if (templateModel.getRowModels() == null) {
			templateModel.setRowModels(new ArrayList<>());
		}
		templateModel.getRowModels().add(rowModel);
		Explorer.saveData();

	}

	public void deleteRow() {
		getItems().removeAll(getSelectionModel().getSelectedItems());
		getSelectionModel().clearSelection();
		Explorer.saveData();
	}

	public void createDocoment(RowModel rowModel) {

		try {
			Scanner scanner = new Scanner(templateModel.getTemplateFile());
			FileChooser fileChooser = new FileChooser();

			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RTF files (*.rtf)", "*.rtf");
			fileChooser.getExtensionFilters().add(extFilter);

			File file = fileChooser.showSaveDialog(WorkBenchWindow.primaryStage);
			FileOutputStream fileOutputStream = new FileOutputStream(file);

			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

			Pattern pattern = Pattern.compile("<\\%[a-zA-Z_ ]+\\%>");
			while (scanner.hasNextLine()) {

				String expression = scanner.nextLine();

				Matcher matcher = pattern.matcher(expression);
				StringBuilder stringBuilder = new StringBuilder();
				int start = 0;
				while (matcher.find()) {
					String value = "";
					String refDoc = matcher.group().replaceAll("<\\%|\\%>|\\s+", "");

					for (CellModel cellModel : rowModel.getValues().values()) {
						if (cellModel.getColumnModel() != null) {
							String refTable = cellModel.getColumnModel().getReference().get();
							if (refTable.equals(refDoc)) {
								value = cellModel.getValue().get();
								break;
							}
						}
					}
					stringBuilder.append(expression.substring(start, matcher.end()).replace(matcher.group(), value));
					start = matcher.end();
				}
				stringBuilder.append(expression.substring(start, expression.length()));
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.newLine();
			}
			scanner.close();
			bufferedWriter.close();
			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ConvertToPDF(File docFile) {
		try {
			InputStream doc = new FileInputStream(docFile);
			XWPFDocument document = new XWPFDocument(doc);
			PdfOptions options = PdfOptions.create();
			//
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().add(extFilter);

			OutputStream out = new FileOutputStream(fileChooser.showSaveDialog(WorkBenchWindow.primaryStage));
			PdfConverter.getInstance().convert(document, out, options);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void addMenu() {
		MenuItem newRow = new MenuItem("New");
		newRow.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newRow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addRow();
			}
		});
		MenuItem getPdf = new MenuItem("Get Pdf");
		getPdf.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createDocoment(getSelectionModel().getSelectedItem());
			}
		});
		MenuItem deleteRow = new MenuItem("Delete");
		deleteRow.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		deleteRow.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				deleteRow();
			}
		});
		setContextMenu(new ContextMenu(newRow, getPdf, deleteRow));
	}

}
