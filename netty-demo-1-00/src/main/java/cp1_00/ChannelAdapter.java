package cp1_00;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
* @Description:    java类作用描述
* @Author:         kirago
* @CreateDate:     2020/7/13 10:49 PM
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public abstract class ChannelAdapter extends Thread{

    private Selector selector;

    private ChannelHandler channelHandler;

    private Charset charset;

    public ChannelAdapter(Selector selector, Charset charset){
        this.selector = selector;
        this.charset = charset;
    }

    @Override
    public void run(){
        while(true){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                SelectionKey sk = null;
                while (iterator.hasNext()){
                    sk = iterator.next();
                    iterator.remove();
                    handleInput(sk);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException{
        if(selectionKey.isValid()) {
            Class<?> superClass = selectionKey.channel().getClass().getSuperclass();
            //客户端
            if(superClass == SocketChannel.class){
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                if(selectionKey.isConnectable()){
                    if(socketChannel.finishConnect()){
                        channelHandler = new ChannelHandler(socketChannel, charset);
                        channelActive(channelHandler);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }else {
                        System.exit(1);
                    }
                }
            }

            // 服务端
            if(superClass == ServerSocketChannel.class){
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    channelHandler = new ChannelHandler(socketChannel, charset);
                    channelActive(channelHandler);
                }
            }

            if(selectionKey.isReadable()){
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    channelRead(channelHandler, new String(bytes, charset));
                }else if(readBytes < 0 ){
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    public abstract void channelActive(ChannelHandler channelHandler);

    public abstract void channelRead(ChannelHandler channelHandler, Object obj);

}
