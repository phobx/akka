package example;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

public class Test {

	public static void main(String[] args) {

		ActorSystem system = ActorSystem.create("helloakka");
		ActorSystem system2 = ActorSystem.create("helloakka");

		system.actorSelection("");
		ActorSelection as;

	}

}
