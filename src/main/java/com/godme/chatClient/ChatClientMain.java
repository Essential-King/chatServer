package com.godme.chatClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class ChatClientMain {
    public static void main(String[] args) throws IOException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            Channel channel = bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChatClientInitialize()).connect(new InetSocketAddress("localhost",8989)).channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                channel.writeAndFlush(reader.readLine()+ "\n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
