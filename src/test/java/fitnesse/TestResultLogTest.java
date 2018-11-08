package fitnesse;

import fitnesse.plugin.graph.TestResultLog;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSummary;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * TestResultLog Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Nov 7, 2018</pre>
 */
public class TestResultLogTest {

  @Before
  public void before() throws Exception {
  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: testSystemStarted(TestSystem testSystem)
   */
  @Test
  public void testTestSystemStarted() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: testOutputChunk(String output)
   */
  @Test
  public void testTestOutputChunk() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: testStarted(TestPage testPage)
   */
  @Test
  public void testTestStarted() throws Exception {
    //TODO: Test goes here...
  }

  /**
   *  测试非标准json的支持度
   */
  @Test
  public void testTestComplete() throws Exception {
    String abc="{a:1,}";
    String out= new JSONObject(abc).toString();
    System.out.println(out);
    assert "{\"a\":1}".equals(out);
    abc="[1,2,]";

    out=new JSONArray(abc).toString();
    System.out.println(out);
    assert "[1,2]".equals(out);


  }

  /**
   * Method: saveStatusFile()
   */
  @Test
  public void testSaveStatusFile() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: initStatusFile()
   */
  @Test
  public void testInitStatusFile() throws Exception {
    //TODO: Test goes here...
   new TestResultLog();
   //new TestSummary(100,100,100);
   // new JSONObject("{}").getInt("AVC");


   Thread.sleep(10000000);
  }

  /**
   * Method: testSystemStopped(TestSystem testSystem, Throwable cause)
   */
  @Test
  public void testTestSystemStopped() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: testAssertionVerified(Assertion assertion, TestResult testResult)
   */
  @Test
  public void testTestAssertionVerified() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: testExceptionOccurred(Assertion assertion, ExceptionResult exceptionResult)
   */
  @Test
  public void testTestExceptionOccurred() throws Exception {
    //TODO: Test goes here...
  }


} 
