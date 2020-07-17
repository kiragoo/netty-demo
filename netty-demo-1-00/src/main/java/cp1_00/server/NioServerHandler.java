package cp1_00.server;

import cp1_00.ChannelAdapter;
import cp1_00.ChannelHandler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NioServerHandler extends ChannelAdapter {

    public NioServerHandler(Selector selector, Charset charset){
        super(selector, charset);
    }

    @Override
    public void channelActive(ChannelHandler channelHandler) {
        try {
            System.out.println("链接报告LocalAddress:" + channelHandler.channel().getLocalAddress());
            channelHandler.writeAndFlush("hi!  NioServer to msg for you \r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandler channelHandler, Object obj) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + obj);
        channelHandler.writeAndFlush("hi 我已经收到你的消息Success！\r\n");
    }
}
