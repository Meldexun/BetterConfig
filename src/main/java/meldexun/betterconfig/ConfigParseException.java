package meldexun.betterconfig;

@SuppressWarnings("serial")
public class ConfigParseException extends Exception {

	public ConfigParseException() {
		super();
	}

	public ConfigParseException(Throwable cause) {
		super(cause);
	}

	public ConfigParseException(String message) {
		super(message);
	}

	public ConfigParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
