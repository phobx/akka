package task.actors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.entity.Result;
import task.util.GenerateFile;

public class MasterActor extends UntypedActor {

	// size of aggregator pool
	private int stoppedAggregators = 0;
	// final result
	private final Map<String, Double> data = new HashMap<>();
	private final Map<String, ActorRef> aggregatorPool = new HashMap<>();

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			String[] splitted = ((String) message).split(";");
			String id = splitted[0].trim();
			String stringAmount = splitted[1].trim();

			ActorRef aggregatorRouter = aggregatorPool.get(id);
			if (aggregatorRouter == null) {
				ActorRef newAggregatorActor = getContext().actorOf(ParserAggregatorActor.props(id));
				newAggregatorActor.tell(stringAmount, getSelf());
				aggregatorPool.put(id, newAggregatorActor);
			} else {
				aggregatorRouter.tell(stringAmount, getSelf());
			}

		} else if (message instanceof Result) {
			// gather all results
			data.put(((Result) message).getKey(), ((Result) message).getValue());
			stoppedAggregators++;
			if (stoppedAggregators == getPoolSize()) {
				// and write to file
				writeToFile();
			}
		} else if (message instanceof EndOfFile) {
			// terminate all aggregators
			aggregatorPool.forEach((id, actor) -> {
				actor.tell(message, getSelf());
			});
		} else {
			unhandled(message);
		}

	}

	private void writeToFile() {
		System.out.println("Writing to file started.");

		data.forEach((id, amount) -> {
			try {
				Files.write(Paths.get(GenerateFile.DESTINATION_FILENAME), idAndAmountToBytes(id, amount),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			} catch (IOException e) {
				System.out.println("Failed to write to file.");
			}
		});
		System.out.println("Writing to file finished.");
		getContext().system().terminate();
	}

	private byte[] idAndAmountToBytes(String id, Double amount) {
		return new StringBuilder().append(id).append(';').append(amount).append("\r\n").toString().getBytes();
	}

	private int getPoolSize() {
		return aggregatorPool.size();
	}

	public static Props props() {
		return Props.create(MasterActor.class);
	}

}
