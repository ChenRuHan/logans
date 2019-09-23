package com.bkcc.logans.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 【描 述】：物理机器工具类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 13:28
 */
public class ComputerUtils {

    /**
     * 【描 述】：获取本机IP地址
     *
     * @param
     * @return Set<String>
     * @author 陈汝晗
     * @since 2019/9/21 15:36
     */
    public static Set<String> getIPSet() {
        Set<String> ipList = new HashSet<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        String ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (Exception e) {
        }
        return ipList;
    }

    /**
     * 【描 述】：获取本地MAC地址
     *
     * @param
     * @return java.lang.String
     * @author 陈汝晗
     * @since 2019/9/21 13:34
     */
    public static String getMacAddress() {
        String mac_s = "";
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;
                mac_s = hexByte(mac[0]) + "-" + hexByte(mac[1]) + "-"
                        + hexByte(mac[2]) + "-" + hexByte(mac[3]) + "-"
                        + hexByte(mac[4]) + "-" + hexByte(mac[5]);
            }
        } catch (Exception e1) {
        }
        return mac_s;
    }

    public static String hexByte(byte b) {
        String s = "000000" + Integer.toHexString(b);
        return s.substring(s.length() - 2);
    }


}///:~
