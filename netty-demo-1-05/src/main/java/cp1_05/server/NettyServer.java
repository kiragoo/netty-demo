package cp1_05.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Logger;

/**
* @Description:    netty server
* @Author:         kirago
* @CreateDate:     2020/7/16 10:21 下午
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class NettyServer {

    private final Logger logger = Logger.getLogger(NettyServer.class.getName());

    public static void main(String[] args){
        new NettyServer().bind(7379);
    }

    /**
     *
     * @param port
     */
    private void bind(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new MyChannelInitailizer());
            ChannelFuture f = serverBootstrap.bind(port);
            logger.info("Netty server done on the port: " + port);
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
