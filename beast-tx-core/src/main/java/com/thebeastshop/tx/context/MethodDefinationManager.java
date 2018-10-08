/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/7
 */
package com.thebeastshop.tx.context;

import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.content.MethodContent;
import com.thebeastshop.tx.enums.TccMethodTypeEnum;
import com.thebeastshop.tx.hook.CancelInvokeHook;
import com.thebeastshop.tx.utils.MethodUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法定义管理器
 */
public class MethodDefinationManager {

    private static Map<String, MethodContent> methodContentMap = new HashMap<>();

    private static CancelInvokeHook cancelInvokeHook;

    public static void registerMethod(Class clazz){
        Method[] methods = MethodUtil.getOwnPublicMethod(clazz);
        MethodContent methodContent = null;
        for(Method m : methods){
            if(getMethodType(m).equals(TccMethodTypeEnum.CONFIRM)){
                Method tryMethod = findTryMethod(m,methods);
                Method cancelMethod = findCancelMethod(m,methods);
                methodContent = new MethodContent(tryMethod,m,cancelMethod);
                methodContentMap.put(MethodUtil.getMethodKey(m),methodContent);
            }
        }
    }

    public static MethodContent getMethodCentent(Method m){
        return methodContentMap.get(MethodUtil.getMethodKey(m));
    }

    public static void registerCancelInvokeHook(CancelInvokeHook cancelInvokeHook){
        MethodDefinationManager.cancelInvokeHook = cancelInvokeHook;
    }

    private static TccMethodTypeEnum getMethodType(Method m){
        if(m.getName().startsWith(TxConstant.TRY_PREFIX)){
            return TccMethodTypeEnum.TRY;
        }else if(m.getName().startsWith(TxConstant.CANCEL_PREFIX)){
            return TccMethodTypeEnum.CANCEL;
        }else{
            return TccMethodTypeEnum.CONFIRM;
        }
    }

    private static Method findTryMethod(Method confirmMethod,Method[] methods){
        for(Method m : methods){
            if(getMethodType(m).equals(TccMethodTypeEnum.TRY)
                    && m.getName().equals(MethodUtil.mergeMethodName(TxConstant.TRY_PREFIX,confirmMethod.getName()))
                    && MethodUtil.matchParameterTypes(m,confirmMethod,true)){
                return m;
            }
        }
        return null;
    }

    private static Method findCancelMethod(Method confirmMethod,Method[] methods){
        for(Method m : methods){
            if(getMethodType(m).equals(TccMethodTypeEnum.CANCEL)
                    && m.getName().equals(MethodUtil.mergeMethodName(TxConstant.CANCEL_PREFIX,confirmMethod.getName()))
                    && MethodUtil.matchParameterTypes(m,confirmMethod,false)){
                return m;
            }
        }
        return null;
    }
}
