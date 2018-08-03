package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.editing.EditResponder;
import fitnesse.responders.editing.SaveRecorder;
import fitnesse.wiki.*;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestWithContentResponder implements Responder {
  /**
   * use body
   * to run test now
   * 由于使用进程进行测试需要很多时间故,使用一个直接程序加速测试过程.
   * 1, 获取 上下文,
   * 2, 解析为html
   * 3, 传给Fixture
   * 4, 获取html报告
   * 不再使用进程架构
   * TODO 估计不能加速多快.
   *
   * @param context Fitnesse框架.
   * @param request Http请求.
   * @return
   * @throws Exception
   */
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {

    String body = request.getBody();

    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");
    String resource = request.getResource();

    WikiPagePath path = PathParser.parse(resource);
    WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
    PageData pageData = page.getData();
    pageData.setContent(body);

    //TODO
    try {
      response.setContent("{}");
    } catch (Exception e) {
      e.printStackTrace();
      response.setContent("{error:1,msg:'" + e.getMessage() + "'}");
    }
    return response;
  }

}
