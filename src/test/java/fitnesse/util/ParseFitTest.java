package fitnesse.util;

import org.junit.Test;

import java.util.List;

import static fitnesse.util.ParseFit.getFitTables;
import static org.junit.Assert.*;

/**
 * pluginTestGraph
 * Created by yu on 2018/8/2.
 */
public class ParseFitTest {

  @Test
  public void parseTextToList() {

    List out=ParseFit.parseTextToList("!include -c .LieLuoBo.AccountLibrary.LoginWWW3001\n" +
            "|script   |Connect Server|${www_base_url}/project/getOneAll|\n" +
            "|set Param|id            |                   |${P196}      |\n" +
            "|get                                                       |\n" +
            "|check    |json Structure|code,msg,body      |true         |\n" +
            "|check    |json Value    |msg                |OK           |\n" +
            "0|check    |text Contain  |\"phoneExt\": \"021-12345678       |true         |\n" +
            "\n" +
            "现在有双重保险,没关系的.\n" +
            "现在返回了 021-12345678-123\n" +
            "|script   |Connect Server|${www_base_url}/project/getOneAll       |\n" +
            "|set Param|id            |                                |${P196}|\n" +
            "|get                                                              |\n" +
            "|check    |json Structure|code,msg,body                   |true   |\n" +
            "|check    |json Value    |msg                             |OK     |\n" +
            "|check    |json Contain  |{\"phoneExt\": \"021-12345678-123\"}|true   |\n" +
            "\n" +
            "显示座机号码\n" +
            "\n" +
            "账号信息\n" +
            "绑定手机19900000001 绑定邮箱autotest-all@test.com\n" +
            "座席操作\n" +
            "座席类型 坐席号码12345678 坐席组号 分机号码021-12345678 登录密码****");
    System.out.println(out);
    System.out.println(getFitTables(out));
  }
}