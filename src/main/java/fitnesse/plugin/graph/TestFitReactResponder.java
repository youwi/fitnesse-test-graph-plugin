package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.html.template.HtmlPage;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.*;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestFitReactResponder implements Responder {
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
        String resource = request.getResource();
        WikiPagePath path = PathParser.parse(resource);
        WikiPage page = context.getRootPage().getPageCrawler().getPage(path);

        PageData pageData = page.getData();

        HtmlPage html = new HtmlPage(context.pageFactory.getVelocityEngine(), "nothingvm.vm", "bootstrap", context.contextRoot);
        //= context.pageFactory.newPage();

        html.setTitle("Edit " + resource);
        html.setHeaderTemplate("nothingvm");//clear header

        // html.setPageTitle(new PageTitle("Editor", PathParser.parse(resource), pageData.getAttribute(PageData.PropertySUITES)));

        html.setNavTemplate("nothingvm");
        //html.put("viewLocation", request.getResource());
        html.setMainTemplate("fitTable");
        html.put("content", pageData.getContent());

        response.setContent(html.html());

        return response;
    }
}
