package com.fujistu.router;

public class Router {

	public static void main(String[] args) {

		BusinessController businessController = new BusinessController();

		if (args.length > 0) {

			switch (args[0]) {
			case "-r"://接收pad文件
				businessController.receiveFilesFromPad();
				break;
			case "-ip"://发送给服务器
				businessController.sendFile2ServerAndReceiveResult(args[1]);
				break;
			case "-s"://将结果发给pad
				businessController.sendResult2Pad();
				break;

			default:
				break;
			}
		} else {
			System.out.println("ERRO:Invalid Parameter!");
		}

//		if (args.length == 1) {
//			String ip = args[0];
//			if (Util.validIP(ip)) {
//				if (Util.ping(ip)) {
//
//					// String p = wlan.getAbsolutePath() ;
//					// new Start(ip, p);
//				} else {
//					System.out.println("ERRO:IP address is unreachable!");
//				}
//			} else {
//				System.out.println("ERRO:Invalid IP Address!");
//			}
//		} else {
//			System.out.println("ERRO:Invalid Parameter!");
//		}
	}
}
