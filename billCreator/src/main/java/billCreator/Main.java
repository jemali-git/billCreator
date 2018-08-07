package billCreator;

import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import billCreator.gui.WorkBenchWindow;

public class Main {
	public static Pattern pattern = Pattern.compile("[a-zA-Z_]+");

	public static ScriptEngine scriptEngine;

	public static void main(String[] args) {
		Main.scriptEngine = (new ScriptEngineManager()).getEngineByName("JavaScript");
		WorkBenchWindow.init(args);
	}
}
