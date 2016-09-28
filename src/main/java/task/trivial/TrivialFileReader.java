package task.trivial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import task.IdAmount;
import task.util.GenerateFile;
import task.util.Parser;

public class TrivialFileReader {

	static final Map<Integer, Double> data = new ConcurrentHashMap<>();

	public static void main(String[] args) {

		try (Stream<String> lines = Files.lines(Paths.get(GenerateFile.FILENAME))) {
			lines.forEach(x -> {
				IdAmount idAmount = Parser.getIdAmount(x);
				if (!data.containsKey(idAmount.getId())) {
					data.put(idAmount.getId(), idAmount.getAmount());
				} else {
					double newAmount = data.get(idAmount.getId()) + idAmount.getAmount();
					data.put(idAmount.getId(), newAmount);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		data.forEach((id, amount) -> System.out.println("ID: " + id + " Amount: " + amount));

	}

}
