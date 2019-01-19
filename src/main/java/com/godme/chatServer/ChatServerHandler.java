package com.godme.chatServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
   public static final  ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        String user = channel.remoteAddress().toString();
        channelGroup.forEach(ch->{
            if(ch == channel){
                return;
            }
                ch.writeAndFlush("["+user+"]:" + msg+"\n");
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            ch.writeAndFlush("用户[" + channel.remoteAddress() + "]加入\n");
        });
        channelGroup.add(channel);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("用户["+ctx.channel().remoteAddress()+"]上线");
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("用户["+ctx.channel().remoteAddress()+"]下线");
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
       channelGroup.forEach(ch->{
           ch.writeAndFlush("用户["+ ctx.channel().remoteAddress()+"]离开\n");
       });
    }
}
