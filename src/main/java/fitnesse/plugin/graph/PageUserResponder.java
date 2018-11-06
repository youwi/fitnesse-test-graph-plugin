package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.FitNesseExpediter;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.util.BeanUtil;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import static fitnesse.util.BeanUtil.getObjectByPath;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFIED;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFYING_USER;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class PageUserResponder implements Responder {
  /**
   * 管理webdriver的Session状态
   * 1, user-->sessionId-->preivew
   * 管理截图和用户开关状态
   * 功能:
   * 1,关闭Driver
   * 2,实时截图
   * 3,查看用户
   */
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    String resource = request.getResource();

    WikiPagePath path = PathParser.parse(resource);
    WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
    PageData pageData = page.getData();


    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");

    Map obj = new HashMap<>();
    obj.put("username", pageData.getAttribute(LAST_MODIFYING_USER));
    obj.put("dateString", pageData.getAttribute(LAST_MODIFIED));
    obj.put("loginName", request.getAuthorizationUsername());
    try {
      Object socket = BeanUtil.getObjectByPath(request, "input.input.in.socket");
      if (socket instanceof Socket) {
        InetSocketAddress clientIp = (InetSocketAddress) ((Socket) socket).getRemoteSocketAddress();

        obj.put("clientIp", clientIp.getAddress());
      }
    } catch (Exception e) {

    }


    JSONObject jsonArray = new JSONObject(obj);
    response.setContent(jsonArray.toString());
    return response;
  }

}
