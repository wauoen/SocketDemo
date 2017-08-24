package com.fujistu.router;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Socket;

public class ProgressClientThread extends Thread {

	String initContent = null;
	String currentContent = null;
	File file = null;
	FileReader fileReader = null;
	BufferedReader bufferedReader = null;
	int totalNumber = -1;
	long initFileLen = 0l;
	long currentFileLen = 0l;
	String ip = "";
	private String rootPath = "";
	
	public ProgressClientThread(String rootPath){
		
		this.rootPath = rootPath;
	}

	private String getip() {

		String ip = "";
		try {
			File file = new File(this.rootPath +"//"+"ip.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);

			ip = br.readLine();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return ip;

	}

	public void run() {
		Socket socket = null;
		PrintStream print = null;
		try {
			ip = getip();
			init();
			// Socket socket = null;
			// Socket socket = new Socket("127.0.0.1", 30001);
			try {
				socket = new Socket(ip, 9999);
//				System.out.println("链接成功，将进度发给pad");
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			while (socket == null || !socket.isConnected()) {
				try {
					socket = new Socket(ip, 9999);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// PrintStream
			print = new PrintStream(socket.getOutputStream());
			int i = 0;
			while (true) {

				// if ( initContent != null) {
				// int currentNum = Integer.parseInt(currentContent);
				//
//				if (fileIsChanged()) {
					// System.out.println("send data!");
					// String index = getI();
//					System.out.println("进度发生变化");
					String index = readFile();
//					System.out.println(index);
					if (index != null && !index.equals("")) {

						try {
							clientSend2Server(socket, index + "", print);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (index != null && index.equals("99")) {
						socket.close();
						print.close();
						saveProgress2Txt("");
						return;
					}

//				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				print.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 将接收的进度保存到txt中
     */
    public void saveProgress2Txt(String prograss){
		try {
			
			File ipfile = new File(this.rootPath + "//"+"progress.txt");
			FileWriter fw;
			fw = new FileWriter(ipfile, false);
			fw.write(prograss);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

	void init() throws IOException {
		file = new File(this.rootPath + "//"+"progress.txt");
		while (true) {
			if (file.exists()) {

				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				initFileLen = file.length();
				bufferedReader.close();
				fileReader.close();
//				System.out.println("文件存在...");
				return;
			} else {
			}
		}
	}

	public boolean fileIsChanged() throws IOException {

		currentFileLen = file.length();
		if (initFileLen != currentFileLen) {
			initFileLen = currentFileLen;
			return true;
		}
		return false;
	}

	public String readFile() throws IOException {
		if (file.exists()) {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String content = bufferedReader.readLine();
			bufferedReader.close();
			fileReader.close();
			return content;
		}
		return null;
	}

	private void clientSend2Server(Socket socket, String i, PrintStream print) throws IOException {
		// System.out.println("send data to server");
		print.println(i);
		// System.out.println("send data to server!" + i);

	}

	private String getI() {
		RandomAccessFile rf = null;
		try {

			rf = new RandomAccessFile(file.getName(), "r");
			if (rf != null) {
				long len = rf.length();
				long start = rf.getFilePointer();
				long nextend = start + len - 1;
				String line;
				int c = -1;
				if (nextend - 1 > 0) {
					while (true) {
						c = rf.read();
						System.out.println("read char->" + c);
						if (c == '\n') {
							line = rf.readLine();
							break;
						}
						nextend--;

					}
					// rf.seek(nextend-4);
					// line = rf.readLine();
					if (line != null) {
//						System.out.println("line ->" + line);
						if (line.contains(" ")) {
							String[] strs = line.split(" ");
							if (strs.length >= 4) {
								String number = strs[2];
								totalNumber = Integer.parseInt(strs[3]);
								return ((int) ((Integer.parseInt(number) / (float) totalNumber) * 100)) + "";
							}
						}
						// else{
						// String number = line;
						// return number;
						// }

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String readLastLine(File file, String charset) throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				if (charset == null) {
					return new String(bytes);
				} else {
					return new String(bytes, charset);
				}
			}
		} catch (Exception e) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

}