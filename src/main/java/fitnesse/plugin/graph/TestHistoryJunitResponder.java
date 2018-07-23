package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.reporting.history.*;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fitnesse.util.BeanUtil.getObjectByPath;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class TestHistoryJunitResponder implements Responder {
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        //context
        SimpleResponse response = new SimpleResponse();

        WikiPage rootPage = context.getRootPage(request.getMap());
        String format = request.getInput("format");

        File resultsDirectory = context.getTestHistoryDirectory();
        String pageName = request.getResource();
        TestHistory testHistory = new TestHistory(resultsDirectory, pageName);

        Map map = (Map) getObjectByPath(testHistory, "pageDirectoryMap");
        File dir = (File) map.get(pageName);
        File[] allReportFiles=dir.listFiles();
        if (allReportFiles.length > 1) {
            File file =  allReportFiles[1];
            ExecutionReport report;
            String content = FileUtil.getFileContent(file);
            report = ExecutionReport.makeReport(content);

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("formatter", this);
            velocityContext.put("suiteExecutionReport", report);
            response.setContentType(Response.Format.XML);
            response.setContent(context.pageFactory.render(velocityContext, "suiteJunit.vm"));
        }

        return response;
    }
}
