/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/17
 */
package com.thebeastshop.tx.test.service;

public interface DemoService {

	String tryTest1(String name);
	
    String test1(String name);

    String test2(String name, Integer age);

    String test3(Long id);
	
	void cancelTest1(String str);
	
	void cancelTest2(String str);
}
