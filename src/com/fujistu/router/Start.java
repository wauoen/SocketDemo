package com.fujistu.router;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Start {

	private String mPath = "";
	private File mFile = null;

	public Start(String path) {
		this.mPath = path;
		mFile = new File(path + "\\" + "results\\");
		if (!mFile.exists()) {
			mFile.mkdir();
		}

	}

	public void receiveFilesFromPad() {
		DataInputStream inputstream = null;
		Socket socket = null;
		String judgeBuffer = null;

		try {
			ServerSocket ss = new ServerSocket(8888);
			System.out.println("Message:Server Started!");
			int index = 0;
			while (true) {
				socket = ss.accept();
				System.out.println(++index + "");
				System.out.println("Message:Connected With The Pad Sucessfully!");
				// System.out.println("链接成功,开始接收pad数据，保存到results文件夹下...");
				try {
					String ip = socket.getInetAddress().getHostAddress();
					File ipfile = new File(mPath + "\\" + "ip.txt");

					if (!ipfile.exists()) {
						ipfile.createNewFile();
					}
					FileWriter fw = new FileWriter(ipfile, false);
					fw.write(ip);
					fw.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				String savePath = mFile.getAbsolutePath() + "\\";

				int bufferSize = 1024;
				byte[] buf = new byte[bufferSize];
				int passedlen = 0;
				long len = 0;
				inputstream = new DataInputStream(socket.getInputStream());
				try {
					judgeBuffer = inputstream.readUTF();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println(judgeBuffer);
				if (judgeBuffer.equals("STOP")) {

					System.out.println("Message:Data Receive Completed!");
					socket.close();
					System.exit(0);
				} else {

					try {
						savePath += judgeBuffer;

						File savefile = new File(savePath);
						if (!savefile.exists()) {
							savefile.createNewFile();
						}

						DataOutputStream fileOut = new DataOutputStream(
								new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(savePath))));
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
