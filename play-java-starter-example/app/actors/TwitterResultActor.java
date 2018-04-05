package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.AbstractActor.Receive;
import akka.event.LoggingAdapter;
import play.libs.Json;
import twitter.Twitter;
import twitter4j.QueryResult;
import twitter4j.TwitterException;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import static akka.pattern.PatternsCS.ask;



public class TwitterResultActor extends AbstractActor{
	
	public final String key ;
	
    public static Props props(String key) {
        return Props.create(TwitterResultActor.class ,key ) ;
    }
    	        
    public TwitterResultActor (String key) {
     	
        this.key = key;

    }
    
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
   		    
	   
	    @Override
	    public Receive createReceive() {
	        return receiveBuilder()
	                .matchAny(key -> {
	                	List<String> res =    new ArrayList<>();
	                	Twitter tweet = new Twitter("got");
	                	CompletionStage<QueryResult> SearchResults = tweet.get() ;		
	                	
	                	
	                	SearchResults.thenAccept( r -> r.getTweets()
	    						.stream()
	    		  				.map(d -> "@" + d.getUser().getScreenName())
	    		  				.limit(10)
	    		  				.forEach(res::add));
	                			
	                	
	                	
	                	sender().tell(res,getSelf());	                	
	                }).build();
   }
	    
}

