/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/13
 */
package com.thebeastshop.tx.proxy;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.enums.TxTypeEnum;
import com.thebeastshop.tx.exceptions.NoInterfaceDefineException;
import com.thebeastshop.tx.scan.demo.Demo;
import com.thebeastshop.tx.scan.demo.IDemo;
import com.thebeastshop.tx.utils.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务策略扫描逻辑
 * 返回动态代理类对象，如果没有事务标签，则返回null
 */
public class TxTypeScanLogic {

    private final Logger log = LoggerFactory.getLogger(TxTypeScanLogic.class);

    private static TxTypeScanLogic txTypeScanLogic = new TxTypeScanLogic();

    public static TxTypeScanLogic loadInstance(){
        return txTypeScanLogic;
    }

    public Object process(Object bean){
        Class clazz = bean.getClass();
        Annotation transactionalAnno = clazz.getAnnotation(Transactional.class);

        Map<Method, TxTypeEnum> methodTxTypeMap = new HashMap<>();
        if(transactionalAnno != null){
            //如果能在class上找到@Transactional标签，则优先寻找这个类上所有public方法上的@BeastTx标签。
            //如果在方法上发现了@BeastTx标签，就用方法上的，如果方法上没有，则用类上的(类上如果没有@BeastTx标签，则默认为TCC策略)
            //然后组织起来放到map中，传递给动态代理类处理

            Annotation classAnno = clazz.getAnnotation(BeastTx.class);
            TxTypeEnum classTxType = null;
            if(classAnno != null){
                BeastTx classBeastTxAnno = (BeastTx)classAnno;
                classTxType = classBeastTxAnno.value();
            }else{
                classTxType = TxTypeEnum.TCC;
            }

            BeastTx methodBeastTxAnno = null;
            TxTypeEnum finalTxType = null;
            for(Method m : MethodUtil.getOwnPublicMethod(clazz)){
                methodBeastTxAnno = m.getAnnotation(BeastTx.class);
                if(methodBeastTxAnno == null){
                    finalTxType = classTxType;
                }else{
                    finalTxType = methodBeastTxAnno.value();
                }

                Method interfaceMethod = MethodUtil.convertToInterfaceMethod(m);
                if(interfaceMethod == null){
                    throw new NoInterfaceDefineException("事务Bean必须得申明接口");
                }
                methodTxTypeMap.put(interfaceMethod, finalTxType);
                log.info("找到事务方法[{}],事务策略是{}",m.getName(),finalTxType.getValue());
            }
        }else{
            //如果类上没有发现@Transactional标签，则遍历每一个方法，看看方法上是否有@Transactional标签
            //如果有，则寻找方法上的@BeastTx标签。如果有。则用，如果没有，默认为TCC策略

            Transactional methodTransactionalAnno = null;
            BeastTx methodBeastTxAnno = null;
            TxTypeEnum finalTxType = null;
            for(Method m : MethodUtil.getOwnPublicMethod(clazz)){
                methodTransactionalAnno = m.getAnnotation(Transactional.class);
                if(methodTransactionalAnno == null){
                    continue;
                }
                methodBeastTxAnno = m.getAnnotation(BeastTx.class);
                if(methodBeastTxAnno == null){
                    finalTxType = TxTypeEnum.TCC;
                }else {
                    finalTxType = methodBeastTxAnno.value();
                }

                Method interfaceMethod = MethodUtil.convertToInterfaceMethod(m);
                if(interfaceMethod == null){
                    throw new NoInterfaceDefineException("事务Bean必须得申明接口");
                }
                methodTxTypeMap.put(interfaceMethod, finalTxType);
                log.info("找到事务方法[{}],事务策略是{}",m.getName(),finalTxType.getValue());
            }
        }

        if(!methodTxTypeMap.isEmpty()){
            //拿到这个bean的interface
            Class[] interfaceClazzArray = bean.getClass().getInterfaces();

            //为这个类创建动态代理类
            TxTypeProxyHandler proxyHandler = new TxTypeProxyHandler(methodTxTypeMap,bean);
            Object proxyObject = Proxy.newProxyInstance(proxyHandler.getClass().getClassLoader(),
                    interfaceClazzArray, proxyHandler);
            log.info("为class[{}]创建事务策略动态代理类",clazz.getName());
            return proxyObject;
        }
        return null;
    }

    public static void main(String[] args) {
        try{
            IDemo demo = new Demo();
            TxTypeScanLogic txTypeScanLogic = TxTypeScanLogic.loadInstance();
            IDemo demoProxy = (IDemo) txTypeScanLogic.process(demo);
            demoProxy.test2();
        }catch (Throwable t){
            t.printStackTrace();
        }

    }
}
