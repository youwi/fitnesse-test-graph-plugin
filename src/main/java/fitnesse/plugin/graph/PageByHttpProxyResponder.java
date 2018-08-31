package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.util.http.Get;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageFactory;
import fitnesse.wikitext.parser.VariableSource;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class PageByHttpProxyResponder implements Responder {


  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    SimpleResponse response = new SimpleResponse();
    String targetUrl = request.getResource().replace("proxy/", "");
    if(targetUrl.startsWith("api")){
      targetUrl="www.lieluobo.testing/"+targetUrl;
    }
    Get get = new Get("http://"+targetUrl, 5000, 5000);
    /**
     * 由于脚本控制直接跳转到首页.代转发无法实现完全代理.
     * 需要完全代理很复杂.
     */
    response.setContent(get.text());
    return response;
  }
}
