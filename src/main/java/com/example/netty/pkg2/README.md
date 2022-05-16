使用包尾部增加特殊字符处理拆包/粘包问题

### 方式

```

在包尾部增加特殊字符进行分割，例如加回车等

```

### 关键

- 客户端服务器增加ChannelHandler
```
// 设置特殊分隔符
ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
```

- 客户端request增加分隔符

```

        for (int i = 1; i <= 100; i++) {
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer(("消息" + i + "$_").getBytes()));
        }
```
- 服务器response增加分隔符
```

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String request = (String) msg;
        System.out.println("Server :" + msg);
        String response = "server response :" + request + "$_";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
    }

```

- client释放buf
```
 @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            String response = (String) msg;
            System.out.println("Client :" + response);
        } finally {
            // 释放buf
            ReferenceCountUtil.release(msg);
        }
    }

```
