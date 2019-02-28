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
public class TestResultLogResponder implements Responder {

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {


    SimpleResponse response = new SimpleResponse();

    String deleteKey=request.getInput("deleteKey");
    String clearKey=request.getInput("clear");
    String msgKey=request.getInput("msg");
    String key=request.getInput("key");


    if(deleteKey!=null){
      TestResultLog.CACHE.remove(deleteKey);
    }
    if(clearKey!=null){
      TestResultLog.clear();
    }
    //保存msg
    if(msgKey!=null){
      JSONObject obj=  TestResultLog.CACHE.getJSONObject(key);
      obj.put("msg",msgKey);
    }

    response.setContent(TestResultLog.CACHE.toString());
    response.addHeader("Content-Type", "application/json;charset=UTF-8");
    response.addHeader("Access-Control-Allow-Origin", "*");

    return response;
  }
}
