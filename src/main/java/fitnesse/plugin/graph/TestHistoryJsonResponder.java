package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.html.template.HtmlPage;
import fitnesse.html.template.PageTitle;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.PageHistory;
import fitnesse.reporting.history.TestHistory;
import fitnesse.util.JSONObjectEx;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import org.json.JSONObject;

import java.io.File;

import static fitnesse.util.BeanUtil.objectToJson;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestHistoryJsonResponder implements Responder{
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    //context

    WikiPage rootPage = context.getRootPage(request.getMap());
    String format = request.getInput("format");

    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");


    File resultsDirectory = context.getTestHistoryDirectory();
    String pageName = request.getResource();
    TestHistory history = new TestHistory(resultsDirectory, pageName);
    PageHistory pageHistory = history.getPageHistory(pageName);
    if(pageHistory==null){
      response.setContent("{}");
    }else{
      response.setContent( objectToJson(pageHistory));
    }

    return response;
  }


}
