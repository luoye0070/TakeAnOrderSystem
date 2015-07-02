package lj.internet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-2
 * Time: 下午10:08
 * To change this template use File | Settings | File Templates.
 */
public class PrintClient {
    private String ipAddress;
    private int port;

    private Socket s;
//    private PrintWriter pw;
    private OutputStreamWriter osw=null;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PrintClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PrintClient() {
    }

    public boolean printData(String printData){
        try{
            s = new Socket(this.ipAddress,this.port);
            OutputStreamWriter osw=new OutputStreamWriter(s.getOutputStream(),"gb2312");
            osw.write(printData);
            osw.flush();
//            pw = new PrintWriter(s.getOutputStream(),true);
//            pw.println(printData);
        }catch(IOException ie){
            ie.printStackTrace();
            return false;
        }
        finally {
//            if(pw!=null){
//                pw.close();
//            }
            if(osw!=null){
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return true;
    }
}
