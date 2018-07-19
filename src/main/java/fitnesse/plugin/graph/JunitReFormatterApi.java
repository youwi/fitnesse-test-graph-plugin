package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.reporting.history.InvalidReportException;
import fitnesse.reporting.history.JunitReFormatter;
import fitnesse.reporting.history.SuiteHistoryFormatter;
import fitnesse.reporting.history.TestExecutionReport;
import fitnesse.wiki.WikiPage;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * pluginTestGraph
 * with page Help info
 * Created by yu on 2018/7/19.
 */
public class JunitReFormatterApi extends JunitReFormatter {
    public JunitReFormatterApi(FitNesseContext context, WikiPage page, Writer writer, SuiteHistoryFormatter historyFormatter) {
        super(context, page, writer, historyFormatter);
        this.context = context;
        this.writer = writer;
        this.historyFormatter = historyFormatter;
    }
    private final FitNesseContext context;
    private final Writer writer;
    private final SuiteHistoryFormatter historyFormatter;

    @Override
    public void close() throws IOException {
      // 由于测试日志中没有 tag和help信息,所以不能从junit导出报告中着手得到.
    }


}
