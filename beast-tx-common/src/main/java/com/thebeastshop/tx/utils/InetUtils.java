/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/10
 */
package com.thebeastshop.tx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 网络工具包
 */
public class InetUtils {

    private static final Logger log = LoggerFactory.getLogger(InetUtils.class);

    public static InetAddress getAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }
}
