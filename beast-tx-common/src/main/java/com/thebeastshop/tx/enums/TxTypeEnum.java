/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/11
 */
package com.thebeastshop.tx.enums;

/**
 * 事务策略类型枚举
 */
public enum TxTypeEnum {
    FINAL_CONSISTENCY("FINAL_CONSISTENCY"),
    TCC("TCC");

    private String value;

    TxTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TxTypeEnum getEnumByValue(String code) {
        for (TxTypeEnum e : TxTypeEnum.values()) {
            if (e.value.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
