package fitnesse.plugin.graph;

import fitnesse.authentication.Authenticator;
import fitnesse.components.TraversalListener;
import fitnesse.plugins.PluginException;
import fitnesse.plugins.PluginFeatureFactory;
import fitnesse.reporting.Formatter;
import fitnesse.reporting.FormatterRegistry;
import fitnesse.responders.ResponderFactory;
import fitnesse.responders.editing.ContentFilter;
import fitnesse.responders.files.FileResponder;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import fitnesse.testrunner.TestsRunnerListener;
import fitnesse.testsystems.*;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.tables.ScriptTable;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.wiki.SymbolicPage;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageFactoryRegistry;
import fitnesse.wiki.fs.FileSystemPage;
import fitnesse.wikitext.parser.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 * META-INF/services/fitnesse.plugins.PluginFeatureFactory
 */
public class FitnesseWillLoadMe implements PluginFeatureFactory {


  @Override
  public Authenticator getAuthenticator() {
    return new ConfluenceAuthenticator();
  }

  @Override
  public ContentFilter getContentFilter() {
    return null;
  }

  @Override
  public void registerResponders(ResponderFactory responderFactory) throws PluginException {
    //resetFileResponderLimit();
    responderFactory.addResponder("files", FileResponder.class);
    System.out.println("-----fitnesse.plugin.graph loaded---ing-");
    responderFactory.addResponder("testHistoryJson", TestHistoryJsonResponder.class);
    responderFactory.addResponder("testHistoryGraph", TestHistoryGraphResponder.class);
    responderFactory.addResponder("testHistoryJunit", TestHistoryJunitResponder.class);
    responderFactory.addResponder("testHistoryStage", TestHistoryStageResponder.class);

    responderFactory.addResponder("editR", FitTableVmResponder.class);
    responderFactory.addResponder("clone", PageCloneResponder.class);
    responderFactory.addResponder("saveContent", SaveContentResponder.class);
    responderFactory.addResponder("filesTagsJson", FitTableFilesJsonResponder.class);
    responderFactory.addResponder("varsTagsJson", PageVarsResponder.class);
    responderFactory.addResponder("testHtml", SaveContentResponder.class);
    responderFactory.addResponder("restart", RestartResponder.class);
    responderFactory.addResponder("searchAsJson", FitSearchJsonResponder.class);
    responderFactory.addResponder("pageHelpJson", PageHelpInfoJsonApi.class);
    responderFactory.addResponder("saveContentPosition", SaveByPosResponder.class);
    responderFactory.addResponder("testNow", TestWithContentResponder.class);
    responderFactory.addResponder("userinfo", PageUserResponder.class);
    responderFactory.addResponder("testStatus", TestResultLogResponder.class);
    responderFactory.addResponder("ScheduleStatus", ScheduleStatusResponder.class);


    responderFactory.addResponder("webdrivers", WebDriverSessionManager.class);


    responderFactory.addResponder("/proxy/", PageByHttpProxyResponder.class);
    responderFactory.addResponder("/api/", PageByHttpProxyResponder.class);


    System.out.println("-----fitnesse.plugin.graph loaded-- ok --");
  }

  public static void traverseAllPage(WikiPage page, TraversalListener<? super WikiPage> listener) {
    if (page instanceof FileSystemPage) {
      String filename = ((FileSystemPage) page).getFileSystemPath().getPath();
      if (filename.contains("FitNesseRoot/FitNesse")) {
        return;
      }
    }
    if (page instanceof SymbolicPage) {
      return;
    }
    listener.process(page);
    for (WikiPage wikiPage : page.getChildren()) {
      traverseAllPage(wikiPage, listener);
    }
  }

  /**
   * default size is: RESOURCE_SIZE_LIMIT= 262144*2;
   * useless now....
   * patch files
   */
  public void resetFileResponderLimit() {
    try {
      Class clazz = Class.forName("fitnesse.responders.files.FileResponder");
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        if (field.getName().equals("RESOURCE_SIZE_LIMIT")) {
          field.setAccessible(true);
          Field modifiersField = Field.class.getDeclaredField("modifiers");
          modifiersField.setAccessible(true);
          modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
          field.setInt(null, 262144 * 4);
        }
        //if(field.getName().equals("LAST_MODIFIED_FOR_RESOURCES")){
        //    field.setAccessible(true);
        //    Field modifiersField = Field.class.getDeclaredField("modifiers");
        //    modifiersField.setAccessible(true);
        //    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        //    field.set(null,new Date((Clock.currentTimeInMillis() / 1000) * 1000 ));
        //}

      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void registerSymbolTypes(SymbolProvider symbolProvider) throws PluginException {
    symbolProvider.add(new PageContentsNormalNameSymbolType());
    symbolProvider.add(new ScheduleRunnerSymbolType());
  }

  @Override
  public void registerWikiPageFactories(WikiPageFactoryRegistry wikiPageFactoryRegistry) throws PluginException {
    wikiPageFactoryRegistry.registerWikiPageFactory(new PageByHttpProxyConvert());
  }

  @Override
  public void registerFormatters(FormatterRegistry registrar) throws PluginException {
    registrar.registerFormatter( TestResultLog.class);

  }

  @Override
  public void registerTestSystemFactories(TestSystemFactoryRegistry testSystemFactoryRegistry) throws PluginException {

  }

  @Override
  public void registerSlimTables(SlimTableFactory slimTableFactory) throws PluginException {
    slimTableFactory.addTableType("POST", ScriptTable.class);

  }

  @Override
  public void registerCustomComparators(CustomComparatorRegistry customComparatorRegistry) throws PluginException {

  }


}


