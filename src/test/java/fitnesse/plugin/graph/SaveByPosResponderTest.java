package fitnesse.plugin.graph;

import difflib.Patch;
import fitnesse.util.Computeclass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * pluginTestGraph
 * Created by yu on 2018/8/2.
 */
public class SaveByPosResponderTest {
  String ori = "!include -c .LieLuoBo.AccountLibrary.LoginWWW3001\n" +
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
          "|check    |json Structure|code,msg,body                   |true   | \n" +
          "|check    |json Value    |msg                             |OK     |\n" +
          "|check    |json Contain  |{\"phoneExt\": \"021-12345678-123\"}|true   |\n" +
          "\n" +
          "显示座机号码\n" +
          "\n" +
          "账号信息\n" +
          "绑定手机19900000001 绑定邮箱autotest-all@test.com\n" +
          "座席操作\n" +
          "座席类型 坐席号码12345678 坐席组号 分机号码021-12345678 登录密码****";

  @Test
  public void formatContentByPos() {
    SaveByPosResponder.Pos pos = new SaveByPosResponder.Pos();
    pos.indexTable = 1;
    pos.indexTr = 0;
    pos.indexTd = 1;

    String out;

    out = SaveByPosResponder.formatContentByPos(ori, "Connect Server", "Connect ServerKK", pos);

    System.out.println(out);

    pos.indexTd = 0;
    System.out.println(SaveByPosResponder.formatContentByPos(ori, "script", "Connect ServerKK", pos));
    out = SaveByPosResponder.formatContentByPos(ori, "script not Found", "Connect ServerKK", pos);
    out = SaveByPosResponder.formatContentByPos(out, "script not Found", "Connect ServerKK", pos);

    System.out.println(out);
    System.out.println(Computeclass.SimilarDegree(ori, out));
    assert ori.trim().equals(out.trim());

  }

  @Test
  public void sfe() {
    SaveByPosResponder.Pos pos = new SaveByPosResponder.Pos();

    pos.indexTable = 1;
    pos.indexTr = 4;
    pos.indexTd = 3;
   String out = SaveByPosResponder.formatContentByPos(ori, "OK", "Connect ServerKK", pos);
    System.out.println(out);

  }
  @Test
  public void s222fe() {
    SaveByPosResponder.Pos pos = new SaveByPosResponder.Pos();

    pos.indexTable = 1;
    pos.indexTr = 4;
    pos.indexTd = 1;
    String out = SaveByPosResponder.formatContentByPos(ori, "json Value", "Connect ServerKK", pos);
    System.out.println(out);

  }
}