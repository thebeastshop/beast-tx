package com.thebeastshop.tx.utils;

public class LOGOPrint {
    public static void print(){
        StringBuilder str = new StringBuilder();
        str.append("================================================================================================\n");
        str.append("                 ____  _____    _    ____ _____   _______  __\n");
        str.append("                | __ )| ____|  / \\  / ___|_   _| |_   _\\ \\/ /\n");
        str.append("                |  _ \\|  _|   / _ \\ \\___ \\ | |_____| |  \\  / \n");
        str.append("                | |_) | |___ / ___ \\ ___) || |_____| |  /  \\ \n");
        str.append("                |____/|_____/_/   \\_\\____/ |_|     |_| /_/\\_\\\n");
        str.append("                分布式事务框架，基于TCC事务的事务框架监控跟踪平台\n");
        str.append("================================================================================================\n");

        System.out.println(str.toString());
    }

    public static void main(String[] args) {
        print();
    }
}
