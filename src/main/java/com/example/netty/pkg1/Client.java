package com.example.netty.pkg1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author xianpeng.xia
 * on 2022/5/17 01:05
 */
public class Client {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    // 设置定长字符串接收
                    sc.pipeline().addLast(new FixedLengthFrameDecoder(5));
                    // 设置字符串形式编码
                    sc.pipeline().addLast(new StringDecoder());
                    sc.pipeline().addLast(new ClientHandler());
                }
            });

        ChannelFuture cf = bootstrap.connect("127.0.0.1", 8765).sync();
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("aaaaabbbbb".getBytes()));
        Thread.sleep(2000);
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("cccccccc  ".getBytes()));

        // 等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
