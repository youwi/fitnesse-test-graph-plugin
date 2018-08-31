package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.TraversalListener;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.ExecutionReport;
import fitnesse.reporting.history.SuiteExecutionReport;
import fitnesse.reporting.history.TestHistory;
import fitnesse.wiki.*;
import fitnesse.wikitext.parser.VariableSource;
import util.FileUtil;

import java.io.File;
import java.util.*;

import static fitnesse.util.BeanUtil.getObjectByPath;
import static fitnesse.util.BeanUtil.objectToJson;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class PageByHttpProxyConvert implements WikiPageFactory {


  @Override
  public WikiPage makePage(File path, String pageName, WikiPage parent, VariableSource variableSource) {

    return new VirtualWikiPage();
  }

  @Override
  public boolean supports(File path) {
    if (path.getName().contains("proxy"))
      return true;
    return false;
  }
}
