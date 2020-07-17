package cp1_05.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.util.logging.Logger;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = Logger.getLogger(MyServerHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        SocketChannel channel = (SocketChannel) ctx.channel();

        logger.info("链接开始......");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        logger.info(msg.toString());
        ctx.writeAndFlush(msg);
    }


}
