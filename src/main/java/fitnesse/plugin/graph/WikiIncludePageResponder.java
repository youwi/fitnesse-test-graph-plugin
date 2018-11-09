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

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    WikiPagePath path = PathParser.parse(request.getResource());
    WikiPage currentPage = context.getRootPage().getPageCrawler().getPage(path);


    TestPageWithSuiteSetUpAndTearDownEx down = new TestPageWithSuiteSetUpAndTearDownEx(currentPage);
    String newContent = down.getContent();

    ParsingPage parsingPage = BaseWikitextPage.makeParsingPage((BaseWikitextPage) currentPage);
    Symbol syntaxTree = Parser.make(parsingPage, newContent).parse();
    Translator wii = new WikiTranslator(new WikiSourcePage(currentPage));
    String wikiFullContent = wii.translateTree(syntaxTree);


    SimpleResponse response = new SimpleResponse();

    response.setContent(wikiFullContent);

    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Content-Type", "text/plain;charset=UTF-8");


    return response;
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


  static class WikiTranslatorEx extends WikiTranslator {
    SymbolType include = new Include();
    WikiBuilder includeBuilder = new WikiBuilder().text("!include ").child(0);

    public WikiTranslatorEx(SourcePage page) {
      super(page);
    }

    @Override
    protected Translation getTranslation(SymbolType symbolType) {
      if (symbolType.toString().equals(include.toString())) {
        return includeBuilder;
      } else {
        return super.getTranslation(symbolType);
      }
    }
  }

}
