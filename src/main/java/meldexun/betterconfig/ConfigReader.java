package meldexun.betterconfig;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

class ConfigReader implements AutoCloseable {

	private final BufferedReader reader;
	private int lineNumber;
	private String currentLine;

	ConfigReader(BufferedReader reader) {
		this.reader = reader;
	}

	static boolean isBlankOrComment(String line) {
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (!Character.isWhitespace(c)) {
				return c == '#';
			}
		}
		return true;
	}

	static boolean strippedEquals(String line, String s) {
		boolean found = false;
		for (int i = 0; i < line.length();) {
			if (Character.isWhitespace(line.charAt(i))) {
				i++;
				continue;
			}
			if (!found && line.startsWith(s, i)) {
				found = true;
				i += s.length();
				continue;
			}
			return false;
		}
		return true;
	}

	String peekLine() throws IOException {
		if (this.currentLine == null) {
			this.currentLine = this.nextLine();
		}
		return this.currentLine;
	}

	String readLine() throws IOException {
		String line = this.peekLine();
		this.currentLine = null;
		return line;
	}

	private String nextLine() throws IOException {
		String line = this.reader.readLine();
		if (line == null) {
			throw new EOFException();
		}
		this.lineNumber++;
		return line;
	}

	boolean readLineIfMatching(Predicate<String> predicate) throws IOException {
		if (!predicate.test(this.peekLine())) {
			return false;
		}
		this.currentLine = null;
		return true;
	}

	@Nullable
	Matcher readMatching(Pattern pattern) throws IOException {
		Matcher matcher = pattern.matcher(this.peekLine());
		if (!matcher.lookingAt()) {
			return null;
		}
		this.currentLine = this.currentLine.substring(matcher.end());
		return matcher;
	}

	@Nullable
	Matcher readLineMatching(Pattern pattern) throws IOException {
		Matcher matcher = pattern.matcher(this.peekLine());
		if (!matcher.lookingAt()) {
			return null;
		}
		this.currentLine = null;
		return matcher;
	}

	void stripStart(@Nullable String stripChars) throws IOException {
		this.currentLine = StringUtils.stripStart(this.peekLine(), stripChars);
	}

	boolean hasNext() throws IOException {
		try {
			this.peekLine();
			return true;
		} catch (EOFException e) {
			return false;
		}
	}

	int lineNumber() {
		return this.lineNumber;
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

}
