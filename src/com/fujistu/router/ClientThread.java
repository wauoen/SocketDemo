package com.fujistu.router;


/**
 * Created by user on 2015/7/28.
 */



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TCP锟侥硷拷锟斤拷锟斤拷锟竭筹拷
 */
public class ClientThread implements Runnable {

    private final String TAG = "ClientThread";

    private Socket s;
    BufferedReader br = null;
    OutputStream os = null;
    private static File resDir;
    private DataOutputStream dos;
    public File[] files;
    private FileInputStream fis;
    private DataInputStream dis;
    private File resDir1;
    private FileOutputStream picResult;
//        public String  ip ="192.168.31.135";
    public String ip = "";
    public String rootPath = "";
        
    
    public ClientThread(String rootPath,String ip){
    	
    	this.ip = ip;
    	this.rootPath = rootPath;
    }


    public void run() {
        try {
//        	System.out.println("将results文件夹下的文件传到服务器！");
            files = FileList();
            int j = files.length;
            for (int i = 0; i < j; i++) {

                Socket socket = new Socket(ip, 8888);

                File fi = new File(files[i].toString());
                DataInputStream dis = new DataInputStream(new BufferedInputStream(
                        new FileInputStream(fi.getPath())));

                DataOutputStream ps = new DataOutputStream(socket.getOutputStream());

                ps.writeUTF(fi.getName());
                ps.flush();
                ps.writeLong(fi.length());
                ps.flush();

                int bufferSize = 8000;
                byte[] buf = new byte[bufferSize];

                while (true) {
                    int read = 0;

                    if (dis != null) {
                        read = dis.read(buf);
                    }

                    if (read == -1) {
                        break;
                    }
                    ps.write(buf, 0, read);
                }
                ps.flush();
                dis.close();
                socket.close();
            }


            Socket socket = new Socket(ip, 8888);

            DataOutputStream ps = new DataOutputStream(socket.getOutputStream());

            ps.writeUTF("STOP");
            System.out.println("Message:Send Results Files To Server Successfully!");
            
            ps.flush();
            ps.close();
            socket.close();
            new Thread(new ServerThread(this.rootPath)).start();//接收计算结果
            new ResultProgressServer(this.rootPath).start();//接收进度
//            new ProgressClientThread(this.rootPath).start();// 将进度发送给pad
//            System.exit(0);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File[] FileList() {

        String appname = this.rootPath+"\\"+"results\\";
        resDir = new File(appname);
        File[] filelist = resDir.listFiles();
        return filelist;
    }

    public void setIP(String serverIP) {
        this.ip = serverIP;
    }

}






