package com.thebeastshop.tx.scan;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.enums.TxTypeEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//@Transactional(propagation = Propagation.NEVER)
//@BeastTx(TxTypeEnum.FINAL_CONSISTENCY)
public class Demo {
    @Transactional
    @BeastTx
    public void test1(){

    }

    @Transactional
    @BeastTx(TxTypeEnum.FINAL_CONSISTENCY)
    public void test2(){

    }
}
