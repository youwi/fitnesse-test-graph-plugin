package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import org.json.JSONObject;

/**
 * pluginTestGraph
 * Created by yu on 2018/11/7.
 */
public class ScheduleStatusResponder implements Responder {

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {


    SimpleResponse response = new SimpleResponse();

    response.setContent(new JSONObject(ScheduleRunnerSymbolType.CACHE_CRON).toString());
    response.addHeader("Content-Type", "application/json;charset=UTF-8");
    response.addHeader("Access-Control-Allow-Origin", "*");

    return response;
  }
}
