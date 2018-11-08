package fitnesse.plugin.graph;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.util.http.Get;
import fitnesse.util.http.Http;
import fitnesse.wikitext.parser.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 支持周期执行.
 * !schedule 0 21 19 * * ? 支持这个语法.
 *
 */
public class ScheduleRunnerSymbolType extends SymbolType implements Rule, Translation {

  static Map<String,TimerTask> CACHE=new HashMap();

  public ScheduleRunnerSymbolType() {
    super("ContentsNormalName");
    wikiMatcher(new Matcher().startLineOrCell().string("!schedule"));
    wikiRule(this);
    htmlTranslation(this);
  }

  @Override
  public Maybe<Symbol> parse(Symbol current, Parser parser) {
    Symbol body = parser.parseToEnd(SymbolType.Newline);

    String corn=body.getContent();


    return new Maybe(body);
  }

  @Override
  public String toTarget(Translator translator, Symbol symbol) {
    String fullPath=translator.getPage().getFullPath();//包装定时器
    String cronString=symbol.getContent();

    CACHE.put(fullPath,new TestCaseCallerTask(fullPath,cronString));

    // TODO

    return symbol.getContent();
  }

  class TestCaseCallerTask extends  TimerTask{
      String fullPath;
      String cron;

    public TestCaseCallerTask(String fullPath, String cron) {
      this.fullPath = fullPath;
      this.cron = cron;
    }


    public void run() {
        Get get = Http.get(fullPath);
        System.out.println(get.text());
        System.out.println(get.headers());

      }

  }

}
