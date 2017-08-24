package com.fujistu.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final int timeOut = 10000; // 超时应该在3钞以上
	private static final int pingTimes = 3; // ping 的次数

	/**
	 * ip地址是否合法
	 * 
	 * @return
	 */
	public static boolean validIP(String ip) {

		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
				+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return ip !=null && pattern.matcher(ip).matches();
	}

	/**
	 * ip地址是否可达
	 * 
	 * @return
	 */
	public static boolean isReachable(String ip) {

		boolean status = false;
		if (ip != null) {
			try {
				status = InetAddress.getByName(ip).isReachable(timeOut);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	public static void ping02(String ipAddress) throws Exception {
		String line = null;
		try {
			Process pro = Runtime.getRuntime().exec("ping " + ipAddress);
			BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while ((line = buf.readLine()) != null)
				System.out.println(line);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * ipAddress是否ping通
	 * @param ipAddress
	 * @return
	 */
	 public static boolean ping(String ipAddress) {  
	        BufferedReader in = null;  
	        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令  
	        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;  
	        try {   // 执行命令并获取输出  
//	            System.out.println(pingCommand);   
	            Process p = r.exec(pingCommand);   
	            if (p == null) {    
	                return false;   
	            }
	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数  
	            int connectedCount = 0;   
	            String line = null;   
	            while ((line = in.readLine()) != null) {    
	                connectedCount += getCheckResult(line);   
	            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真  
	            return connectedCount == pingTimes;  
	        } catch (Exception ex) {   
	            ex.printStackTrace();   // 出现异常则返回假  
	            return false;  
	        } finally {   
	            try {    
	                in.close();   
	            } catch (IOException e) {    
	                e.printStackTrace();   
	            }  
	        }
	    }
	    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
	    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);  
	        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
	        Matcher matcher = pattern.matcher(line);  
	        while (matcher.find()) {
	            return 1;
	        }
	        return 0; 
	    }
}
