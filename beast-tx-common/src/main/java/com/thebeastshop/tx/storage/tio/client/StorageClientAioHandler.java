/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.storage.tio.client;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.thebeastshop.tx.storage.client.HandlerCallback;
import com.thebeastshop.tx.storage.tio.StoragePacket;
import com.thebeastshop.tx.storage.tio.TioCoder;

/**
 * 
 */
public class StorageClientAioHandler implements ClientAioHandler {
	private static StoragePacket heartbeatPacket = new StoragePacket();

	public Map<Long, HandlerCallback> callbackMap = new ConcurrentHashMap<>();

	@Override
	public StoragePacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
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
		StoragePacket storagePacket = (StoragePacket) packet;
		HandlerCallback callback = callbackMap.get(storagePacket.getCallbackSeq());
		byte[] body = storagePacket.getBody();
		if (ArrayUtils.isNotEmpty(body) && callback != null) {
			callback.doCallback(body);
			callbackMap.remove(storagePacket.getCallbackSeq());
		}
	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 */
	@Override
	public StoragePacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
