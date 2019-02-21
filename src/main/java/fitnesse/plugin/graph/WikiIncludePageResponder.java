package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

import fitnesse.testrunner.TestPageWithSuiteSetUpAndTearDown;
import fitnesse.wiki.*;

import java.io.UnsupportedEncodingException;

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

    String wikiFullContent=getFullContent(context,newContent,request.getResource());

    return textResponse(wikiFullContent);
  }

  static Response textResponse(String text) throws UnsupportedEncodingException {
    SimpleResponse response = new SimpleResponse();

    response.setContent(text);

    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Content-Type", "text/plain;charset=UTF-8");

    return response;
  }

  public static String getFullContent(FitNesseContext context,String src,String oriPath) {
    if (src.indexOf("!include") == -1) return src;
    StringBuilder stringBuilder = new StringBuilder();
    for (String lines : src.split("\n")) {

      if (lines.startsWith("!include")) {
        String[] arg = lines.trim().split(" ");
        String path = arg[arg.length - 1];
        String out=loadContentByPath(context,path,oriPath);
        stringBuilder.append(out).append("\n");
      }else{
        stringBuilder.append(lines).append("\n");
      }
    }
    return stringBuilder.toString();
  }

  public static String loadContentByPath(FitNesseContext context,String pathString,String oriPath) {
    try{
      WikiPagePath path = PathParser.parse(pathString);
      WikiPagePath pathOri = PathParser.parse(oriPath);
      if(path.isSubPagePath()){
        path=pathOri.append(path);
      }else if(path.isRelativePath()){
        pathOri.removeNameFromEnd();
        path=pathOri.append(path);
      }else if(path.isBackwardSearchPath()){
        //        pathOri.removeNameFromEnd();
        //        pathOri.removeNameFromEnd();
        //        path=pathOri.append(path);
      }

      WikiPage currentPage = context.getRootPage().getPageCrawler().getPage(path);
      //getPage(path);
      return getFullContent(context,currentPage.getData().getContent(),path.toString());
    }catch (Exception e){
      return " -- "+e.getMessage()+" -- "+pathString;
    }
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
