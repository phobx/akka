package task;

import akka.actor.UntypedActor;

public class LineParserActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			int id = Integer.parseInt(((String) message).split(";")[0].trim());
			double amount = Integer.parseInt(((String) message).split(";")[1].trim());
			getSender().tell(new IdAmount(id, amount), getSelf());
		} else {
			unhandled(message);
		}

	}

}
