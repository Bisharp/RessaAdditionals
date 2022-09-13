package ressa;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CallbackExtractor {
	private static final String CLEAN = "\".*\"";

	private static final Pattern START = Pattern.compile(".*\\[");
	private static final Pattern END = Pattern.compile("\\]");
	private static final Pattern CALLBACK = Pattern.compile("\"callback\":\s*\"(.*)\"");

	public static void main(String[] args) {
		try (Scanner scan = new Scanner(System.in)) {
			scan.next(START);

			StringBuilder result = new StringBuilder();
			int indentation = 1;
			while (indentation > 0) {
				indentation += extract(scan.nextLine(), result);
			}
			System.out.println(result);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static final int extract(String toFormat, StringBuilder bldr) {
		String bracketTest = toFormat.replaceAll(CLEAN, "");
		int numBrackets = countMatches(bracketTest, START) - countMatches(bracketTest, END);

		var result = CALLBACK.matcher(toFormat);
		if (result.find()) {
			bldr.append("// <Insert_Description>\n").append(RunePrettyPrinter.format(result.group(1))).append("\n\n");
		}

		return numBrackets;
	}

	private static final int countMatches(String format, Pattern patt) {
		var matcher = patt.matcher(format);
		int c = 0;
		while (matcher.find()) {
			c++;
		}
		return c;
	}
}
