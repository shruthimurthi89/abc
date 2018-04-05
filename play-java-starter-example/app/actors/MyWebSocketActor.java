package actors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;

import akka.NotUsed;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import play.libs.Json;
import twitter4j.QueryResult;
import static akka.pattern.PatternsCS.ask;
import java.util.stream.Collectors;
import akka.util.Timeout;


public class MyWebSocketActor extends AbstractActor {

	List<String> dummy = new ArrayList<>();
	JsonNode SampleJson = null;

	public static Props props(ActorRef out) {
		return Props.create(MyWebSocketActor.class, out);
	}


	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private final ActorRef out;

	public MyWebSocketActor(ActorRef out) {
		dummy.add("abc");
		dummy.add("bcd");

		SampleJson = Json.toJson(dummy);

		this.out = out;

	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().matchAny(message -> {

			final ActorRef ResultActor = getContext().actorOf(Props.create(TwitterResultActor.class, Json.stringify((JsonNode) message)));
			
			ask(ResultActor , "request" , 1000).thenAccept(s -> out.tell(Json.toJson(s), self()));
			
		}).build();

	}
}