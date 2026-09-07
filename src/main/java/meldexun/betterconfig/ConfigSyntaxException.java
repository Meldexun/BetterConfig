package meldexun.betterconfig;

@SuppressWarnings("serial")
public class ConfigSyntaxException extends ConfigParseException {

	public ConfigSyntaxException() {
		super();
	}

	public ConfigSyntaxException(Throwable cause) {
		super(cause);
	}

	public ConfigSyntaxException(String message) {
		super(message);
	}

	public ConfigSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

}
