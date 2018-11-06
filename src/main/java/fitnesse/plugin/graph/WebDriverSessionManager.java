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

import java.util.HashMap;
import java.util.Map;

import static fitnesse.wiki.WikiPageProperty.LAST_MODIFIED;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFYING_USER;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class WebDriverSessionManager implements Responder {

  static Map<String,String> UserSessionIdMap=new HashMap<>();

  /**
   *  管理webdriver的Session状态
   *  1, user-->sessionId-->preivew
   *  管理截图和用户开关状态
   *  功能:
   *  1,关闭Driver
   *  2,实时截图
   *  3,查看用户
   *  4,绑定用户
   *  5,入库
   *  6,清理所有浏览器
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


  public void register(String userName,String sessionId){


    UserSessionIdMap.put(userName,sessionId);
  }

  public String vv(){
    //
    return "";
  }

  public void clearAllSession(){
    //TODO
  }
  public void clearByName(String userName){
    UserSessionIdMap.remove(userName);

  }

  /**
   * 检查id还有没有效果,并检查是否被别人占用.
   * 超时2s.如果被
   * @param sessionId sessionid
   * @return
   */
  public boolean isAlive(String sessionId){

    return  true;

  }
}
