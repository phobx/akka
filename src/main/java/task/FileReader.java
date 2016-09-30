package task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import task.actors.AggregatorActor;
import task.actors.LineParserActor;
import task.actors.WriteToFileActor;
import task.entity.EndOfFile;
import task.util.GenerateFile;

public class FileReader {

	static final Map<Integer, Double> data = new ConcurrentHashMap<>();

	public static void main(String[] args) {

		final ActorSystem actorSystem = ActorSystem.create("test");
		final ActorRef writeToFileActor = actorSystem.actorOf(WriteToFileActor.props(), "writeToFileActor");
		final ActorRef aggregatorActor = actorSystem.actorOf(AggregatorActor.props(writeToFileActor), "aggregatorActor");
		final ActorRef lineParserActor = actorSystem.actorOf(LineParserActor.props(aggregatorActor), "lineParserActor");

		try (Stream<String> lines = Files.lines(Paths.get(GenerateFile.SOURCE_FILENAME))) {
			lines.forEach(x -> lineParserActor.tell(x, ActorRef.noSender()));
			lineParserActor.tell(new EndOfFile(), ActorRef.noSender());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
