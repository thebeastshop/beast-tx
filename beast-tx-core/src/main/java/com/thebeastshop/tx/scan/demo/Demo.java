package com.thebeastshop.tx.scan.demo;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.enums.TxTypeEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//@Transactional(propagation = Propagation.NEVER)
//@BeastTx(TxTypeEnum.FINAL_CONSISTENCY)
public class Demo implements IDemo{
//    @Transactional
//    @BeastTx
    public void test1(){
        System.out.println("test1");
    }

    public void test1(String name,String address){
        System.out.println("test1 overload");
    }

    @Transactional
    @BeastTx(TxTypeEnum.TCC)
    public void test2(){
        System.out.println("test2");
    }
}
