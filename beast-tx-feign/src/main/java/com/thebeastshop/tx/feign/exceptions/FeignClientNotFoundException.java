/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.feign.exceptions;

import org.springframework.beans.BeansException;

/**
 * FeignClient依赖不存在异常
 */
public class FeignClientNotFoundException extends BeansException {

    public FeignClientNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FeignClientNotFoundException(String msg) {
        super(msg);
    }
}
