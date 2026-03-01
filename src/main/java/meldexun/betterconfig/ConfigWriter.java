package meldexun.betterconfig;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConfigWriter implements AutoCloseable {

	private final BufferedWriter writer;
	private int indentation;
	private boolean lineStarted;

	public ConfigWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public ConfigWriter writeLine(char c) throws IOException {
		return this.write(c).newLine();
	}

	public ConfigWriter writeLine(char c, int count) throws IOException {
		return this.write(c, count).newLine();
	}

	public ConfigWriter writeLine(String s) throws IOException {
		return this.write(s).newLine();
	}

	public ConfigWriter writeCommentLine(String s) throws IOException {
		return this.startComment().writeLine(s);
	}

	public ConfigWriter startComment() throws IOException {
		return this.write("# ");
	}

	public ConfigWriter write(char c) throws IOException {
		this.indent();
		this.writer.write(c);
		return this;
	}

	public ConfigWriter write(char c, int count) throws IOException {
		this.indent();
		for (int i = 0; i < count; i++) {
			this.writer.write(c);
		}
		return this;
	}

	public ConfigWriter write(String s) throws IOException {
		this.indent();
		this.writer.write(s);
		return this;
	}

	private void indent() throws IOException {
		if (!this.lineStarted) {
			for (int i = 0; i < this.indentation * 4; i++) {
				this.writer.write(' ');
			}
			this.lineStarted = true;
		}
	}

	public ConfigWriter newLine() throws IOException {
		this.writer.newLine();
		this.lineStarted = false;
		return this;
	}

	public ConfigWriter incrementIndentation() {
		this.indentation++;
		return this;
	}

	public ConfigWriter decrementIndentation() {
		this.indentation--;
		return this;
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
