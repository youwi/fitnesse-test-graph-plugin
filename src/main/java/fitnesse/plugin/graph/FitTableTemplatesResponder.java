package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.html.template.HtmlPage;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class FitTableTemplatesResponder implements Responder {
    /**
     * 使用react来编辑表格
     * 目前使用的是新的代码.
     *
     * @param context
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        SimpleResponse response = new SimpleResponse();
        return response;


    }
}
