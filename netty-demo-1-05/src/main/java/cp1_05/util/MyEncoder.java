package cp1_05.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
* @Description:    自定义解码
* @Author:         kirago
* @CreateDate:     2020/7/16 10:03 下午
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MyEncoder extends MessageToByteEncoder {

    /**
     *
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        String msg = o.toString();
        byte[] bytes = msg.getBytes();
        byte[] send = new byte[bytes.length+2];
        byteBuf.writeInt(send.length);
        byteBuf.writeBytes(send);
    }
}
