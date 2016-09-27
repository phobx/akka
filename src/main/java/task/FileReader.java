package task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class FileReader {

	static final Map<Integer, Double> data = new ConcurrentHashMap<>();

	public static void main(String[] args) {

		final ActorSystem actorSystem = ActorSystem.create("test");
		final ActorRef lineParserActor = actorSystem.actorOf(Props.create(LineParserActor.class), "lineParserActor");

		try (Stream<String> lines = Files.lines(Paths.get(GenerateFile.FILENAME))) {
			lines.forEach(x -> lineParserActor.tell(x, ActorRef.noSender()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
