package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.Formatter;
import fitnesse.testsystems.*;
import fitnesse.util.BeanUtil;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static fitnesse.plugin.graph.TestResultLog.STATUS_FILE;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFIED;
import static fitnesse.wiki.WikiPageProperty.LAST_MODIFYING_USER;

/**
 * pluginTestGraph
 * Created by yu on 2018/11/7.
 */
public class TestResultLogResponder implements Responder {

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {


    SimpleResponse response = new SimpleResponse();

    response.setContent(TestResultLog.CACHE.toString());
    response.addHeader("Content-Type", "application/json;charset=UTF-8");
    response.addHeader("Access-Control-Allow-Origin", "*");

    return response;
  }
}
