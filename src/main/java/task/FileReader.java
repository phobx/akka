package task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import task.actors.MasterActor;
import task.entity.EndOfFile;
import task.util.GenerateFile;

public class FileReader {

	public static void main(String[] args) {

		final ActorSystem actorSystem = ActorSystem.create("test");
		final ActorRef master = actorSystem.actorOf(MasterActor.props(), "master");

		try (Stream<String> lines = Files.lines(Paths.get(GenerateFile.SOURCE_FILENAME))) {
			// read each line and send it to main LineParser actor
			lines.forEach(x -> master.tell(x, ActorRef.noSender()));
			// tell about end of file
			master.tell(new EndOfFile(), ActorRef.noSender());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
