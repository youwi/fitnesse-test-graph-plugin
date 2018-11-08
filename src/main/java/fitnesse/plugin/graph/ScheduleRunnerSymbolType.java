package fitnesse.plugin.graph;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.util.http.Get;
import fitnesse.util.http.Http;
import fitnesse.wikitext.parser.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static fitnesse.util.CronDateUtils.getInital;
import static fitnesse.util.CronDateUtils.getPeriod;

/**
 * 支持周期执行.
 * !schedule 0 21 19 * * ? 支持这个语法.
 */
public class ScheduleRunnerSymbolType extends SymbolType implements Rule, Translation {

  static Map<String, ScheduledFuture> CACHE = new HashMap();
  static Map<String, String> CACHE_CRON = new HashMap();

  ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

  public ScheduleRunnerSymbolType() {
    super("ContentsNormalName");
    wikiMatcher(new Matcher().startLineOrCell().string("!schedule"));
    wikiRule(this);
    htmlTranslation(this);
  }

  @Override
  public Maybe<Symbol> parse(Symbol current, Parser parser) {
    Symbol body = parser.parseToEnd(SymbolType.Newline);
    for (Symbol option : body.getChildren()) {
      if (option.isType(SymbolType.Whitespace)) continue;
      current.add(option);
    }
    StringBuilder csvBuilder = new StringBuilder();
    for(Symbol city : current.getChildren()){
      csvBuilder.append(city.getContent());
      csvBuilder.append(" ");
    }

    String fullPath = parser.getPage().getPage().getFullPath();

    String corn = csvBuilder.toString();
    if(!corn.equals("")){
      fill(fullPath, corn);
    }

    return new Maybe(body);
  }

  @Override
  public String toTarget(Translator translator, Symbol symbol) {
    String fullPath = translator.getPage().getFullPath();//包装定时器
    String cronString = symbol.getContent();
    fill(fullPath, cronString);

    // TODO

    return symbol.getContent();
  }

  void fill(String fullPath, String cronString) {
    ScheduledFuture task = CACHE.get(fullPath);

    if (task != null) {
      task.cancel(true);
    }
    //TODO 目前1天执行一次

    // timer.schedule(task, cronToDate(cronString), 24 * 60 * 60 * 1000);
    long initalDelay = getInital(cronString);
    long period = getPeriod(cronString);
    ScheduledFuture scheduledFuture = service.scheduleAtFixedRate(new TestCaseCallerTask(fullPath, cronString), initalDelay, period, TimeUnit.MILLISECONDS);
    //cronToDate(cronString).getTime()+
    CACHE.put(fullPath, scheduledFuture);
    CACHE_CRON.put(fullPath,cronString);

  }


  public Date cronToDate(String cron) {
    return new Date();
  }

  public class TestCaseCallerTask implements Runnable {
    String fullPath;
    int count = 0;


    public String getCron() {
      return cron;
    }

    public void setCron(String cron) {
      this.cron = cron;
    }

    public String cron;

    public TestCaseCallerTask(String fullPath, String cron) {
      this.fullPath = fullPath;
      this.cron = cron;
    }

    public void run() {
      count++;
      try {
        System.out.println("-- doing job -- " + count + " " + fullPath);
        Get get = Http.get(fullPath);
        System.out.println(get.text());
        System.out.println(get.headers());
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }

}
