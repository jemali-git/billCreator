package billCreator.gui.view.explorer.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import billCreator.gui.view.editor.model.ColumnModel;
import billCreator.gui.view.editor.model.RowModel;

public class TemplateModel implements Serializable {
	File templateFile;
	List<RowModel> rowModels;
	List<ColumnModel> columnModels;

	public void load() {
		if (rowModels != null) {
			rowModels.forEach(rowModel -> {
				rowModel.load();
			});
		}
		if (columnModels != null) {
			columnModels.forEach(columnModel -> {
				columnModel.load();
			});
		}
	}

	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

	public List<RowModel> getRowModels() {
		return rowModels;
	}

	public void setRowModels(List<RowModel> rowModels) {
		this.rowModels = rowModels;
	}

	public List<ColumnModel> getColumnModels() {
		return columnModels;
	}

	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}

	@Override
	public String toString() {
		return templateFile.getName();
	}

}
