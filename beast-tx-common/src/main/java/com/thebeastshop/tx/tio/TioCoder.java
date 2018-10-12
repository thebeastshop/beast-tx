/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.tio;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

/**
 * t-io编码器
 */
public class TioCoder {

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	public static ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		RecordPacket helloPacket = (RecordPacket) packet;
		byte[] body = helloPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		// bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
		int allLen = RecordPacket.HEADER_LENGHT + bodyLen;
		// 创建一个新的bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		// 设置字节序
		buffer.order(groupContext.getByteOrder());

		// 写入消息头----消息头的内容就是消息体的长度
		buffer.putInt(bodyLen);

		// 写入消息体
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}
	
	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	public static RecordPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
		// 收到的数据组不了业务包，则返回null以告诉框架数据不够
		if (readableLength < RecordPacket.HEADER_LENGHT) {
			return null;
		}

		// 读取消息体的长度
		int bodyLength = buffer.getInt();

		// 数据不正确，则抛出AioDecodeException异常
		if (bodyLength < 0) {
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}

		// 计算本次需要的数据长度
		int neededLength = RecordPacket.HEADER_LENGHT + bodyLength;
		// 收到的数据是否足够组包
		int isDataEnough = readableLength - neededLength;
		// 不够消息体长度(剩下的buffe组不了消息体)
		if (isDataEnough < 0) {
			return null;
		} else // 组包成功
		{
			RecordPacket imPacket = new RecordPacket();
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}
}
