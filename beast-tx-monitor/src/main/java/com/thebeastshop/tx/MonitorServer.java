/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2019/5/16
 */
package com.thebeastshop.tx;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.socket.config.ServerConfig;
import com.thebeastshop.tx.socket.server.SocketServer;
import com.thebeastshop.tx.socket.server.SocketServerHandler;
import com.thebeastshop.tx.vo.MonitorVo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

@Component
public class MonitorServer implements CommandLineRunner {
    @Override
    public void run(String... strings) {
        // -------启动网络服务端-------
        ServiceLoader<SocketServer> loader = ServiceLoader.load(SocketServer.class);
        if (loader != null && loader.iterator().hasNext()) {
            ServerConfig config = new ServerConfig();
            config.setHandler((SocketServerHandler<Void>) dataBytes -> {
                MonitorVo monitorVo = JSON.parseObject(dataBytes, MonitorVo.class);
                System.out.println("存储数据：" + JSON.toJSONString(monitorVo));
                return null;
            });
            loader.iterator().next().initServer(config).start();
        }
    }
}
