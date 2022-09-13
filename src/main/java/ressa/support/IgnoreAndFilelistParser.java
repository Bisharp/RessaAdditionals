package ressa.support;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class IgnoreAndFilelistParser {

	/** String splitting the ignore files from the filelist */
	private static final String STOP = "//";

	/** Patterns to ignore */
	private List<Pattern> ignore;

	/** Files to parse */
	private List<String> files;

	/**
	 * Initialize parser from the provided command line arguments
	 * 
	 * @param args CL arguments
	 */
	public IgnoreAndFilelistParser(String[] args) {
		// Verify inputs
		Objects.requireNonNull(args, "args cannot be null");
		if (args.length == 0) {
			throw new RuntimeException("USAGE: (<ignore_patterns> " + STOP + ")? <root_dirs>...");
		}

		// Find all ignores
		int i = 0;
		while (!STOP.equals(args[i])) {
			i++;
			if (i >= args.length) {
				i = 0;
				break;
			}
		}

		// Partition
		ignore = Arrays.stream(Arrays.copyOfRange(args, 0, i)).map(patt -> Pattern.compile(patt)).toList();
		files = List.of(Arrays.copyOfRange(args, i + 1, args.length));
	}
}
