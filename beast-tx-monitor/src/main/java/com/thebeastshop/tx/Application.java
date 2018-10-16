package com.thebeastshop.tx;

import java.util.ServiceLoader;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.storage.HandlerCallback;
import com.thebeastshop.tx.storage.server.StorageServer;
import com.thebeastshop.tx.vo.Record;

@SpringBootApplication
@MapperScan("com.thebeastshop.tx.**.mapper")
public class Application {

	public static void main(String[] args) {
		// -------启动记录存储服务端-------
		ServiceLoader<StorageServer> loader = ServiceLoader.load(StorageServer.class);
		if (loader != null && loader.iterator().hasNext()) {
			loader.iterator().next().start(new HandlerCallback() {
				@Override
				public void doCallback(byte[] dataBytes) {
					Record record = JSON.parseObject(dataBytes, Record.class);
					System.out.println("存储数据：" + JSON.toJSONString(record));
				}
			});
		}
		// ----------------------------
		SpringApplication.run(Application.class, args);
	}
}
