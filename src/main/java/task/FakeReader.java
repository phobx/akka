package task;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import task.actors.MasterActor;
import task.entity.EndOfFile;
import task.util.GenerateFile;

public class FakeReader {

	public static final int NUMBER_OF_LINES = 100_000_000;
	public static final int TIMES_TO_REPEAT = 5;

	public static void main(String[] args) {

		final ActorSystem actorSystem = ActorSystem.create("test");
		final ActorRef master = actorSystem.actorOf(MasterActor.props(), "master");

		for (int j = 0; j < TIMES_TO_REPEAT; j++) {
			long start = System.currentTimeMillis();
			for (int i = 0; i < NUMBER_OF_LINES; i++) {
				StringBuilder sb = new StringBuilder()
						.append(GenerateFile.nextId())
							.append(';')
							.append(GenerateFile.nextAmount());
				// read each line and send it to main LineParser actor
				master.tell(sb.toString(), ActorRef.noSender());

			}
			long end = System.currentTimeMillis();
			System.out.println("Iteration #" + (j + 1) + ". Total time in millis of " + NUMBER_OF_LINES + " lines: "
					+ (end - start));
		}
		// tell about end of file
		master.tell(new EndOfFile(), ActorRef.noSender());

	}
}
