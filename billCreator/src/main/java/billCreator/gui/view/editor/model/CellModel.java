package billCreator.gui.view.editor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;

import billCreator.gui.view.explorer.Explorer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class CellModel implements Serializable {
	private transient SimpleStringProperty value = new SimpleStringProperty("");
	private String valueS;
	private ColumnModel columnModel;
	private RowModel rowModel;

	public void load() {

		value = new SimpleStringProperty(valueS);
		value.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				valueS = value.get();
				Explorer.saveData();
			}
		});
	}

	public void create(RowModel rowModel, ColumnModel columnModel) {
		this.rowModel = rowModel;
		this.columnModel = columnModel;
		valueS = value.get();
		value.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				valueS = value.get();
				Explorer.saveData();
			}
			
		});

		columnModel.getExpressionValue().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				initilizeValue();

			}
		});
		rowModel.getValues().values().forEach(value -> {
			if (!value.equals(CellModel.this)) {
				value.getValue().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						updateValue();
					}
				});
			}
		});
		initilizeValue();
	}

	public SimpleStringProperty getValue() {
		return value;
	}

	public void initilizeValue() {
		if (columnModel != null) {
			if (!columnModel.getExpressionValue().isEmpty().get()) {
				String expression = columnModel.getExpressionValue().get();

				switch (expression) {
				case "$Date::now": {
					this.value.set((new Date()).toLocaleString());
					break;
				}

				case "$Increment::+1": {
					this.value.set(rowModel.getOrder() + "");
					break;
				}
				default:
					updateValue();
					break;

				}
			}
		}
	}

	public void updateValue() {

		if (columnModel != null) {
			if (!columnModel.getExpressionValue().isEmpty().get()) {
				String expression = columnModel.getExpressionValue().get();

				Matcher matcher = billCreator.Main.pattern.matcher(expression);
				StringBuilder stringBuilder = new StringBuilder();
				int start = 0;
				while (matcher.find()) {
					for (CellModel cellModel : rowModel.getValues().values()) {
						if (cellModel.getColumnModel() != null) {
							String ref = cellModel.getColumnModel().getReference().get();
							if (ref.equals(matcher.group())) {
								stringBuilder.append(expression.substring(start, matcher.end()).replace(matcher.group(),
										cellModel.getValue().get()));
								break;
							}
						}

					}
					start = matcher.end();
				}
				stringBuilder.append(expression.substring(start, expression.length()));
				System.out.println(stringBuilder.toString());
				try {
					Object object = billCreator.Main.scriptEngine.eval(stringBuilder.toString());
					if (object != null) {
						this.value.set(object.toString());
					}

				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		}
	}

	public ColumnModel getColumnModel() {
		return columnModel;
	}

	public RowModel getRowModel() {
		return rowModel;
	}
}
