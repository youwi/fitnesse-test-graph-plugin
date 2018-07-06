package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesseMain.FitNesseMain;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/6.
 */
public class TestRestartResponder implements Responder {


    public void restartApplication() {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar;
        try {
            currentJar = new File(FitNesseMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            /* is it a jar file? */
            if (!currentJar.getName().endsWith(".jar"))
                return;

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        restartApplication();
        return null;
    }
}
