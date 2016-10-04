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
import akka.routing.RoundRobinPool;
import task.entity.EndOfFile;
import task.entity.Result;
import task.util.GenerateFile;

public class MasterActor extends UntypedActor {

	// size of aggregator pool
	public static final int SIZE_OF_POOL = 10;
	private int stoppedAggregators = 0;
	private final ActorRef aggregatorRouter = this.getContext().actorOf(
			ParserAggregatorActor.props().withRouter(new RoundRobinPool(SIZE_OF_POOL)), "aggregator");
	// final result
	private final Map<Integer, Double> data = new HashMap<>();

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			aggregatorRouter.tell(message, getSelf());
		} else if (message instanceof Result) {
			// gather all results
			((Result) message).getResult().forEach((id, amount) -> {
				if (!data.containsKey(id)) {
					data.put(id, amount);
				} else {
					double newAmount = data.get(id) + amount;
					data.put(id, newAmount);
				}
			});
			stoppedAggregators++;
			if (stoppedAggregators == SIZE_OF_POOL) {
				// and write to file
				writeToFile();
			}
		} else if (message instanceof EndOfFile) {
			// terminate all aggregators
			for (int i = 0; i < SIZE_OF_POOL; i++) {
				aggregatorRouter.tell(message, getSelf());
			}

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

	private byte[] idAndAmountToBytes(Integer id, Double amount) {
		return new StringBuilder().append(id).append(';').append(amount).append("\r\n").toString().getBytes();
	}

	public static Props props() {
		return Props.create(MasterActor.class);
	}

}
