package fitnesse.plugin.graph;

import fitnesse.util.http.Get;
import fitnesse.util.http.Http;
import org.json.JSONObject;

import fitnesse.authentication.Authenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * pluginTestGraph
 * Created by yu on 2018/11/2.
 */
public class TmpConfluenceAuthenticator extends Authenticator {

  Map<String, String> cache = new HashMap();
  //TODO
  String url = "http://conf.jihui.in/rest/prototype/1/search.json?max-results=1000&search=user&query=";

  /**
   * 使用 LDAP 和 admin 进行登陆修改
   * 代码写死了用 本地局域网进行登陆
   * @return
   */
  @Override
  public boolean isAuthenticated(String username, String password) {

    if (cache.get(username) != null || "admin".equals(username)) {
      return true;
    } else {
      Get get = Http.get(url + username);
      JSONObject jsonObject = new JSONObject(get.text());
      int totalSize = jsonObject.getInt("totalSize");
      if (totalSize == 1) {
        cache.put(username, "OK");
        return true;
      }
    }
    return false;
  }
};

