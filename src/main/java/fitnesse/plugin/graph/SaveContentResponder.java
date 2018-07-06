package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.editing.EditResponder;
import fitnesse.responders.editing.SaveRecorder;
import fitnesse.wiki.*;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class SaveContentResponder implements Responder {
    /**
     * use body
     *
     * @param context
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        String body = request.getBody();

        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        String resource = request.getResource();

        WikiPagePath path = PathParser.parse(resource);
        WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
        PageData pageData=page.getData();
        pageData.setContent(body);
        page.commit(pageData);
        SaveRecorder.pageSaved(page, getTicketId(request));
        VersionInfo commitRecord = page.commit(pageData);

        try {
            response.setContent("{}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent("{error:1,msg:'" + e.getMessage() + "'}");
        }
        return response;
    }

    private long getTicketId(Request request) {
        if (!request.hasInput(EditResponder.TICKET_ID))
            return 0;
        String ticketIdString = request.getInput(EditResponder.TICKET_ID);
        return Long.parseLong(ticketIdString);
    }

}
