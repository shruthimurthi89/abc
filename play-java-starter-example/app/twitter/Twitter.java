package twitter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This is a new class
 * @author Arvin
 *
 */
public class Twitter {
	String data;
	
	public Twitter(String SearchString) {
		data = SearchString;
	}
		
	/**
	 * This is a method used to search tweets
	 * @return
	 * @throws TwitterException
	 */
	@SuppressWarnings("unchecked")
	public CompletionStage<QueryResult> get() throws TwitterException  {
		// TODO Auto-generated method stub
		CompletableFuture<QueryResult> futureResult = new CompletableFuture<>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
						.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
						.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
						.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
						.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");
		

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getInstance();
		Query query = new Query(data);
	   
		return futureResult.completedFuture(twitter.search(query));
		    
	}
	
	
	public CompletionStage<String>  getProfile() throws TwitterException  {
		// TODO Auto-generated method stub
		
		CompletableFuture<String> futureProfile = new CompletableFuture<>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
						.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
						.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
						.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
						.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getInstance();
	    
		User user = twitter.showUser(data);
	   return	futureProfile.completedFuture(user.getFollowersCount() + " "+ user.getFriendsCount() + " "+ user.getLocation() + " "+ user.getDescription() + " "+ user.getScreenName());		 			    
		    
	}
		
		public CompletableFuture<List<Status>> getDetails() throws TwitterException{
			ConfigurationBuilder cb = new ConfigurationBuilder();
			CompletableFuture<List<Status>> futureStatus = new CompletableFuture<>();
			
			cb.setDebugEnabled(true)
							.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
							.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
							.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
							.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");
			TwitterFactory tf = new TwitterFactory(cb.build());
			
			
			new Thread( () -> {

				try {
					twitter4j.Twitter twitter = tf.getInstance();
					futureStatus.complete(twitter.getUserTimeline(data));
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}).start();
				
			return futureStatus;
			
		}


}
