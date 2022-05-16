使用消息定长处理拆包/粘包问题

## 方式

```
例如每个报文的大小固定为5个字节，如果不够，空位补空格
```

## 关键

- 客户端服务器增加ChannelHandler

```
 // 设置定长字符串接收
 sc.pipeline().addLast(new FixedLengthFrameDecoder(5));
```

- 发送定长
```
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("aaaaabbbbb".getBytes()));
        Thread.sleep(2000);
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("cccccccc  ".getBytes()));

```