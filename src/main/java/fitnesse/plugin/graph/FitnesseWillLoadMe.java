package fitnesse.plugin.graph;

import fitnesse.authentication.Authenticator;
import fitnesse.plugins.PluginException;
import fitnesse.plugins.PluginFeatureFactory;
import fitnesse.reporting.FormatterRegistry;
import fitnesse.responders.ResponderFactory;
import fitnesse.responders.editing.ContentFilter;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.wiki.WikiPageFactoryRegistry;
import fitnesse.wikitext.parser.SymbolProvider;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 * META-INF/services/fitnesse.plugins.PluginFeatureFactory
 */
public class FitnesseWillLoadMe  implements PluginFeatureFactory {

  @Override
  public Authenticator getAuthenticator() {
    return null;
  }

  @Override
  public ContentFilter getContentFilter() {
    return null;
  }

  @Override
  public void registerResponders(ResponderFactory responderFactory) throws PluginException {
    System.out.println("-----fitnesse.plugin.graph loaded----");
    responderFactory.addResponder("testHistoryJson",TestHistoryJsonResponder.class);
    responderFactory.addResponder("testHistoryGraph",TestHistoryGraphResponder.class);
    responderFactory.addResponder("clone",TestHistoryCloneResponder.class);
  }

  @Override
  public void registerSymbolTypes(SymbolProvider symbolProvider) throws PluginException {

  }

  @Override
  public void registerWikiPageFactories(WikiPageFactoryRegistry wikiPageFactoryRegistry) throws PluginException {

  }

  @Override
  public void registerFormatters(FormatterRegistry registrar) throws PluginException {

  }

  @Override
  public void registerTestSystemFactories(TestSystemFactoryRegistry testSystemFactoryRegistry) throws PluginException {

  }

  @Override
  public void registerSlimTables(SlimTableFactory slimTableFactory) throws PluginException {

  }

  @Override
  public void registerCustomComparators(CustomComparatorRegistry customComparatorRegistry) throws PluginException {

  }


}


