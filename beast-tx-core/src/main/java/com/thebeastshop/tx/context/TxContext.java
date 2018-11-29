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

    private String nodeId;

    private Long txId;

    private TxContextStateEnum txContextState;

    private final ConcurrentLinkedDeque<InvokeContent> invokeQueue = new ConcurrentLinkedDeque<>();

    public TxContext(String nodeId, Long txId, TxContextStateEnum txContextState) {
        this.nodeId = nodeId;
        this.txId = txId;
        this.txContextState = txContextState;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

}
