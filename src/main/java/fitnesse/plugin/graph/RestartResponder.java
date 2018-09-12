package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.util.BeanUtil;
import fitnesseMain.FitNesseMain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/6.
 */
public class RestartResponder implements Responder {


  public String restartApplication(final FitNesseContext context) {
    final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    final File currentJar;
    try {
      currentJar = new File(FitNesseMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());


      if (!currentJar.getName().endsWith(".jar"))
        return "";
      final ArrayList<String> command = new ArrayList<String>();
      // command.add("/bin/sh");
      // command.add("-c");
      command.add(javaBin);
      RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
      List<String> listOfArguments = runtimeMxBean.getInputArguments();

      for (String arg : listOfArguments) {
        if (!arg.contains("-agentlib"))
          command.add(arg);
      }
      command.add("-cp");
      command.add(ManagementFactory.getRuntimeMXBean().getClassPath());
      command.add("-jar");
      command.add(currentJar.getPath());

      final ProcessBuilder builder = new ProcessBuilder(command);
      try {

        Process p = builder.start();
        Thread shutdownThread = new Thread() {
          @Override
          public void run() {
            try {
              Object obj = BeanUtil.getObjectByPath(context, "fitNesse.theService.serverSocket");
              if (obj instanceof ServerSocket) {
                ((ServerSocket) obj).close();
              }
            } catch (Exception e) {
              System.out.println(e.getMessage());
            }
          }
        };
        shutdownThread.start();
        p.waitFor(1, TimeUnit.SECONDS);
        String out = join(command.toArray(), " ");
        out += logStream(p.getErrorStream());
        out += logStream(p.getInputStream());
        return out;
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String logStream(InputStream error) throws IOException {
    byte[] bytes = new byte[0];
    bytes = new byte[error.available()];
    error.read(bytes);
    String str = new String(bytes);
    return str;
  }

  static public String join(Object[] list, String conjunction) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Object item : list) {
      if (first)
        first = false;
      else
        sb.append(conjunction);
      sb.append(item);
    }
    return sb.toString();
  }


  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {



    String out = restartApplication(context);
    System.out.println(out);
    SimpleResponse response = new SimpleResponse();
    response.setContent(out);
    return response;
  }
}
