package fitnesse.plugin.graph;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/12.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class RemoteConnector extends Thread {

    InputStream is;

    OutputStream os;

    static final Logger logger = Logger.getLogger(RemoteConnector.class.getName());


    public RemoteConnector() {
    }


    public RemoteConnector(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }


    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = this.is;
            byte[] b = new byte[8192];
            int length;
            out = this.os;
            while ((length = in.read(b)) > 0) {
                out.write(b, 0, length);
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


    public void startX() {
        new Sb1().start();
        new Sb2().start();
    }


    public static void main(String[] args) {
        new RemoteConnector().startX();
    }


    class Sb1 extends Thread {

        Socket local;


        public void startX() throws Exception {
            int port = 1080;
            ServerSocket ss = new ServerSocket(port);
            logger.info("监听端口:" + port);
            while (true) {
                Socket s = ss.accept();
                String ip = s.getInetAddress().getHostAddress();
                if ("127.0.0.1".equals(ip)) {
                    logger.info("建立本地连接成功!");
                    this.local = s;
                } else {
                    if (this.local != null) {
                        try {
                            logger.info("开始传输数据到:" + ip);
                            (new RemoteConnector(s.getInputStream(), this.local.getOutputStream())).start();
                            (new RemoteConnector(this.local.getInputStream(), s.getOutputStream())).start();
                        } catch (Exception e) {
                            throw e;//抛出异常，等待重新连接
                        }
                    }
                }
            }
        }


        public void run() {
            try {
                startX();
            } catch (Exception e) {
                logger.info(this.getClass() + "-通信异常:" + e.toString() + "\n重新启动....");
            }
        }

    }


    class Sb2 extends Thread {


        public void startX() throws Exception {
            int port = 9527;
            String forwardHost = "127.0.0.1";
            int forwardPort = 1080;
            ServerSocket ss = new ServerSocket(port);
            logger.info("监听端口:" + port);
            while (true) {
                Socket s = ss.accept();
                String ip = s.getInetAddress().getHostAddress();
                logger.info("主机:" + ip + ",连接成功!");
                Socket socket = new Socket(forwardHost, forwardPort);
                logger.info("转发到:" + forwardHost + ",端口:" + forwardPort);
                (new RemoteConnector(socket.getInputStream(), s.getOutputStream())).start();
                (new RemoteConnector(s.getInputStream(), socket.getOutputStream())).start();
            }
        }


        public void run() {
            try {
                startX();
            } catch (Exception e) {
                logger.info(this.getClass() + "-通信异常:" + e.toString() + "\n重新启动....");
            }
        }

    }
}