package cp1_05.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
* @Description:    自定义编码
* @Author:         kirago
* @CreateDate:     2020/7/16 10:02 下午
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MyDecoder extends ByteToMessageDecoder {

    /**
     * 数据基础包长度
     */
    private final int BASE_LEN = 4;

    /**
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() < BASE_LEN) return;

        // 记录包头位置
        int beginIdx;

        while (true) {
            // 获取包头开始的index
            beginIdx = byteBuf.readerIndex();
            // 标记包头开始的index
            byteBuf.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            if (byteBuf.readByte() == 0x02) {
                break;
            }
            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (byteBuf.readableBytes() < BASE_LEN) {
                return;
            }

        }

        //剩余长度不足可读取数量[没有内容长度位]
        int readableCount = byteBuf.readableBytes();
        if (readableCount <= 1) {
            byteBuf.readerIndex(beginIdx);
            return;
        }

        //长度域占4字节，读取int
        ByteBuf bb = byteBuf.readBytes(1);
        String msgLengthStr = bb.toString(Charset.forName("GBK"));
        int msgLength = Integer.parseInt(msgLengthStr);

        //剩余长度不足可读取数量[没有结尾标识]
        readableCount = byteBuf.readableBytes();
        if (readableCount < msgLength + 1) {
            byteBuf.readerIndex(beginIdx);
            return;
        }

        ByteBuf msgContent = byteBuf.readBytes(msgLength);

        //如果没有结尾标识，还原指针位置[其他标识结尾]
        byte end = byteBuf.readByte();
        if (end != 0x03) {
            byteBuf.readerIndex(beginIdx);
            return;
        }

        list.add(msgContent.toString(Charset.forName("GBK")));
    }
}
