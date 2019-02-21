package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

import static fitnesse.plugin.graph.PageVarsResponder.buildVarTagsWikiText;
import static fitnesse.plugin.graph.WikiIncludePageResponder.getFullContent;
import static fitnesse.plugin.graph.WikiIncludePageResponder.textResponse;

/**
 * pluginTestGraph
 * Created by yu on 2019-02-21.
 */
public class WikiIncludeWithVarPageResponder implements Responder {
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        WikiPagePath path = PathParser.parse(request.getResource());
        WikiPage currentPage = context.getRootPage().getPageCrawler().getPage(path);


        WikiIncludePageResponder.TestPageWithSuiteSetUpAndTearDownEx down = new WikiIncludePageResponder.TestPageWithSuiteSetUpAndTearDownEx(currentPage);
        String newContent = down.getContent();

        String wikiFullContentWithVar=buildVarTagsWikiText(context,request.getResource())+

                getFullContent(context,newContent,request.getResource());

        return textResponse(wikiFullContentWithVar);
    }
}
