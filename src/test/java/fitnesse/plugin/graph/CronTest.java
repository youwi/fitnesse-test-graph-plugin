package fitnesse.plugin.graph;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.junit.Test;

import java.util.Locale;

import static com.cronutils.model.CronType.QUARTZ;

/**
 * pluginTestGraph
 * Created by yu on 2018/11/8.
 */
public class CronTest {
  @Test
  public void sfee(){
    CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);

    //create a parser based on provided definition
    CronParser parser = new CronParser(cronDefinition);
    Cron quartzCron = parser.parse("0 * * 1-3 * ? *");


    //create a descriptor for a specific Locale
    CronDescriptor descriptor = CronDescriptor.instance(Locale.CHINA);

    //parse some expression and ask descriptor for description
    String description = descriptor.describe(parser.parse("*/45 * * * * ?"));
    //description will be: "every 45 seconds"
    System.out.println(description);

    //validate expression
    quartzCron.validate();
    quartzCron.getCronDefinition();
  }
}
