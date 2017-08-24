package com.fujistu.router;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;

/**
 * Created by user on 2016/10/9.
 */
public class ResultProgressServer extends Thread {

	private String TAG = "ResultProgressServer";
	private String rootPath = "";

	public ResultProgressServer(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(8899);
			while (true) {
				Socket socket = ss.accept();
				// System.out.println("链接成功，开始接收进度！");
				serverReceiveFromClient(socket);
				ss.close();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void serverReceiveFromClient(Socket socket) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String line = br.readLine();
				// System.out.println(line);
				saveProgress2Txt(line);
				if (line.equals("99")) {
					br.close();
					socket.close();
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 将接收的进度保存到txt中
	 */
	public void saveProgress2Txt(String progress) {
		try {

			try {
				System.out.println("Message:Current Progress-->" + (int) (Integer.parseInt(progress) * (0.9f)));
				
			} catch (Exception e) {
			}

			File ipfile = new File(this.rootPath + "//" + "progress.txt");
			FileWriter fw;
			fw = new FileWriter(ipfile, false);
			fw.write(progress);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
