package task.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class GenerateFile {

	public static final String SOURCE_FILENAME = "C:\\file_storage.txt";
	public static final String DESTINATION_FILENAME = "C:\\file_result.txt";

	private static final int NUMBER_OF_LINES = 100_000;
	private static final int ID_MAX_VALUE = 1000;
	private static final Path FILEPATH = Paths.get(SOURCE_FILENAME);
	private static final Random RANDOM = new Random();

	public static int nextId() {
		return RANDOM.nextInt(ID_MAX_VALUE);
	}

	public static double nextAmount() {
		return RANDOM.nextDouble();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Writing started.");

		for (int i = 0; i < NUMBER_OF_LINES; i++) {
			StringBuilder sb = new StringBuilder().append(nextId()).append(';').append(nextAmount()).append("\r\n");
			Files.write(FILEPATH, sb.toString().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		}

		System.out.println("Writing finished.");
	}

}
