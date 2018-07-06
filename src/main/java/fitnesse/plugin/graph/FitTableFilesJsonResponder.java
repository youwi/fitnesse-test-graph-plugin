package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.TraversalListener;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import fitnesse.wiki.fs.FileSystemPage;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/6.
 */
public class FitTableFilesJsonResponder implements Responder {

    /**
     * link  to fitnesse.responders.refactoring.RefactorPageResponder
     * filter Fitnesse Root
     * need cache
     *
     * @param
     * @param
     * @return
     */
    static List<String> CACHED;
    static long LAST_TIME_SPEND = 0;

    List<String> collectPageNames(final WikiPage thisPage, WikiPage rootPage) {
        final List<String> pageNames = new ArrayList<>();
        if (thisPage != null) {
            final WikiPagePath thisPagePath = thisPage.getPageCrawler().getFullPath();
            traverse(rootPage, new TraversalListener<WikiPage>() {

                @Override
                public void process(WikiPage page) {
                    WikiPagePath pagePath = page.getPageCrawler().getFullPath();
                    pagePath.makeAbsolute();
                    if (!thisPagePath.equals(pagePath) && !pagePath.isEmpty()) {
                        pageNames.add(pagePath.toString());
                    }
                }
            });
        }
        return pageNames;
    }

    private void traverse(WikiPage page, TraversalListener<? super WikiPage> listener) {
        if (page instanceof FileSystemPage) {
            String filename = ((FileSystemPage) page).getFileSystemPath().getPath();
            if (filename.contains("FitNesseRoot/FitNesse")) {
                return;
            }
        }

        listener.process(page);
        for (WikiPage wikiPage : page.getChildren()) {
            traverse(wikiPage, listener);
        }
    }


    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        List arr;
        if (LAST_TIME_SPEND > 200) {
            arr = CACHED;
            new UpdateTaskThread(context.getRootPage()).start();
        } else {
            long stime = System.currentTimeMillis();
            arr = collectPageNames(context.getRootPage(), context.getRootPage());
            long etime = System.currentTimeMillis();

            LAST_TIME_SPEND = etime - stime;
            CACHED = arr;
        }

        JSONArray jsonArray = new JSONArray(arr);
        response.setContent(jsonArray.toString());
        return response;
    }

    /**
     * thread update cache.
     */
    class UpdateTaskThread extends Thread {
        WikiPage root;

        UpdateTaskThread(WikiPage root) {
            this.root = root;
        }

        @Override
        public void run() {
            CACHED = collectPageNames(root, root);
        }
    }
}
