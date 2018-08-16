package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.TraversalListener;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fitnesse.plugin.graph.FitnesseWillLoadMe.traverseAllPage;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.LITERAL;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/6.
 */
public class FitSearchJsonResponder implements Responder {

  /**
   * link  to fitnesse.responders.refactoring.RefactorPageResponder
   * filter Fitnesse Root
   * need cache
   *
   * @param
   * @param
   * @return
   */
  static Map<String, List> CACHED = new HashMap();
  static long LAST_TIME_SPEND = 0;

  List<String> searchPageNamesContents(final WikiPage thisPage, String searchString) {
    final List<String> pageNames = new ArrayList();
    if (thisPage != null) {
      Pattern regularExpression = Pattern.compile(searchString, CASE_INSENSITIVE + LITERAL);

      traverseAllPage(thisPage, new TraversalListener<WikiPage>() {
        @Override
        public void process(WikiPage page) {
          String pageContent = page.getData().getContent();
          WikiPagePath pagePath = page.getPageCrawler().getFullPath();
          pagePath.makeAbsolute();
          String pageName = pagePath.toString();
          Matcher matcher = regularExpression.matcher(pageContent);
          if (regularExpression.matcher(pageName).find()) {
            pageNames.add(pageName);
          } else if (matcher.find()) {
            pageNames.add(pageName);
          }
        }
      });
    }
    return pageNames;
  }


  static Thread runningThread = null;

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");
    String searchString = request.getInput("searchString");
    String key = request.getResource() + ":" + searchString;
    List arr;
    if (CACHED.get(key) != null) {
      arr = CACHED.get(key);
      if (runningThread == null || !runningThread.isAlive()) {
        runningThread = new Thread(new Runnable() {
          @Override
          public void run() {
            CACHED.put(key, searchPageNamesContents(context.getRootPage(), searchString));
          }
        });
        runningThread.start();
      }
    } else {
      long stime = System.currentTimeMillis();
      arr = searchPageNamesContents(context.getRootPage(), searchString);
      long etime = System.currentTimeMillis();

      LAST_TIME_SPEND = etime - stime;
      System.out.println("last Thread time spend:  " + LAST_TIME_SPEND);
      CACHED.put(key, arr);
    }

    JSONArray jsonArray = new JSONArray(arr);
    response.setContent(jsonArray.toString());
    return response;
  }

}
