/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/31
 */
package com.thebeastshop.tx.feign.spring;

import com.thebeastshop.tx.context.MethodDefinationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * spring cloud Feign的方法扫描器
 */
public class FeignMethodScanner implements InitializingBean, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(FeignMethodScanner.class);

    private static ApplicationContext applicationContext = null;

    private String feignPackage;

    @Override
    public void afterPropertiesSet() throws Exception {
        String classpath = FeignMethodScanner.class.getResource("/").getPath();
        feignPackage = feignPackage.replace(".", File.separator);
        String searchPath = classpath + feignPackage;
        List<String> classPaths = doPath(new File(searchPath));
        for (String s : classPaths) {
            s = s.replace(classpath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
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

    public String getFeignPackage() {
        return feignPackage;
    }

    public void setFeignPackage(String feignPackage) {
        this.feignPackage = feignPackage;
    }
}
