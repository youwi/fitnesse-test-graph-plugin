package fitnesse.util;

import fitnesse.util.CronDateUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * CronDateUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Nov 8, 2018</pre>
 */
public class CronDateUtilsTest {

  @Before
  public void before() throws Exception {


  }

  @After
  public void after() throws Exception {


  }

  /**
   * Method: getCron(final Date date)
   */
  @Test
  public void testGetCron() throws Exception {
    List abc = new ArrayList();
    abc.add(1);
    abc.add(1);
    abc.add(1);

    abc.add(1);

    String out = BeanUtil.objectToJson(abc);
    System.out.println(out);
  }

  /**
   * Method: getDate(final String cron)
   */
  @Test
  public void testGetDate() throws Exception {
    Map map = new HashMap();
    map.put("A", "B");
    map.put("A", "B");
    map.put("A", "B");
    map.put("A", "B");

    String out = new JSONObject(map).toString();
    System.out.println(out);


    out = BeanUtil.objectToJson(map);

    System.out.println(out);
    assert "{\"A\":\"B\"}".equals(out);
  }

  @Test
  public void sfe() {
    String cron = "20 28 17 02 08 ? 2016";

    Date cronDate = CronDateUtils.getDate(cron);
    System.out.println("===================");
    System.out.println(cronDate.toString());


    cron = "20 28 17 * 08 ? 2016";

    cronDate = CronDateUtils.getDate(cron);
    System.out.println("===================");
    System.out.println(cronDate.toString());


    cron = "20 28 17 * 08 ? ?";

    cronDate = CronDateUtils.getDate(cron);
    System.out.println("===================");
    System.out.println(cronDate.toString());

  }

} 
