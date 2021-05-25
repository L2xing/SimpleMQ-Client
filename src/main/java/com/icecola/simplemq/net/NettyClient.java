package com.icecola.simplemq.net;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author liuxingxing
 * @date 2021.05.24 22:53
 */
public class NettyClient {


    /**
     * 客户端业务处理handler
     */
    private ClientHandler clientHandler = new ClientHandler();

    /**
     * 事件池
     */
    private EventLoopGroup group = new NioEventLoopGroup();

    /**
     * 启动器
     */
    private Bootstrap bootstrap = new Bootstrap();

    /**
     * 客户端通道
     */
    public static Channel clientChannel;

    /**
     * 客户端连接
     *
     * @param host
     * @param port
     * @throws InterruptedException
     */
    public NettyClient(String host, int port) throws InterruptedException {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast("clientHandler", clientHandler);
                    }
                });

        //发起同步连接操作
        ChannelFuture channelFuture = bootstrap.connect(host, port);

        //注册连接事件
        channelFuture.addListener((ChannelFutureListener) future -> {
            //如果连接成功
            if (future.isSuccess()) {
                clientChannel = channelFuture.channel();
            }
            //如果连接失败，尝试重新连接
            else {
                future.channel().close();
                bootstrap.connect(host, port);
            }
        });

        //注册关闭事件
        channelFuture.channel().closeFuture().addListener(cfl -> {
            close();
        });
    }

    /**
     * 客户端关闭
     */
    private void close() {
        //关闭客户端套接字
        if (clientChannel != null) {
            clientChannel.close();
        }
        //关闭客户端线程组
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 客户端发送消息
     *
     * @param message
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String send(String message) throws InterruptedException, ExecutionException {
        clientChannel.writeAndFlush(message);
        System.out.println("完成");
        return "";
    }

}
