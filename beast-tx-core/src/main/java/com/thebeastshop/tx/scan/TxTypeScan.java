/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/11
 */
package com.thebeastshop.tx.scan;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.enums.TxTypeEnum;
import com.thebeastshop.tx.utils.MethodUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 这种策略只能在java8之上使用，所以放弃-_-|||
 * 公用扫描逻辑
 * 扫描所有带有@Transactional标签的类（先扫类上的再扫方法上的）
 * 如果带有@TxType标签，则把@TxType上的事务策略通过反射设置到@Transactional的noRollbackForClassName属性上。
 * 因为Spring的TransactionInterceptor类并拿不到开启事务的method。所以通过通过这样的方式可以完美传递事务策略
 * 开发者无感知，而且不影响@Transactional标签的任何功能使用
 */
@Deprecated
public class TxTypeScan {

    private static TxTypeScan txTypeScan = new TxTypeScan();

    public static TxTypeScan loadInstance(){
        return txTypeScan;
    }

    /**
     * 先看类上有没有@Transactional标签，如果有，则从类上去寻找@BeastTx标签，默认是TCC事务策略
     * 类上如果没有@Transactional标签，则遍历类中的公有方法，从方法上去找@Transactional标签。和类上的逻辑一致
     * @param bean
     * @throws Throwable
     */
    public void process(Object bean) throws Throwable{
        Class clazz = bean.getClass();
        Annotation annotation1 = clazz.getAnnotation(Transactional.class);
        Annotation annotation2 = clazz.getAnnotation(BeastTx.class);
        if(annotation1 == null){
            Method[] publicMethods = MethodUtil.getOwnPublicMethod(clazz);
            for(Method m : publicMethods){
                Annotation mAnnotation1 = m.getAnnotation(Transactional.class);
                if(mAnnotation1 == null){
                    continue;
                }
                Transactional txAnnotation = (Transactional)mAnnotation1;
                Annotation mAnnotation2 = m.getAnnotation(BeastTx.class);

                if(mAnnotation2 != null){
                    BeastTx beastTxAnnotation = (BeastTx)mAnnotation2;
                    TxTypeEnum txType = beastTxAnnotation.value();
                    putTxType2Transactional(txAnnotation,txType);
                    System.out.println(txAnnotation);
                }
            }
        }else{
            Transactional txAnnotation = (Transactional)annotation1;

            if(annotation2 != null){
                BeastTx beastTxAnnotation = (BeastTx)annotation2;
                TxTypeEnum txType = beastTxAnnotation.value();
                putTxType2Transactional(txAnnotation,txType);
            }
        }

    }

    private void putTxType2Transactional(Transactional transactional, TxTypeEnum txType) throws Exception{
        InvocationHandler h = Proxy.getInvocationHandler(transactional);
        Field field = h.getClass().getDeclaredField(TxConstant.TRANSACTIONAL_MEMBER_VALUES);
        field.setAccessible(true);
        Map memberValues = (Map) field.get(h);
        String[] noRollbackForClassNameArray = (String[])memberValues.get(TxConstant.TRANSACTIONAL_SYS_SET_FIELD);
        noRollbackForClassNameArray = ArrayUtils.add(noRollbackForClassNameArray,txType.getValue());
        memberValues.put(TxConstant.TRANSACTIONAL_SYS_SET_FIELD, noRollbackForClassNameArray);
    }

    public static void main(String[] args) throws Throwable{
        Demo demo = new Demo();
        TxTypeScan.loadInstance().process(demo);
        Annotation a = demo.getClass().getAnnotation(Transactional.class);
        if(a != null){
            Transactional txAn = (Transactional)a;
            System.out.println("class transactional hashcode:" + txAn.hashCode());
            for(String str : txAn.noRollbackForClassName()){
                System.out.println(str);
            }
        }

        System.out.println("--------------------------------------------------");
        for(Method m : MethodUtil.getOwnPublicMethod(demo.getClass())){
            System.out.println(m.getName());
            Annotation a2 = m.getAnnotation(Transactional.class);
            if(a2 != null){
                Transactional txAnnotation = (Transactional)a2;
                System.out.println("method transactional hashcode:" + txAnnotation.hashCode());
                for(String str : txAnnotation.noRollbackForClassName()){
                    System.out.println(str);
                }
            }
        }

    }
}
