package com.thebeastshop.tx;

import java.util.ServiceLoader;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.storage.server.StorageServer;
import com.thebeastshop.tx.storage.server.StorageServerHandler;
import com.thebeastshop.tx.vo.Record;

@SpringBootApplication
@MapperScan("com.thebeastshop.tx.**.mapper")
public class Application {

	public static void main(String[] args) {
		// -------启动记录存储服务端-------
		ServiceLoader<StorageServer> loader = ServiceLoader.load(StorageServer.class);
		if (loader != null && loader.iterator().hasNext()) {
			loader.iterator().next().start(new StorageServerHandler() {
				@Override
				public Object receive(byte[] dataBytes) {
					Record record = JSON.parseObject(dataBytes, Record.class);
					System.out.println("存储数据：" + JSON.toJSONString(record));
					Record reply = new Record();
					reply.setTxId(111);
					return reply;
				}
			});
		}
		// ----------------------------
		SpringApplication.run(Application.class, args);
	}
}
