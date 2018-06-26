package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.PageHistory;
import fitnesse.reporting.history.TestHistory;
import fitnesse.wiki.*;
import org.json.JSONObject;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestHistoryCloneResponder implements Responder {
    /**
     * simplie clone
     *
     * @param context
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        WikiPage rootPage = context.getRootPage(request.getMap());

        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        String resource = request.getResource();

        WikiPagePath path = PathParser.parse(resource);
        WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
        String data = page.getData().getContent();

        try {
            WikiPage childPage = WikiPageUtil.addPage(rootPage, PathParser.parse(resource + "Clone"), data);
            //.setProperties(page.getData().getProperties());
            childPage.commit(page.getData());
            context.recentChanges.updateRecentChanges(childPage);
            response.setContent("{}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent("{error:1,msg:'"+e.getMessage()+"'}");
        }

        return response;
    }
}
