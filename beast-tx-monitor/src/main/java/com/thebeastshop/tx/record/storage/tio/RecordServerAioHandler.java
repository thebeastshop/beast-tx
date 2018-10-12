package com.thebeastshop.tx.record.storage.tio;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.tio.RecordPacket;
import com.thebeastshop.tx.tio.TioCoder;
import com.thebeastshop.tx.vo.Record;

public class RecordServerAioHandler implements ServerAioHandler {

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

			RecordPacket resppacket = new RecordPacket();
			resppacket.setBody(JSON.toJSONString(record).getBytes());
			Tio.send(channelContext, resppacket);
		}
		return;
	}
}
