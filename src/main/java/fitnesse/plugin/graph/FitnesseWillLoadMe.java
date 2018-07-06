package fitnesse.plugin.graph;

import fitnesse.authentication.Authenticator;
import fitnesse.plugins.PluginException;
import fitnesse.plugins.PluginFeatureFactory;
import fitnesse.reporting.FormatterRegistry;
import fitnesse.responders.ResponderFactory;
import fitnesse.responders.editing.ContentFilter;
import fitnesse.responders.files.FileResponder;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.util.Clock;
import fitnesse.wiki.WikiPageFactoryRegistry;
import fitnesse.wikitext.parser.SymbolProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;


/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 * META-INF/services/fitnesse.plugins.PluginFeatureFactory
 */
public class FitnesseWillLoadMe implements PluginFeatureFactory {

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
        //resetFileResponderLimit();
        responderFactory.addResponder("files", FileResponder.class);
        System.out.println("-----fitnesse.plugin.graph loaded----");
        responderFactory.addResponder("testHistoryJson", TestHistoryJsonResponder.class);
        responderFactory.addResponder("testHistoryGraph", TestHistoryGraphResponder.class);
        responderFactory.addResponder("editR", TestFitReactResponder.class);
        responderFactory.addResponder("clone", TestHistoryCloneResponder.class);
        responderFactory.addResponder("saveContent", TestSaveContentResponder.class);
        responderFactory.addResponder("filesTagsJson", TestSaveContentResponder.class);
        responderFactory.addResponder("includeTagsJson", TestSaveContentResponder.class);
        responderFactory.addResponder("urlsTagsJson", TestSaveContentResponder.class);
        responderFactory.addResponder("varsTagsJson", TestSaveContentResponder.class);
        responderFactory.addResponder("testHtml", TestSaveContentResponder.class);


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


