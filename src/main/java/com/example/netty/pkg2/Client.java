package com.example.netty.pkg2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
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
                    // 设置特殊分隔符
                    ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                    sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                    // 字符串编码
                    sc.pipeline().addLast(new StringDecoder());
                    sc.pipeline().addLast(new ClientHandler());
                }
            });

        ChannelFuture cf = bootstrap.connect("127.0.0.1", 8765).sync();

        for (int i = 1; i <= 100; i++) {
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer(("消息" + i + "$_").getBytes()));
        }
        // 等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
