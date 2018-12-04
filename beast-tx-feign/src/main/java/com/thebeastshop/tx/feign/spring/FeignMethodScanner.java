/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/31
 */
package com.thebeastshop.tx.feign.spring;

import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.hook.CancelInvokeHook;
import com.thebeastshop.tx.utils.LOGOPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * spring cloud Feign的方法扫描器
 */
public class FeignMethodScanner implements BeanPostProcessor, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(FeignMethodScanner.class);

    private static ApplicationContext applicationContext = null;

    private String feignPackage;

    private AtomicBoolean hasScaned = new AtomicBoolean(false);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if(!hasScaned.getAndSet(true)){
                scanFeignClient();
            }

            if(CancelInvokeHook.class.isAssignableFrom(bean.getClass())){
                MethodDefinationManager.registerCancelInvokeHook((CancelInvokeHook)bean);
            }
        } catch (Exception e) {
            throw new BeanCreationException("[BEAST-TX]扫描Feign package包出现异常");
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void scanFeignClient() throws Exception {
        String classpath = FeignMethodScanner.class.getResource("/").getPath();
        feignPackage = feignPackage.replace(".", File.separator);
        String searchPath = classpath + feignPackage;
        List<String> classPaths = doPath(new File(searchPath));
        for (String s : classPaths) {
            s = s.replace("\\","/");
            s = s.substring(s.lastIndexOf("classes/")).replace("classes/","").replace("/",".").replace(".class","");
            Class cls = Class.forName(s);
            if(cls.getAnnotation(FeignClient.class) != null){
                MethodDefinationManager.registerMethod(cls);
            }
        }
    }

    private static List<String> doPath(File file) {
        List<String> classPaths = new ArrayList<>();
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                classPaths.addAll(doPath(f1));
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
        return classPaths;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FeignMethodScanner.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return FeignMethodScanner.applicationContext;
    }

    public void setFeignPackage(String feignPackage) {
        this.feignPackage = feignPackage;
    }
}
