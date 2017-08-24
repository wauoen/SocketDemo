package com.fujistu.router;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageLoadClient {
	private static File resDir;
	
	private String rootPath="";

	public File[] files;

	public ImageLoadClient(String rootPath) {
		// TODO Auto-generated constructor stub
		this.rootPath = rootPath;
	}
	
	
	public String getip() {
		String ip = "";

		try {
			File ipfile = new File(this.rootPath+"\\"+"ip.txt");
			BufferedReader br = new BufferedReader(new FileReader(ipfile));
			ip = br.readLine();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ip;

	}

	public void connect() {

		try {
			String ip = getip();
			files = FileList();
//			System.out.println("将imgload文件发送给pad");
			for (int i = 0; i < files.length; i++) {

				// Socket socket = new Socket("192.168.31.100", 8888);
				Socket socket = new Socket(ip, 9999);
				File fi = new File(files[i].toString());
				System.out.println("Message:Connect With Pad Successfully!");

				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fi.getPath())));

				DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
//				System.out.println(fi.getName());
				ps.writeUTF(fi.getName());
				ps.flush();
				ps.writeLong((long) fi.length());
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

			Socket socket = new Socket(ip, 9999);
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			ps.writeUTF("AABBCC");
//			System.out.println("imgload发送完成");
			 System.out.println("Message:Send Calculation Results To Pad Successfully! ");
			ps.flush();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public File[] FileList() {

		String appname = this.rootPath+"//"+"imgload/";
		resDir = new File(appname);
		// File dir = new File("");
		File[] filelist = resDir.listFiles();
		return filelist;
	}
}
