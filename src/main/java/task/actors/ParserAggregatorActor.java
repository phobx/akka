package task.actors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import akka.actor.Props;
import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.entity.IdAmount;
import task.entity.Result;
import task.util.Parser;

public class ParserAggregatorActor extends UntypedActor {

	private final Map<Integer, Double> data = new HashMap<>();

	{
		System.out.println("new aggregator created.");
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			IdAmount idAmount = Parser.getIdAmount((String) message);
			if (!data.containsKey(idAmount.getId())) {
				data.put(idAmount.getId(), idAmount.getAmount());
			} else {
				double newAmount = data.get(idAmount.getId()) + idAmount.getAmount();
				data.put(idAmount.getId(), newAmount);
			}
		} else if (message instanceof EndOfFile) {
			// send result to file writer actor
			getSender().tell(new Result(Collections.unmodifiableMap(data)), getSelf());
			System.out.println("aggregator terminated.");
			getContext().stop(getSelf());
		} else {
			unhandled(message);
		}

	}

	public static Props props() {
		return Props.create(ParserAggregatorActor.class);
	}

}
