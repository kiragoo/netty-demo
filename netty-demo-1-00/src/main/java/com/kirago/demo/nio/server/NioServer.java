package com.kirago.demo.nio.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;

/**
* @Description:    java类作用描述
* @Author:         kirago
* @CreateDate:     2020/7/15 10:59 下午
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class NioServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public static void main(String[] args) throws IOException{
        new NioServer().bind(7397);
    }

    private void bind(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("socker 建立连接并监听端口 port: " + port);
            new NioServerHandler(selector, Charset.forName("GBK")).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
