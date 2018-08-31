package fitnesse.util.http;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IAT @wkzf
 * Created by yu on 2017/7/20.
 */
public class FitLog {

  public static FitLog getLogger() {
    return new FitLog();
  }

  static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

  public static void info() {
    System.out.printf("%tF %<tT.%<tL", new Date());
  }

  public static void info(String str) {
    String out = format.format(new Date());

    //System.out.printf("%tF %<tT.%<tL "+str+"\n",new Date());
    if (str.length() > 2000) {
      System.out.println(out + " " + str.substring(0, 2000) + "...");
    } else {
      System.out.println(out + " " + str);

    }
  }

  public static void error(String str) {
    String out = format.format(new Date());

    if (str.length() > 2000) {
      System.err.println(out + " " + str.substring(0, 2000) + "...");
    } else {
      System.err.println(out + " " + str);

    }
  }
}
