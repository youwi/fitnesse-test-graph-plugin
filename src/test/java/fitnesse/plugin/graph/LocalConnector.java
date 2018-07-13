package fitnesse.plugin.graph;

/**
 * pluginTestGraph
 * Created  by  yu  on  2018/7/12.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class LocalConnector extends Thread {

    InputStream is;

    OutputStream os;

    static final Logger logger = Logger.getLogger(LocalConnector.class.getName());


    public LocalConnector(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }


    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = this.is;
            out = this.os;
            byte buffer[] = new byte[8192];
            int a;
            while ((a = in.read(buffer)) > 0) {
                out.write(buffer, 0, a);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.info(e.toString());
            }
        }
    }


    public static void main(String[] args) {
        try {
            int port = 1080;
            String forwardHost = "23.244.180.74";
            int forwardPort = 1080;
            ServerSocket ss = new ServerSocket(port);
            logger.info("监听端口:" + port);
            while (true) {
                try {
                    Socket s = ss.accept();
                    String ip = s.getInetAddress().getHostAddress();
                    logger.info("Host:" + ip + ",连接成功!");
                    Socket socket = new Socket(forwardHost, forwardPort);
                    logger.info("转发到:" + forwardHost + ",端口:" + forwardPort);
                    (new LocalConnector(socket.getInputStream(), s.getOutputStream())).start();
                    (new LocalConnector(s.getInputStream(), socket.getOutputStream())).start();
                } catch (Exception e) {
                    logger.info(e.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}