package ressa;

import java.util.Scanner;

public class RuneMinifier {

	public static final String DELIM = "//EOF";

	public static void main(String[] args) {
		try (Scanner scan = new Scanner(System.in)) {
			StringBuilder bldr = new StringBuilder();
			String line;

			while (!DELIM.equals(line = scan.nextLine())) {
				bldr.append(line.replaceAll("\t", " ").replaceAll("  +", " ").replace("\"", "\\\"").replaceAll("\n|\r", ""));
			}
			System.out.println(bldr);
		}
	}
}
