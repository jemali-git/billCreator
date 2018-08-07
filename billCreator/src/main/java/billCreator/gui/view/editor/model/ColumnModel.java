package billCreator.gui.view.editor.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;

import billCreator.gui.view.explorer.Explorer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ColumnModel implements Serializable {
	private String id;

	private transient SimpleStringProperty reference;

	private String referenceS;

	private transient SimpleStringProperty expressionValue;

	private String expressionValueS;

	public ColumnModel(String id) {
		this.id = id;
		reference = new SimpleStringProperty();
		reference.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				referenceS = reference.get();
				Explorer.saveData();
			}
		});
		reference.set(id);
		expressionValue = new SimpleStringProperty();
		expressionValue.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				expressionValueS = expressionValue.get();
				Explorer.saveData();
			}
		});
		expressionValue.set(id);
	}

	public void load() {

		reference = new SimpleStringProperty(referenceS);
		reference.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				referenceS = reference.get();
				Explorer.saveData();
			}
		});
		expressionValue = new SimpleStringProperty(expressionValueS);
		expressionValue.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				expressionValueS = expressionValue.get();
				Explorer.saveData();
			}
		});
	}

	public SimpleStringProperty getReference() {
		return reference;
	}

	public void setReference(SimpleStringProperty reference) {
		this.reference = reference;
	}

	public SimpleStringProperty getExpressionValue() {
		return expressionValue;
	}

	public void setExpressionValue(SimpleStringProperty expressionValue) {
		this.expressionValue = expressionValue;
	}

	public String getId() {
		return id;
	}

}
