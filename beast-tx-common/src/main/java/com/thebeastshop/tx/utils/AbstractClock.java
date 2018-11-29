/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/14
 */
package com.thebeastshop.tx.utils;

public abstract class AbstractClock {

	/**
	 * 创建系统时钟.
	 * 
	 * @return 系统时钟
	 */
	public static AbstractClock systemClock() {
		return new SystemClock();
	}

	/**
	 * 返回从纪元开始的毫秒数.
	 * 
	 * @return 从纪元开始的毫秒数
	 */
	public abstract long millis();

	private static final class SystemClock extends AbstractClock {

		@Override
		public long millis() {
			return System.currentTimeMillis();
		}
	}
}
