package ressa;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Indentation {
	int c;

	public Indentation() {
		this(0);
	}

	public Indentation(int indent) {
		c = indent;
	}

	public void inc() {
		c++;
	}

	public void dec() {
		c--;
	}

	public int getIndentationDepth() {
		return c;
	}

	public String getIndentation() {
		return Stream.generate(() -> "  ").limit(c).collect(Collectors.joining());
	}
}
