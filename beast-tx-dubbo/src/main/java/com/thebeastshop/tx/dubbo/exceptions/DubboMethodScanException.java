/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.dubbo.exceptions;

/**
 * dubbo方法扫描中异常
 */
public class DubboMethodScanException extends RuntimeException {

    private String message;

    public DubboMethodScanException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
