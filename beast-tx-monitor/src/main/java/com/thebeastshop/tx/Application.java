package com.thebeastshop.tx;

import java.util.ServiceLoader;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.network.HasBytes;
import com.thebeastshop.tx.network.config.ServerConfig;
import com.thebeastshop.tx.network.server.NetworkServer;
import com.thebeastshop.tx.network.server.NetworkServerHandler;
import com.thebeastshop.tx.vo.Record;

@SpringBootApplication
@MapperScan("com.thebeastshop.tx.**.mapper")
public class Application {

	public static void main(String[] args) {
		// -------启动网络服务端-------
		ServiceLoader<NetworkServer> loader = ServiceLoader.load(NetworkServer.class);
		if (loader != null && loader.iterator().hasNext()) {
			ServerConfig config = new ServerConfig();
			config.setHandler(new NetworkServerHandler() {
				@Override
				public HasBytes receive(byte[] dataBytes) {
					Record record = JSON.parseObject(dataBytes, Record.class);
					System.out.println("存储数据：" + JSON.toJSONString(record));
					Record reply = new Record();
					reply.setTxId(111);
					return reply;
				}
			});
			loader.iterator().next().initServer(config).start();
		}
		// ----------------------------
		SpringApplication.run(Application.class, args);
	}
}
