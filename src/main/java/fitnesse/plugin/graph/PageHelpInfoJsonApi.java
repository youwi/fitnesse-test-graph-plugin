package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.TraversalListener;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fitnesse.plugin.graph.FitnesseWillLoadMe.traverseAllPage;

/**
 * pluginTestGraph
 * with page Help info
 * Created by yu on 2018/7/19.
 */
public class PageHelpInfoJsonApi implements Responder {

    // only for RootPage CACHE.
    static Map CACHE = new HashMap();

    List<String> collectPageHelpInfo(WikiPage rootPage) {
        final List<String> pageNames = new ArrayList<>();
        if (rootPage != null) {
            traverseAllPage(rootPage, new TraversalListener<WikiPage>() {
                @Override
                public void process(WikiPage page) {
                    String helpString = page.getData().getAttribute("Help");
                    WikiPagePath pagePath = page.getPageCrawler().getFullPath();
                    CACHE.put(pagePath.toString(), helpString);
                }
            });
        }
        return pageNames;
    }
    static Thread running;

    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        //只有一个线程,不考虑冲突和其它异常.
        if(running==null ||! running.isAlive()){
            running=new Thread(new Runnable() {
                @Override
                public void run() {
                    collectPageHelpInfo(context.getRootPage());
                }
            });
            running.start();
        }
        JSONObject object = new JSONObject(CACHE);
        response.setContent(object.toString());
        return response;
    }


}
