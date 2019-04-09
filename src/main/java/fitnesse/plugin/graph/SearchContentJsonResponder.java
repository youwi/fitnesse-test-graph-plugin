package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.TraversalListener;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import fitnesse.wiki.WikiPageProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fitnesse.plugin.graph.FitnesseWillLoadMe.traverseAllPage;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.LITERAL;

/**
 * pluginTestGraph
 * Created by yu on 2019-04-02.
 */
public class SearchContentJsonResponder implements Responder {
    /**
     *
     *  搜索的内容包含帮助信息和部分内容.
     * @param
     * @param
     * @return
     */
    static Map<String, JSONArray> CACHED = new HashMap();
    static long LAST_TIME_SPEND = 0;

    JSONArray searchPContentTags(final WikiPage thisPage, String searchString) {
        final JSONArray list = new JSONArray();

        if (thisPage != null) {
            final Pattern regularExpression = Pattern.compile(searchString, CASE_INSENSITIVE + LITERAL);

            traverseAllPage(thisPage, new TraversalListener<WikiPage>() {
                @Override
                public void process(WikiPage page) {
                    String pageContent = page.getData().getContent();
                    WikiPagePath pagePath = page.getPageCrawler().getFullPath();
                    pagePath.makeAbsolute();
                    String pageName = pagePath.toString();
                    Matcher matcherContent = regularExpression.matcher(pageContent);
                    Matcher matcherPageName = regularExpression.matcher(pageName);

                    if ( matcherPageName.find()|| matcherContent.find()) {
                        JSONObject obj=new JSONObject();
                        obj.put("helpName",page.getData().getAttribute(WikiPageProperty.HELP));
                        obj.put("pageName",pageName);
                        obj.put("contentPart",subString4line(pageContent));
                        list.put(obj);
                    }
                }
            });
        }
        return list;
    }

    /**
     * 截取4行预览信息
     * @param src
     * @return
     */
    public static String  subString4line(String src){
       String[] list= src.split("\n");
       if(list.length>5){
           return list[0]+"\n"+list[1]+"\n"+list[2]+"\n"+list[3];
       }else{
           return  src;
       }
    }


    static Thread runningThread = null;

    @Override
    public Response makeResponse(final FitNesseContext context, Request request) throws Exception {

        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        final String searchString = request.getInput("searchString");
        final String key = request.getResource() + ":" + searchString;
        JSONArray arr;
        if (CACHED.get(key) != null) {
            arr = CACHED.get(key);
            if (runningThread == null || !runningThread.isAlive()) {
                runningThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CACHED.put(key, searchPContentTags(context.getRootPage(), searchString));
                    }
                });
                runningThread.start();
            }
        } else {
            long stime = System.currentTimeMillis();
            arr = searchPContentTags(context.getRootPage(), searchString);
            long etime = System.currentTimeMillis();

            LAST_TIME_SPEND = etime - stime;
            System.out.println("last Thread time spend:  " + LAST_TIME_SPEND);
            CACHED.put(key, arr);
        }
        response.setContent(arr.toString());
        return response;
    }
}
