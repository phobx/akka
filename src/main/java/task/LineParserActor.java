package task;

import akka.actor.UntypedActor;
import task.util.Parser;

public class LineParserActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			getSender().tell(Parser.getIdAmount((String) message), getSelf());
		} else {
			unhandled(message);
		}

	}

}
