package org.tsinghuatj.framework.utils;

public class IPUtils {

	public static Long encoded(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}
		return result;
	}

	public static String decoded(Long ip) {
		StringBuilder sb = new StringBuilder(15);
		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ip & 0xff));
			if (i < 3) {
				sb.insert(0, '.');
			}
			ip = ip >> 8;
		}
		return sb.toString();
	}
}
