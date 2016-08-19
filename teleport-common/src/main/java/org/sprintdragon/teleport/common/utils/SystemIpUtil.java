package org.sprintdragon.teleport.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * User: bjshijianwei
 * Date: 13-4-8
 * Time: 上午9:51
 */
public class SystemIpUtil {

    /**
     * 获取本机ip 包含127.0.0.1
     *
     * @return
     */
    public static Set<String> getIp() {
        Set<String> set = new HashSet<String>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        set.add(inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return set;
    }

    /**
     * 获取有效ip
     *
     * @return
     */
    public static String getRealIp() {
        Set<String> ips = getIp();
        for (String ip : ips) {
            if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
                continue;
            }
            return ip;
        }
        return "127.0.0.1";
    }

    /**
     * 获取ip 末尾
     *
     * @return
     */
    public static int getIpPostfix() {
        try {
            String ip = getRealIp();
            String str = ip.substring(ip.lastIndexOf(".") + 1);
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
