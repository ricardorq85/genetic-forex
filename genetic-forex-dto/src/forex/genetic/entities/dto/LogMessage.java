package forex.genetic.entities.dto;

public class LogMessage {
	private String text;
	private int logLevel;
	private int tabLevel;

	public LogMessage(String text, int logLevel, int tabLevel) {
		super();
		this.text = text;
		this.logLevel = logLevel;
		this.tabLevel = tabLevel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public int getTabLevel() {
		return tabLevel;
	}

	public void setTabLevel(int tabLevel) {
		this.tabLevel = tabLevel;
	}

}
