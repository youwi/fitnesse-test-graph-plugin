package fitnesse.plugin.graph;

import fitnesse.reporting.Formatter;
import fitnesse.testsystems.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

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
    String fullPath=testPage.getFullPath();
    int old=0;
    int newCount=testSummary.getWrong()+testSummary.getExceptions();
    try{
      old = CACHE.getInt(testPage.getFullPath());
    }catch (Exception e){
      System.out.println("---->"+e.getMessage());
    }
    if (old != newCount) {
      committed = false;
      CACHE.put(fullPath, newCount);
    } else {

    }
  }

  static void saveStatusFile() {
    File file = new File(STATUS_FILE);

    try {
      if (!committed) {
        Files.write(Paths.get(file.getPath()), CACHE.toString().getBytes());
        System.out.println("--save-log---->"+STATUS_FILE+"<----");
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
      if("".equals(fileContent)){
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
