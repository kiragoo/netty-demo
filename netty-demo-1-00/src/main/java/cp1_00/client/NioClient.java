package cp1_00.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NioClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        boolean isConnet = socketChannel.connect(new InetSocketAddress("127.0.0.1", 7397));
        if(isConnet){
            socketChannel.register(selector, SelectionKey.OP_READ);
        }else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
        System.out.println("client start done");
        new NioClientHandler(selector, Charset.forName("GBK")).start();
    }
}
