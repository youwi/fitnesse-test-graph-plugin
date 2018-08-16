package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.html.template.HtmlPage;
import fitnesse.html.template.PageTitle;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.PageHistory;
import fitnesse.reporting.history.TestHistory;
import fitnesse.responders.NotFoundResponder;
import fitnesse.wiki.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import static fitnesse.util.BeanUtil.objectToJson;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestHistoryGraphResponder implements Responder {
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    SimpleResponse response = new SimpleResponse();
    String resource = request.getResource();
    WikiPagePath path = PathParser.parse(resource);
    WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
    if (page == null)
      return new NotFoundResponder().makeResponse(context, request);

    PageData pageData = page.getData();
    File resultsDirectory = context.getTestHistoryDirectory();
    TestHistory history = new TestHistory(resultsDirectory, request.getResource());

    HtmlPage html = context.pageFactory.newPage();
    html.setTitle("Version Selection: " + resource);
    html.setPageTitle(new PageTitle("Test History Graph", PathParser.parse(resource), pageData.getAttribute(PageData.PropertySUITES)));

    String pageName = request.getResource();
    PageHistory pageHistory = history.getPageHistory(pageName);
    if (pageHistory == null) {
      response.setContent("{}");
    } else {
      html.put("TestHistoryString", objectToJson(pageHistory));
    }
    html.setNavTemplate("viewNav");
    html.put("viewLocation", request.getResource());
    html.setMainTemplate("testHistoryGraph");

    response.setContent(html.html());
    return response;
  }
}
