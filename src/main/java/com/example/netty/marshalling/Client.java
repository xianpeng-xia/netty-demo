package com.example.netty.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author xianpeng.xia
 * on 2022/5/17 22:38
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup wGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(wGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodecFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ClientHandler());
                }
            });

        ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
        Channel channel = cf.channel();

        for (int i = 0; i < 10; i++) {
            RequestData rd = new RequestData();
            rd.setId("" + i);
            rd.setName("我是消息" + i);
            rd.setRequestMessage("内容" + i);
            /*String path = System.getProperty("user.dir")
                + File.separatorChar + "source" + File.separatorChar + "001.jpg";
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            rd.setAttachment(GzipUtils.gzip(data));*/
            channel.writeAndFlush(rd);
        }

        cf.channel().closeFuture().sync();
        wGroup.shutdownGracefully();

    }

}
