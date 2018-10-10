/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/10
 */
package com.thebeastshop.tx.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * base64算法
 */
public class Base64Utils {
	
	public static String simpleEncode(byte[] data) {
		return new BASE64Encoder().encode(data);
	}
	
	public static byte[] simpleDecode(String str) {
		try {
			return new BASE64Decoder().decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public static String encode(String str){
    	return new BASE64Encoder().encode(str.getBytes());
    }
    
    public static String decode(String str){
    	try {
			return new String(new BASE64Decoder().decodeBuffer(str));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
          
}  