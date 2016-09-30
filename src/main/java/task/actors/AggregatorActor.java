package task.actors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.entity.IdAmount;
import task.entity.Result;

public class AggregatorActor extends UntypedActor {

	private final Map<Integer, Double> data = new HashMap<>();
	private final ActorRef writeToFileActor;

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof IdAmount) {
			if (!data.containsKey(((IdAmount) message).getId())) {
				data.put(((IdAmount) message).getId(), ((IdAmount) message).getAmount());
			} else {
				double newAmount = data.get(((IdAmount) message).getId()) + ((IdAmount) message).getAmount();
				data.put(((IdAmount) message).getId(), newAmount);
			}
		} else if (message instanceof EndOfFile) {
			// send result to file writer actor
			writeToFileActor.tell(new Result(Collections.unmodifiableMap(data)), getSelf());
		} else {
			unhandled(message);
		}

	}

	public AggregatorActor(ActorRef writeToFileActor) {
		this.writeToFileActor = writeToFileActor;
	}

	public static Props props(ActorRef writeToFileActor) {
		return Props.create(AggregatorActor.class, writeToFileActor);
	}

}
