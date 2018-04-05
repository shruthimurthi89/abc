import akka.actor.ActorSystem;
import controllers.HomeController;
import org.junit.Test;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import twitter4j.TwitterException;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.contentAsString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import org.junit.Test;

import play.twirl.api.Content;


/**
 * Unit testing does not require Play application start up.
 *
 * https://www.playframework.com/documentation/latest/JavaTest
 */
public class UnitTest {
    

    // Unit test a controller
    @Test
    public void testSearch() throws TwitterException , InterruptedException ,ExecutionException {
      Result result = new HomeController().Search();
      assertEquals(OK, result.status());
      assertEquals("text/html", result.contentType().get());
      assertEquals("utf-8", result.charset().get());
      assertThat(contentAsString(result)).contains("Tweet Analytics");

    }
    
    @Test
    public void testFirst() throws TwitterException , InterruptedException ,ExecutionException {
      Result result = new HomeController().SearchResults("got");
      assertEquals(OK, result.status());
      assertEquals("text/html", result.contentType().get());
      assertEquals("utf-8", result.charset().get());
      assertThat(contentAsString(result)).contains("got");
    }
    
    @Test
    public void testprofile() throws TwitterException , InterruptedException ,ExecutionException {
        Result result = new HomeController().profile("@marnuell");
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertThat(contentAsString(result)).contains("marnuell");
       // Content html = views.html.pro.render();
       // assertThat(html.body()).contains("Your new application is ready.");
      }
  


}
