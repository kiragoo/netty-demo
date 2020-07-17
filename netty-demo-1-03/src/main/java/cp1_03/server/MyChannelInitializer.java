package cp1_03.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel ch) throws Exception {
        /* 解码器*/
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));

        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        /* 自定义 hander */
        ch.pipeline().addLast(new MyServerHandler());
    }
}
