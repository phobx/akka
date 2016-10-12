package task.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.entity.Result;

public class ParserAggregatorActor extends UntypedActor {

	private final String key;
	private double value = 0;

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			value += Double.parseDouble((String) message);
		} else if (message instanceof EndOfFile) {
			// send result to file writer actor
			getSender().tell(new Result(key, value), getSelf());
			System.out.println("Aggregator terminated. Key: " + key + ". Value: " + value);
			getContext().stop(getSelf());
		} else {
			unhandled(message);
		}

	}

	public ParserAggregatorActor(String key) {
		super();
		this.key = key;
		System.out.println("New aggregator created. Key: " + key);
	}

	public static Props props(String key) {
		return Props.create(ParserAggregatorActor.class, key);
	}

}
