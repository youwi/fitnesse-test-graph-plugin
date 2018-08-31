package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.ExecutionReport;
import fitnesse.reporting.history.PageHistory;
import fitnesse.reporting.history.SuiteExecutionReport;
import fitnesse.reporting.history.TestHistory;
import fitnesse.wiki.WikiPage;
import org.json.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fitnesse.util.BeanUtil.getObjectByPath;
import static fitnesse.util.BeanUtil.objectToJson;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestHistoryStageResponder implements Responder {
  static Map<String, Integer> CACHE_KV = new HashMap();
  static Map<String, String> CACHE_USER = new HashMap();
  static long LAST_RUN_FULL = System.currentTimeMillis();
  static long LAST_RUN = System.currentTimeMillis();

  static Thread runningThread;

  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {

    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");


    if (runningThread == null || !runningThread.isAlive()) {
      runningThread = new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            if (System.currentTimeMillis() - LAST_RUN < 60 * 1000 && CACHE_KV.size() != 0) {
              return;
            }
            boolean fullRun = false;
            if (System.currentTimeMillis() - LAST_RUN_FULL > 24 * 60 * 60 * 1000) {
              LAST_RUN_FULL = System.currentTimeMillis();
              fullRun = true;
            }

            ExecutionReport report = getFirstReportByPath(context, request.getResource());

            if (report instanceof SuiteExecutionReport) {
              for (SuiteExecutionReport.PageHistoryReference s : ((SuiteExecutionReport) report).getPageHistoryReferences()) {
                Integer lastWrong = s.getTestSummary().getWrong();
                String name = s.getPageName();
                if (lastWrong <= 0 && !fullRun) {
                  continue;
                }
                CACHE_KV.put(name, lastWrong);
                ExecutionReport reportCase = getFirstReportByPath(context, name);
                CACHE_KV.put(name, reportCase.getFinalCounts().getWrong());

              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      runningThread.start();
    }

    Map out = new HashMap();
    if (CACHE_KV.size() == 0) {
      response.setContent("{}");
    }
    for (Object key : CACHE_KV.keySet()) {
      if (CACHE_KV.get(key) > 0) {
        out.put(key, CACHE_KV.get(key));
      }

      response.setContent(objectToJson(out));
    }
    return response;
  }

  public ExecutionReport getFirstReportByPath(FitNesseContext context, String pageName) throws Exception {
    File resultsDirectory = context.getTestHistoryDirectory();
    TestHistory history = new TestHistory(resultsDirectory, pageName);
    Map map = (Map) getObjectByPath(history, "pageDirectoryMap");
    File dir = (File) map.get(pageName);
    File[] allReportFiles = dir.listFiles();
    Arrays.sort(allReportFiles);

    if (allReportFiles.length == 0) {
      return null;
    }

    File file = allReportFiles[allReportFiles.length - 1];
    String content = FileUtil.getFileContent(file);
    return ExecutionReport.makeReport(content);

  }
}
