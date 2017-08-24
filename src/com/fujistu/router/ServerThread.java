 package com.fujistu.router;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by user on 2015/7/31.
 */
public class ServerThread implements Runnable {

    private String TAG = "ServerThread";
    private Socket socket;
    private DataInputStream inputstream;
    private File resDir;
    private String judge;
    private String rootPath = "";
    public ServerThread(String rootPath) {
    	this.rootPath = rootPath;
	}
    

    @Override
    public void run() {

        try {
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                socket = ss.accept();
                System.out.println("Message:Start Receive Calculation Results... ");
//                System.out.println("链接成功，开始接收服务器端发送的imgload文件");
//                serverReceiveFromClient(socket);
/*                if(inputstream.readUTF() == "STOP"){
                    break;
                }*/

                String appname = this.rootPath+"\\"+"imgload";
                resDir = new File(appname);
                if (!resDir.exists())
                    resDir.mkdir();
                String savePath = resDir.getPath().toString() + "/";
                int bufferSize = 8000;
                byte[] buf = new byte[bufferSize];
                int passedlen = 0;
                long len = 0;
                inputstream = new DataInputStream(socket.getInputStream());
                judge = inputstream.readUTF();
//                System.out.println("judge0------:"+judge);
                if (judge.equals("AABBCC")) {
                    ss.close();
                    System.out.println("Message:Receive Calculation Results Successfully! ");
                    System.exit(0);
//                    System.out.println("AABBCC");
//                    new ImageLoadClient(this.rootPath).connect();
                    
                    break;
                }
                savePath += judge;
                DataOutputStream fileOut = new DataOutputStream(
                        new BufferedOutputStream(new BufferedOutputStream(
                                new FileOutputStream(savePath))));
                len = inputstream.readLong();
                while (true) {
                    int read = 0;
                    if (inputstream != null) {
                        read = inputstream.read(buf);
                    }
                    passedlen += read;
                    if (read == -1) {
                        break;
                    }
                    fileOut.write(buf, 0, read);
                }
                fileOut.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
