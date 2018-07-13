package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesseMain.FitNesseMain;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/6.
 */
public class TempWebPackResponder implements Responder {


    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        String ss = "{\"websocket\":true,\"origins\":[\"*:3000\"],\"cookie_needed\":false,\"entropy\":43269185}";
        SimpleResponse response = new SimpleResponse();
        response.setContent(ss);
        response.setContentType("application/json;charset=utf-8");
        return response;
    }
}
