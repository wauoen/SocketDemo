package com.fujistu.router;

import java.io.File;

/**
 * @author user
 */
public class BusinessController {

	public static final String APP_PATH = "WlanPlan";
	public String mAppPath = "";

	public BusinessController() {
		File root = new File("");
		String rootpath = root.getAbsolutePath();
		File wlan = new File(rootpath + "\\" + APP_PATH);
		if (!wlan.exists()) {
			wlan.mkdirs();
		}
		this.mAppPath = wlan.getAbsolutePath();
	}

	/**
	 * ����pad����files
	 */
	public void receiveFilesFromPad() {

		new Start(mAppPath).receiveFilesFromPad();
	}

	/**
	 * ���ļ����������������ձ�����
	 */
	public void sendFile2ServerAndReceiveResult(String serverIP) {

		if (Util.validIP(serverIP)) {
			if (Util.ping(serverIP)) {
				Thread thread = new Thread(new ClientThread(mAppPath, serverIP));
				thread.start();
			} else {
				System.out.println("ERRO:IP address is unreachable!");
			}
		} else {
			System.out.println("ERRO:Invalid IP Address!");
		}

		
	}

	/**
	 * ������������pad
	 */
	public void sendResult2Pad() {
		new ImageLoadClient(mAppPath).connect();
	}

}
