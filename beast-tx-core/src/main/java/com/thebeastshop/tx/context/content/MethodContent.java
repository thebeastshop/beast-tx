/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/7
 */
package com.thebeastshop.tx.context.content;

import java.lang.reflect.Method;

/**
 * TCC方法内容
 * 如果不是TCC模式的话，则只有confirmMethod
 */
public class MethodContent {

    private Method confirmMethod;

    private Method tryMethod;

    private Method cancelMethod;

    public MethodContent(Method tryMethod, Method confirmMethod, Method cancelMethod) {
        this.tryMethod = tryMethod;
        this.confirmMethod = confirmMethod;
        this.cancelMethod = cancelMethod;
    }

    public Method getTryMethod() {
        return tryMethod;
    }

    public void setTryMethod(Method tryMethod) {
        this.tryMethod = tryMethod;
    }

    public Method getConfirmMethod() {
        return confirmMethod;
    }

    public void setConfirmMethod(Method confirmMethod) {
        this.confirmMethod = confirmMethod;
    }

    public Method getCancelMethod() {
        return cancelMethod;
    }

    public void setCancelMethod(Method cancelMethod) {
        this.cancelMethod = cancelMethod;
    }

    public MethodContentState getMethodContentState(){
        if(confirmMethod != null && cancelMethod != null && tryMethod == null){
            return MethodContentState.CC;
        }else if(confirmMethod != null && cancelMethod == null && tryMethod == null){
            return MethodContentState.C_ONLY;
        }else if(confirmMethod != null && cancelMethod != null && tryMethod != null){
            return MethodContentState.TCC;
        }
        return null;
    }


    public enum MethodContentState{
        CC,C_ONLY,TCC;
    }
}
