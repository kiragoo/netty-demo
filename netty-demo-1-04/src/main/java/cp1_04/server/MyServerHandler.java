package cp1_04.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当客户端主动连接服务器的连接后，这个通道就是活跃的。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ChannelHander.channels.add(ctx.channel());

        SocketChannel channel = (SocketChannel) ctx.channel();

        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");

        /* 通知客户端连接建立成功*/
        String str = "通知客户端连接成功" + " " + new Date() + " " + channel.localAddress().getHostString();
        ctx.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的连接时，这个通道就是不活跃的。
      * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("客户端断开" + ctx.channel().localAddress().toString());

        /* 当有客户端断开时 就将此客户端从 channelGroup 中移除*/
        ChannelHander.channels.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        /* 此处已经做了编解码处理了 不需要自己进行编解码了*/
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date() + " 接收到消息" + msg));

        /* 收到消息后 群发给客户端 */
        String str = "服务端收到: " + new Date() + " " + msg + "\r\n";
        ChannelHander.channels.writeAndFlush(str);
    }

    /* 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.close();
        System.out.println("异常信息: \r\n" + cause.getMessage());
    }

}
