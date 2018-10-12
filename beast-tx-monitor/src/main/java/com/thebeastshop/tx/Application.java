package com.thebeastshop.tx;

import java.util.ServiceLoader;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thebeastshop.tx.record.storage.RecordStorageServer;

@SpringBootApplication
@MapperScan("com.thebeastshop.tx.**.mapper")
public class Application {

	public static void main(String[] args) {
		// -------启动记录存储服务端-------
		ServiceLoader<RecordStorageServer> loader = ServiceLoader.load(RecordStorageServer.class);
		if (loader != null && loader.iterator().hasNext()) {
			loader.iterator().next().start();
		}
		// ----------------------------
		SpringApplication.run(Application.class, args);
	}
}
