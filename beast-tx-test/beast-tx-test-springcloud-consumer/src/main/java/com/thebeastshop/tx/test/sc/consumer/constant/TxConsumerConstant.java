package com.thebeastshop.tx.test.sc.consumer.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/config.properties")
public class TxConsumerConstant {

    @Value("${http.connectTimeOutMillis}")
    private int connectTimeOutMillis;

    @Value("${http.readTimeOutMillis}")
    private int readTimeOutMillis;

    @Value("${tx.feign.scan}")
    private String txfeignScanPackage;

    public int getConnectTimeOutMillis() {
        return connectTimeOutMillis;
    }

    public void setConnectTimeOutMillis(int connectTimeOutMillis) {
        this.connectTimeOutMillis = connectTimeOutMillis;
    }

    public int getReadTimeOutMillis() {
        return readTimeOutMillis;
    }

    public void setReadTimeOutMillis(int readTimeOutMillis) {
        this.readTimeOutMillis = readTimeOutMillis;
    }

    public String getTxfeignScanPackage() {
        return txfeignScanPackage;
    }

    public void setTxfeignScanPackage(String txfeignScanPackage) {
        this.txfeignScanPackage = txfeignScanPackage;
    }
}
