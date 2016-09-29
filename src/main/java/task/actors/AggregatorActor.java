package task.actors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.entity.IdAmount;
import task.entity.Result;

public class AggregatorActor extends UntypedActor {

	static final Map<Integer, Double> data = new HashMap<>();

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
			new Result(Collections.unmodifiableMap(data));
		}

	}

}
