package fitnesse.plugin.graph;

import fitnesse.reporting.Formatter;
import fitnesse.testrunner.WikiTestPage;
import fitnesse.testsystems.*;
import fitnesse.wiki.WikiPage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import static fitnesse.wiki.WikiPageProperty.*;

/**
 * pluginTestGraph
 * Created by yu on 2018/11/7.
 */
public class TestResultLog implements Formatter {
  public static String STATUS_FILE = "/tmp/fit_test_status.json";

  static boolean committed = true;

  static Timer timer = new Timer(true);

  static TimerTask task = new TimerTask() {
    public void run() {
      saveStatusFile();
    }
  };
  public static JSONObject CACHE = new JSONObject(initStatusFile());

  public static void clear(){
    CACHE=new JSONObject("{}");
  }


  public TestResultLog() {
  }
  // delay为long,period为long：从现在起过delay毫秒以后，每隔period
  // 毫秒执行一次。

  @Override
  public void testSystemStarted(TestSystem testSystem) {

  }

  @Override
  public void testOutputChunk(String output) {

  }

  @Override
  public void testStarted(TestPage testPage) {

  }

  @Override
  public void testComplete(TestPage testPage, TestSummary testSummary) {
    String fullPath = testPage.getFullPath();
    int newCount = testSummary.getWrong() + testSummary.getExceptions();
    JSONObject node = getObject(CACHE, fullPath);
    //int old = getInt(node, "wrong");

    if (testPage instanceof WikiTestPage) {
      committed = false;
      node.put("wrong", newCount);
      WikiPage page = ((WikiTestPage) testPage).getSourcePage();
      String user = page.getData().getAttribute(LAST_MODIFYING_USER);
      String pageName = page.getData().getAttribute(HELP);
      String update = page.getData().getAttribute(LAST_MODIFIED);
      node.put("user", user);
      node.put("pageName", pageName);
      node.put("update", update);

      String skip=page.getData().getAttribute(PRUNE);// Skip Test
      if("on".equals(skip)){
        node.put("wrong",0);
      }
    }

  }

  JSONObject getObject(JSONObject jsonObject, String key) {
    JSONObject old;
    try {
      old = CACHE.getJSONObject(key);
    } catch (Exception e) {
      old = new JSONObject();
      jsonObject.put(key, old);
    }
    return old;
  }

  int getInt(JSONObject jsonObject, String key) {
    int old = 0;
    try {
      old = CACHE.getInt(key);
    } catch (Exception e) {
      System.out.println("---->" + e.getMessage());
    }
    return old;
  }

  static void saveStatusFile() {
    File file = new File(STATUS_FILE);

    try {
      if (!committed) {
        Files.write(Paths.get(file.getPath()), CACHE.toString().getBytes());
        System.out.println("--save-log---->" + STATUS_FILE + "<----");
      }
      committed = true;
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  static String initStatusFile() {

    timer.schedule(task, 5000, 15000);

    File file = new File(STATUS_FILE);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return "{}";
    }
    String fileContent = "{}";
    try {
      fileContent = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
      if ("".equals(fileContent)) {
        return "{}";
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileContent;
  }


  @Override
  public void testSystemStopped(TestSystem testSystem, Throwable cause) {

  }

  @Override
  public void testAssertionVerified(Assertion assertion, TestResult testResult) {

  }

  @Override
  public void testExceptionOccurred(Assertion assertion, ExceptionResult exceptionResult) {

  }
}
