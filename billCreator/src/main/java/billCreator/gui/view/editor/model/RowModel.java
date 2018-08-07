package billCreator.gui.view.editor.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RowModel implements Serializable {

	Map<String, CellModel> values = new HashMap<>();
	int order = 0;

	public void load() {
		values.values().forEach(cellModel -> {
			cellModel.load();
		});
	}

	public Map<String, CellModel> getValues() {
		return values;
	}

	public void setValues(Map<String, CellModel> columnInfoMap) {
		this.values = columnInfoMap;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
