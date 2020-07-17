package cp1_05.server;

import cp1_05.util.MyDecoder;
import cp1_05.util.MyEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
* @Description:    自定义 Channel initlizer
* @Author:         kirago
* @CreateDate:     2020/7/16 10:08 下午
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MyChannelInitailizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 自定义解码器
        channel.pipeline().addLast(new MyDecoder());

        // 自定义编码器
        channel.pipeline().addLast(new MyEncoder());

        // 在管道中添加自定义的接受数据实现方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}
