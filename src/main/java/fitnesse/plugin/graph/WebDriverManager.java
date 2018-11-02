package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

import static fitnesse.wiki.WikiPageProperty.LAST_MODIFIED;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFYING_USER;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class WebDriverManager implements Responder {
  /**
   *  管理webdriver的Session状态
   *  1, user-->sessionId-->preivew
   *  管理截图和用户开关状态
   *  功能:
   *  1,关闭Driver
   *  2,实时截图
   *  3,查看用户
   *
   */
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    String resource = request.getResource();

    WikiPagePath path = PathParser.parse(resource);
    WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
    PageData pageData=page.getData();

    return null;
  }

}
