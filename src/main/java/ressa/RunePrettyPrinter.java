package ressa;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunePrettyPrinter {

	static class Indentation {
		int c = 0;

		public void inc() {
			c++;
		}

		public void dec() {
			c--;
		}

		public String getIndentation() {
			return Stream.generate(() -> "  ").limit(c).collect(Collectors.joining());
		}
	}

	public static void main(String[] args) {
		try (Scanner scan = new Scanner(System.in)) {
			System.out.println(format(scan.nextLine()));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Format JSON-escaped ReSSA script to readable
	 * 
	 * @param toFormat Line to format readably
	 * @return String formatted in a readable manner
	 */
	public static final String format(String toFormat) {
		StringBuilder bldr = new StringBuilder();
		Indentation ind = new Indentation();

		boolean stripWhitespace = true;
		boolean literal = false;
		boolean escapeInLit = false;

		List<String> lines = new LinkedList<>();
		for (char c : toFormat.replaceAll("\\\\\"", "\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\t", "    ").toCharArray()) {
			// Clear line-leading whitespace
			if (Character.isWhitespace(c) && stripWhitespace) {
				continue;
			}

			// Add and decrease from indentation
			if (!literal) {
				if (c == '{') {
					ind.inc();
				} else if (c == '}') {
					ind.dec();
				}
			}

			// Flip on encountering "
			if ((c == '"' || c == '`') && !escapeInLit) {
				literal = !literal;
			} else if (escapeInLit) {
				escapeInLit = false;
			} else if (literal && c == '\\') {
				escapeInLit = true;
			}

			// Add leading whitespace
			if (stripWhitespace) {
				stripWhitespace = false;
				bldr.append(ind.getIndentation());
			}

			// Add next character
			bldr.append(c);

			// Add line breaks
			if (!literal) {
				if (c == '{' || c == '}' || c == ';') {
					lines.add(bldr.toString());
					bldr.setLength(0);
					stripWhitespace = true;
				}
			}
		}
		
		return lines.stream().collect(Collectors.joining("\n"));
	}
}
