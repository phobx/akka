package task.actors;

import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.util.Parser;

public class LineParserActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			getSender().tell(Parser.getIdAmount((String) message), getSelf());
		} else if (message instanceof EndOfFile) {
			getSender().tell(message, getSelf());
		} else {
			unhandled(message);
		}

	}

}
