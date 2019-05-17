/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/9/30
 */
package com.thebeastshop.tx.context;

import com.thebeastshop.tx.context.content.InvokeContent;
import com.thebeastshop.tx.enums.TxContextStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 事务上下文
 */
public class TxContext {

    private final static Logger log = LoggerFactory.getLogger(TxContext.class);

    private String nodeIp;

    private String nodeHostName;

    private Long txId;

    private String txClassName;

    private String txMethodName;

    private Object[] txArgs;

    private TxContextStateEnum txContextState;

    private String exceptionMessage;

    private final ConcurrentLinkedDeque<InvokeContent> invokeQueue = new ConcurrentLinkedDeque<>();

    public TxContext(String nodeIp, String nodeHostName, Long txId,String txClassName,String txMethodName,Object[] txArgs,TxContextStateEnum txContextState) {
        this.nodeIp = nodeIp;
        this.nodeHostName = nodeHostName;
        this.txId = txId;
        this.txClassName = txClassName;
        this.txMethodName = txMethodName;
        this.txArgs = txArgs;
        this.txContextState = txContextState;
    }

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

    public void addInvokeQueue(InvokeContent invokeContent){
        this.invokeQueue.add(invokeContent);
    }

    public TxContextStateEnum getTxContextState() {
        return txContextState;
    }

    public void setTxContextState(TxContextStateEnum txContextState) {
        this.txContextState = txContextState;
    }

    public void logInvokeContent(InvokeContent invokeContent){
        log.info("[BEAST-TX]事务ID[{}]记录RPC方法[{}]",
                this.getTxId(),
                invokeContent.getMethodContent().getConfirmMethod().getName());
        invokeQueue.add(invokeContent);
    }

    public void rollback(){
        while (!invokeQueue.isEmpty()) {
            final InvokeContent invokeContent = invokeQueue.pollLast();
            invokeContent.rollback();
        }
    }

    public boolean needRollback(){
        return !invokeQueue.isEmpty();
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

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
