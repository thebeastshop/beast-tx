/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/9/30
 */
package com.thebeastshop.tx.context.content;

import com.thebeastshop.tx.enums.TxTypeEnum;

/**
 * 调用内容
 */
public class InvokeContent {

    private TxTypeEnum txType;

    private MethodContent methodContent;

    private Object[] args;

    private Object result;

    public MethodContent getMethodContent() {
        return methodContent;
    }

    public void setMethodContent(MethodContent methodContent) {
        this.methodContent = methodContent;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public TxTypeEnum getTxType() {
        return txType;
    }

    public void setTxType(TxTypeEnum txType) {
        this.txType = txType;
    }
}
