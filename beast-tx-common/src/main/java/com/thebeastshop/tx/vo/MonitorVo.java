/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/12/7
 */
package com.thebeastshop.tx.vo;

import com.thebeastshop.tx.enums.TxContextStateEnum;

import java.io.Serializable;

public class MonitorVo implements Serializable {

    private String nodeIp;

    private String nodeHostName;

    private Long txId;

    private String txClassName;

    private String txMethodName;

    private Object[] txArgs;

    private TxContextStateEnum txContextState;

    private String exceptionMessage;

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getNodeHostName() {
        return nodeHostName;
    }

    public void setNodeHostName(String nodeHostName) {
        this.nodeHostName = nodeHostName;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public String getTxClassName() {
        return txClassName;
    }

    public void setTxClassName(String txClassName) {
        this.txClassName = txClassName;
    }

    public String getTxMethodName() {
        return txMethodName;
    }

    public void setTxMethodName(String txMethodName) {
        this.txMethodName = txMethodName;
    }

    public Object[] getTxArgs() {
        return txArgs;
    }

    public void setTxArgs(Object[] txArgs) {
        this.txArgs = txArgs;
    }

    public TxContextStateEnum getTxContextState() {
        return txContextState;
    }

    public void setTxContextState(TxContextStateEnum txContextState) {
        this.txContextState = txContextState;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
