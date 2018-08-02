package fitnesse.util;

import java.util.ArrayList;
import java.util.List;

/**
 * pluginTestGraph
 * Created by yu on 2018/8/2.
 * 切开Fit的表格为和个模块.每个表格为一个List中的元素.
 */
public class ParseFit {

  public static List parseTextToList(String text) {
    List<List> all = new ArrayList();
    List crr = null;
    boolean lastIsText = false;
    boolean blockIng = false;

    for (String line : text.split("\n")) {
      boolean isText = !line.startsWith("|");


      if (line.contains("{{{")) {
        blockIng = true;
      }
      if (line.contains("}}}")) {
        crr.add(line);
        blockIng = false;
        lastIsText = false;
        continue;
      }
      if (blockIng) {
        crr.add(line);
        lastIsText = false;
        continue;
      }

      if (isText && lastIsText) {
        crr.add(line);
      }
      if (isText && !lastIsText) {
        crr = new ArrayList();
        all.add(crr);
        crr.add(line);
      }
      if (!isText && lastIsText) {
        crr = new ArrayList();
        all.add(crr);
        crr.add(line);
      }
      if (!isText && !lastIsText) {
        crr.add(line);
      }
      lastIsText = isText;
    }
    return all;
  }

  /**
   * 过滤只剩下Table
   *
   * @param ori 源数组
   * @return
   */
  public static List getFitTables(List ori) {
    List out = new ArrayList();
    for (Object line : ori) {
      if (line.toString().startsWith("[|") || line.toString().startsWith("|")) {
        out.add(line);
      }
    }
    return out;
  }
}
