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
	private boolean isBlankOrComment;

	ConfigReader(BufferedReader reader) {
		this.reader = reader;
	}

	private static boolean isBlankOrComment(String line) {
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (!Character.isWhitespace(c)) {
				return c == '#' || c == '~';
			}
		}
		return true;
	}

	String peekLine() throws IOException {
		if (this.currentLine == null || this.isBlankOrComment) {
			this.currentLine = this.nextLine();
			this.isBlankOrComment = false;
		}
		return this.currentLine;
	}

	String readLine() throws IOException {
		String line = this.peekLine();
		this.currentLine = null;
		return line;
	}

	String peekRawLine() throws IOException {
		if (this.currentLine == null) {
			this.currentLine = this.nextRawLine();
			this.isBlankOrComment = isBlankOrComment(this.currentLine);
		}
		return this.currentLine;
	}

	String readRawLine() throws IOException {
		String line = this.peekRawLine();
		this.currentLine = null;
		return line;
	}

	private String nextLine() throws IOException {
		String line;
		do {
			line = this.nextRawLine();
		} while (isBlankOrComment(line));
		return StringUtils.stripStart(line, null);
	}

	private String nextRawLine() throws IOException {
		String line = this.reader.readLine();
		if (line == null) {
			throw new EOFException();
		}
		this.lineNumber++;
		return line;
	}

	boolean readLineIfEqual(String s) throws IOException {
		return this.readLineIfMatching(s::equals);
	}

	boolean readLineIfMatching(Predicate<String> predicate) throws IOException {
		if (!predicate.test(this.peekLine())) {
			return false;
		}
		this.readLine();
		return true;
	}

	@Nullable
	Matcher readMatching(Pattern pattern) throws IOException {
		Matcher matcher = pattern.matcher(this.peekLine());
		if (matcher.lookingAt()) {
			this.currentLine = this.currentLine.substring(matcher.end());
			return matcher;
		}
		return null;
	}

	@Nullable
	Matcher readRawMatching(Pattern pattern) throws IOException {
		Matcher matcher = pattern.matcher(this.peekRawLine());
		if (matcher.lookingAt()) {
			this.currentLine = this.currentLine.substring(matcher.end());
			return matcher;
		}
		return null;
	}

	void stripStart(@Nullable String stripChars) throws IOException {
		this.currentLine = StringUtils.stripStart(this.peekLine(), stripChars);
	}

	void stripStartRaw(@Nullable String stripChars) throws IOException {
		this.currentLine = StringUtils.stripStart(this.peekRawLine(), stripChars);
	}

	boolean hasRawNext() throws IOException {
		try {
			this.peekRawLine();
			return true;
		} catch (EOFException e) {
			return false;
		}
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
