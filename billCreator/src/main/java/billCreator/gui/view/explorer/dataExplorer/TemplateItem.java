package billCreator.gui.view.explorer.dataExplorer;

import billCreator.gui.view.KpiPerspective;
import billCreator.gui.view.editor.EditorTab;
import billCreator.gui.view.explorer.dataExplorer.util.Ticket;
import billCreator.gui.view.explorer.model.TemplateModel;
import javafx.scene.control.TreeItem;

public class TemplateItem extends TreeItem<Object> {
	private Ticket ticket;
	TemplateModel templateModel;

	public TemplateItem(Object value) {
		super(value);
		templateModel = (TemplateModel) value;
		ticket = new Ticket("Template");
		setGraphic(ticket);
	}

	public void select() {
		ticket.selectionOn();
	}

	public void deselect() {
		ticket.selectOff();
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void open() {
		KpiPerspective.editor.getTabs().add(new EditorTab(templateModel));
	}

}
