package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import actors.MyWebSocketActor;
import actors.TwitterResultActor;
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.scaladsl.Keep;
import play.mvc.*;
import play.mvc.Http.RequestHeader;
import scala.compat.java8.FutureConverters;
import services.Counter;
import twitter.Twitter;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import views.html.*;
import views.*;

import twitter4j.Query;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import play.libs.streams.ActorFlow;
import play.cache.*;
import play.http.websocket.Message;
import play.libs.F.Either;
import play.libs.concurrent.HttpExecutionContext;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	
    private HttpExecutionContext httpExecutionContext;

    private AsyncCacheApi cache;
    
    private final ActorSystem actorSystem;
    private final Materializer materializer;

/*    @Inject
    public HomeController(AsyncCacheApi cache) {
        this.cache = cache;
    }
    */
    @Inject
   public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }


  /*  @Inject
    public HomeController(ActorSystem actorSystem , Materializer materializer ) {
    	
        this.actorSystem = actorSystem;
        this.materializer = materializer;

    }*/

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public Result scripts() {
        return ok(script.render());
    }
    
    public Result Search() {
    		
        return ok(mainview.render("Tweet Analytics")); 
       
    }
    
    public CompletionStage<Result> SearchResults(String keys) throws TwitterException , InterruptedException ,
    ExecutionException{
        
    	List<String> links_temp = new ArrayList<String>();
    	List<String> result_temp = new ArrayList<String>();
    	
    	CompletableFuture<ArrayList<String>> res = new CompletableFuture<ArrayList<String>>();

    	Twitter tweet = new Twitter(keys);


    	return (tweet.get()).thenApplyAsync(	 s -> {
    		
    		return ok(viewtweets.render( ((QueryResult) s)
    	    		  .getTweets()
    	    		  .stream()
    	    		  .map(d -> "\t" + d.getText())
    	    		  .limit(10)
    	    		  .collect(Collectors.toList()) , 
    	    		  
    	    		  ((QueryResult) s)
    	    		  .getTweets()
    	    		  .stream()
    	    		  .map(d -> "@" + d.getUser().getScreenName())
    	    		  .limit(10)
    	    		  .collect(Collectors.toList())
    	    		  ));
    		
    	} ,httpExecutionContext.current());
    			   
    }
	
  public Result profile(String name) throws TwitterException ,InterruptedException ,ExecutionException{    	
    	
	    Twitter tweet = new Twitter(name);
	    
  	    CompletableFuture<String> profileInfo = (CompletableFuture<String>) tweet.getProfile();
  	    CompletableFuture<QueryResult> FutureResult = (CompletableFuture<QueryResult>) tweet.get();
  	    
     	String Profile = getProfile(name);
    	List<String> Tweets = getDetails(name);
    	
    	    	
		return ok(profile.render(Profile, Tweets));
	}

    public String getProfile (String Key)throws TwitterException, InterruptedException  {
    	Twitter tweet = new Twitter(Key);
    	
    	CompletableFuture<String> profileInfo = (CompletableFuture<String>) tweet.getProfile();
    	
    	
		try {
			return profileInfo.get() ;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		return null;
    }
    
    public List<String> getDetails (String Key)throws TwitterException, InterruptedException  {
    	
    	Twitter tweet = new Twitter(Key);
    	CompletableFuture<List<Status>> FutureList = (CompletableFuture<List<Status>>) tweet.getDetails();
    	List<String> res = new ArrayList<>();
    	
    	FutureList.thenAccept(s ->  s.stream()
    			                    .map(f -> f.getText())
    			                    .limit(10)
    			                    .forEach(res::add));
    	return res;
    	
     }
    
    
    public WebSocket socket() {
        return  WebSocket.Json.accept(request ->
                ActorFlow.actorRef(MyWebSocketActor::props,
                        actorSystem, materializer));
    }
    


}
