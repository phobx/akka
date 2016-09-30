package task.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import task.entity.EndOfFile;
import task.util.Parser;

public class LineParserActor extends UntypedActor {

	private final ActorRef aggregatorActor;

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			aggregatorActor.tell(Parser.getIdAmount((String) message), getSelf());
		} else if (message instanceof EndOfFile) {
			aggregatorActor.tell(message, getSelf());
		} else {
			unhandled(message);
		}

	}

	public LineParserActor(ActorRef aggregatorActor) {
		super();
		this.aggregatorActor = aggregatorActor;
	}

	public static Props props(ActorRef aggregatorActor) {
		return Props.create(LineParserActor.class, aggregatorActor);
	}

}
