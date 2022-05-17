package com.example.netty.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xianpeng.xia
 * on 2022/5/17 22:30
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接受request请求 并进行业务处理
        RequestData rd = (RequestData) msg;
        System.err.println("id: " + rd.getId() + ", name: " + rd.getName() + ", requestMessage: " + rd.getRequestMessage());
/*
        byte[] attachment = GzipUtils.ungzip(rd.getAttachment());

        String path = System.getProperty("user.dir")
            + File.separatorChar + "receive" + File.separatorChar + "001.jpg";

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(attachment);
        fos.close();*/

        //	回送相应数据
        ResponseData responseData = new ResponseData();
        responseData.setId("response " + rd.getId());
        responseData.setId("response " + rd.getName());
        responseData.setResponseMessage("响应信息");

        ctx.writeAndFlush(responseData);

    }
}
