package fitnesse.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 该类提供Quartz的cron表达式与Date之间的转换
 * Created by zhangzh on 2016/8/2.
 */
public class CronDateUtils {
  private static final String CRON_DATE_FORMAT = "ss mm HH dd MM W yyyy";

  /***
   *
   * @param date 时间
   * @return cron类型的日期
   */
  public static String getCron(final Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
    String formatTimeStr = "";
    if (date != null) {
      formatTimeStr = sdf.format(date);
    }
    return formatTimeStr;
  }

  /***
   *
   * @param cron Quartz cron的类型的日期
   * @return Date日期
   */

  public static Date getDate( String cron) {


    if (cron == null) {
      return null;
    }
    cron=cron.replace("*", "0").replace("?","0");

    SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
    Date date = null;
    try {
      date = sdf.parse(cron);
    } catch (ParseException e) {
      System.out.println(e.getMessage());

    }
    return date;
  }

  /**
   * 支持部分表达式,生成时间间隔
   *
   * @param cronString 表达式
   * @return period second
   */
  public static long getPeriod(String cronString) {
    return 24*60*60*1000;
  }

  /**
   * 当前时间差 时分秒
   * @param cronString
   * @return
   */
  public static long getInital(String cronString){
   Date date= getDate(cronString);

   long out=date.getMinutes()*60*1000+date.getSeconds()*1000+date.getHours()*60*60*10000;
   return out;
  }
}