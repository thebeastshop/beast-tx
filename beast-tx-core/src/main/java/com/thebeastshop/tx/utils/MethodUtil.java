/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/8
 */
package com.thebeastshop.tx.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodUtil {

    public static String getMethodKey(Method method) {
        String name = getMethodNameWithParameterTypes(method.getDeclaringClass(), method);
        return MD5Util.makeMD5(name);
    }

    public static String getMethodKey(Class clazz,
                                      String methodName, Class[] paramTypes) {
        String name = getMethodNameWithParameterTypes(clazz, methodName, paramTypes);
        return MD5Util.makeMD5(name);
    }

    public static String getMethodNameWithParameterTypes(Class clazz, Method method) {
        return getMethodNameWithParameterTypes(clazz, method.getName(), method.getParameterTypes());
    }


    public static String getMethodNameWithParameterTypes(Class clazz,
                                 String methodName, Class[] paramTypes) {
        String name = clazz.getName() + "." +
                methodName + "(";
        StringBuilder builder = new StringBuilder(name);
        for (int i = 0, len = paramTypes.length; i < len; i++) {
            Class pType = paramTypes[i];
            builder.append(pType.getName());
            if (i < len - 1) {
                builder.append(',');
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public static String getMethodNameWithArguments(
            Class clazz, String methodName, Object[] arguments) {
        String name = clazz.getName() + "." +
                methodName + "(";
        StringBuilder builder = new StringBuilder(name);
        for (int i = 0, len = arguments.length; i < len; i++) {
            Object arg = arguments[i];
            builder.append(JSON.toJSONString(arg));
            if (i < len - 1) {
                builder.append(',');
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public static String mergeMethodName(String prefix, String methodName){
        return prefix + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
    }

    public static boolean matchParameterTypes(Method m1, Method m2, boolean wholeMatch){
        Type[] types1 = m1.getGenericParameterTypes();
        Type[] types2 = m2.getGenericParameterTypes();
        if(types1.length == 0 || types2.length == 0){
            return false;
        }
        if(wholeMatch){
            if(types1.length != types2.length){
                return false;
            }else{
                for(int i=0;i<types1.length;i++){
                    if(!types1[i].equals(types2[i])){
                        return false;
                    }
                }
            }
            return true;
        }else{
            int index = Math.min(types1.length,types2.length);
            for(int i=0;i<index;i++){
                if(types1[i].equals(types2[i])){
                    return true;
                }
            }
            return false;
        }
    }

    public static Method[] getOwnPublicMethod(Class clazz){
        List<Method> methodList = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            if(Modifier.isPublic(m.getModifiers())){
                methodList.add(m);
            }
        }
        return methodList.toArray(new Method[methodList.size()]);
    }
}
