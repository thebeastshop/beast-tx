/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.record.storage.tio;

import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.tio.RecordPacket;
import com.thebeastshop.tx.tio.TioCoder;
import com.thebeastshop.tx.vo.Record;

/**
 * 
 */
public class RecordClientAioHandler implements ClientAioHandler {
	private static RecordPacket heartbeatPacket = new RecordPacket();

	@Override
	public RecordPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
		return TioCoder.decode(buffer, limit, position, readableLength, channelContext);
	}

	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		return TioCoder.encode(packet, groupContext, channelContext);
	}

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		RecordPacket helloPacket = (RecordPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			Record record = JSON.parseObject(body, Record.class);
			System.out.println("收到消息：" + JSON.toJSONString(record));
		}

		return;
	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 */
	@Override
	public RecordPacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
