package com.fujistu.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final int timeOut = 10000; // ��ʱӦ����3������
	private static final int pingTimes = 3; // ping �Ĵ���

	/**
	 * ip��ַ�Ƿ�Ϸ�
	 * 
	 * @return
	 */
	public static boolean validIP(String ip) {

		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
				+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return ip !=null && pattern.matcher(ip).matches();
	}

	/**
	 * ip��ַ�Ƿ�ɴ�
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
	 * ipAddress�Ƿ�pingͨ
	 * @param ipAddress
	 * @return
	 */
	 public static boolean ping(String ipAddress) {  
	        BufferedReader in = null;  
	        Runtime r = Runtime.getRuntime();  // ��Ҫִ�е�ping����,��������windows��ʽ������  
	        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;  
	        try {   // ִ�������ȡ���  
//	            System.out.println(pingCommand);   
	            Process p = r.exec(pingCommand);   
	            if (p == null) {    
	                return false;   
	            }
	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // ���м�����,�������Ƴ���=23ms TTL=62�����Ĵ���  
	            int connectedCount = 0;   
	            String line = null;   
	            while ((line = in.readLine()) != null) {    
	                connectedCount += getCheckResult(line);   
	            }   // �����������=23ms TTL=62����������,���ֵĴ���=���Դ����򷵻���  
	            return connectedCount == pingTimes;  
	        } catch (Exception ex) {   
	            ex.printStackTrace();   // �����쳣�򷵻ؼ�  
	            return false;  
	        } finally {   
	            try {    
	                in.close();   
	            } catch (IOException e) {    
	                e.printStackTrace();   
	            }  
	        }
	    }
	    //��line����=18ms TTL=16����,˵���Ѿ�pingͨ,����1,��t����0.
	    private static int getCheckResult(String line) {  // System.out.println("����̨����Ľ��Ϊ:"+line);  
	        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
	        Matcher matcher = pattern.matcher(line);  
	        while (matcher.find()) {
	            return 1;
	        }
	        return 0; 
	    }
}
