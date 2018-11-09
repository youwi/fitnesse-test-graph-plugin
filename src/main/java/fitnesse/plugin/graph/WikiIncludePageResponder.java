package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

import fitnesse.testrunner.TestPageWithSuiteSetUpAndTearDown;
import fitnesse.wiki.*;
import fitnesse.wikitext.parser.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取当前页面包含的内容的页面
 * 如自动包含的 SetUp的内容,
 * 返回纯文本.
 */
public class WikiIncludePageResponder implements Responder {
  FitNesseContext context;

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    this.context = context;
    WikiPagePath path = PathParser.parse(request.getResource());
    WikiPage currentPage = context.getRootPage().getPageCrawler().getPage(path);


    TestPageWithSuiteSetUpAndTearDownEx down = new TestPageWithSuiteSetUpAndTearDownEx(currentPage);
    String newContent = down.getContent();

    String wikiFullContent=getFullContent(newContent);

    SimpleResponse response = new SimpleResponse();

    response.setContent(wikiFullContent);

    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Content-Type", "text/plain;charset=UTF-8");

    return response;
  }

  public String getFullContent(String src) {
    if (src.indexOf("!include") == -1) return src;
    StringBuilder stringBuilder = new StringBuilder();
    for (String lines : src.split("\n")) {

      if (lines.startsWith("!include")) {
        String[] arg = lines.trim().split(" ");
        String path = arg[arg.length - 1];
        String out=loadContentByPath(path);
        stringBuilder.append(out).append("\n");
      }else{
        stringBuilder.append(lines).append("\n");
      }
    }
    return stringBuilder.toString();
  }

  public String loadContentByPath(String pathString) {
    WikiPagePath path = PathParser.parse(pathString);
    WikiPage currentPage = context.getRootPage().getPageCrawler().getPage(path);
    return getFullContent(currentPage.getData().getContent());
  }


  static class TestPageWithSuiteSetUpAndTearDownEx extends TestPageWithSuiteSetUpAndTearDown {
    public TestPageWithSuiteSetUpAndTearDownEx(WikiPage sourcePage) {
      super(sourcePage);
    }

    @Override
    public String getContent() {
      return getDecoratedContent();
    }
  }


}
