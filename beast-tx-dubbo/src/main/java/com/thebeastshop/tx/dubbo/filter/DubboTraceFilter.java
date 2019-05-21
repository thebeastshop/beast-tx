package com.thebeastshop.tx.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.context.TxContextManager;

@Activate(group = {Constants.PROVIDER,Constants.CONSUMER},order = -2000)
public class DubboTraceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TxContext txContext = null;
        if(TxContextManager.hasTxContent()){
            txContext = TxContextManager.getTxContext();
        }
        if(txContext != null){
            invocation.getAttachments().put(TxConstant.TX_TRACE_ID,txContext.getTxId().toString());
        }


        Result result = invoker.invoke(invocation);



        return result;
    }
}
