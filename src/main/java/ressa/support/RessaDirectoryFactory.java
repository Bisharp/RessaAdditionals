package ressa.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class RessaDirectoryFactory {

	/**
	 * Run the directory handler from the command line, outputting as JSON to
	 * {@code output} in the working directory.
	 * 
	 * @param args {@code <ignore_list>... // <files>...}
	 * @throws IOException If an IO error occurs
	 */
	public static void main(String[] args) throws IOException {
		IgnoreAndFilelistParser parser = new IgnoreAndFilelistParser(args);
		Collection<Pattern> ignore = parser.getIgnore();
		List<String> dirs = parser.getFiles();

		// Create output
		File f = new File("output");
		if (!f.exists()) {
			f.createNewFile();
		}
		try (var out = Files.newBufferedWriter(f.toPath())) {
			out.write(makeRessaDirectory(dirs, ignore).toJSON());
			out.flush();
		}
	}

	/**
	 * Convert a list of directories into an in-memory recursive structure
	 * 
	 * @param dirs   Directories to handle
	 * @param ignore Files patterns to ignore
	 * @return A RessaDirectory representing the set of dirs, as though they are one file
	 */
	public static RessaDirectory makeRessaDirectory(List<String> dirs, Collection<Pattern> ignore) {
		var result = dirs.stream().map(dir -> handleDirectory(dir, ignore)).filter(dir -> dir != null && dir.isPresent())
				.map(dir -> dir.get()).toList();
		return new RessaDirectory("project", List.of(), result);
	}

	/**
	 * Generate a tree of objects describing the file named.
	 * 
	 * @param name
	 * @param ignore
	 * @return A RessaDirectory if the name describes a directory, and empty
	 *         Optional if it describes a terminal file, or null if the
	 *         file/directory matches the ignore list.
	 */
	public static Optional<RessaDirectory> handleDirectory(String name, Collection<Pattern> ignore) {

		File file = new File(name);
		final String path = file.getAbsolutePath().replace('\\', '/');

		// Verify file not in ignore list (null == invalid)
		if (ignore.stream().anyMatch(patt -> patt.matcher(path).find())) {
			return null;
		}

		// Verify not terminal (empty == file not directory)
		if (!file.isDirectory()) {
			return Optional.empty();
		}

		// Set up structures
		List<String> files = new LinkedList<>();
		List<RessaDirectory> subFiles = new LinkedList<>();
		for (var sub : file.list()) {
			// Create copy
			sub = path + "/" + sub;
			Optional<RessaDirectory> req = handleDirectory(sub, ignore);

			// If the file is valid to add, add it to the correct list
			if (req != null) {
				if (req.isPresent()) {
					subFiles.add(req.get());
				} else {
					files.add(sub);
				}
			}
		}

		if (files.size() > 0 || subFiles.size() > 0) {
			return Optional.of(new RessaDirectory(path, files, subFiles));
		} else {
			return null; // Invalid file, because it's an effectively empty directory
		}
	}
}
