package ressa.support;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Request entry representing the file/directory
 * 
 * @author Micah
 */
public record RessaDirectory(String path, List<String> files, List<RessaDirectory> subDirectories) {
	
	/**
	 * Perform an action on every file
	 * 
	 * @param callback Callback to accept the fully-qualified file paths
	 */
	public void forEachFile(Consumer<String> callback) {
		files.stream().forEach(callback);
		subDirectories.stream().forEach(dir -> dir.forEachFile(callback));
	}

	/**
	 * Convert to a JSON string
	 */
	public String toJSON() {
		if (files.size() == 0 && subDirectories.size() == 0) {
			return "";
		}

		return Stream.of("{", "\"path\": \"%s\",".formatted(escapeForJson(path)), "\"files\": [",
				files.stream().map(file -> "\"%s\"".formatted(escapeForJson(file)))
						.collect(Collectors.joining(", ")),
				"],", "\"subDirectories\": [",
				subDirectories.stream().map(dir -> dir.toJSON()).collect(Collectors.joining(", ")), "]", "}")
				.collect(Collectors.joining());
	}
	
	public String toString() {
		return toJSON();
	}

	private String escapeForJson(String input) {
		return input.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}