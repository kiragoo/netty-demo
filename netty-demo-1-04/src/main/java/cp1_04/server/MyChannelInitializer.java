package cp1_04.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /* 基于换行符号*/
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));

        /* 解码转String */
        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        ch.pipeline().addLast(new MyServerHandler());
    }
}
