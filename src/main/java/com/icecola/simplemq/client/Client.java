package com.icecola.simplemq.client;

import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.api.IConsumer;
import com.icecola.simplemq.api.INetListener;
import com.icecola.simplemq.api.IProvider;
import com.icecola.simplemq.api.provider.SimpleProviderImpl;
import com.icecola.simplemq.bean.OperateEnum;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.client.handle.ClientProtocolHandler;
import com.icecola.simplemq.client.handle.ProtocolDecoder;
import com.icecola.simplemq.queue.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 16:17
 */
public class Client {

    static String addr = "127.0.0.1";

    static Integer port = 6666;

    private Channel channel;

    public Client(IConsumer consumer) {
        Bootstrap client = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup();
        client.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ProtocolDecoder())
                                .addLast(new ClientProtocolHandler(consumer));
                    }
                });
        try {
            ChannelFuture sync = client.connect(addr, port).sync();
            sync.addListener((ChannelFutureListener) future -> {
                if (sync.isSuccess()) {
                    System.out.println("【Netty 客户端连接】：连接成功！！");
                    channel = sync.channel();
                } else {
                    sync.channel().close();
                    if (channel != null) {
                        client.clone();
                    }
                }
            });

            // 关闭
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                worker.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Client(INetListener netListener) {
        Bootstrap client = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup();
        client.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ProtocolDecoder());
                    }
                });
        try {
            ChannelFuture sync = client.connect(addr, port).sync();
            sync.addListener((ChannelFutureListener) future -> {
                if (sync.isSuccess()) {
                    System.out.println("【Netty 客户端连接】：连接成功！！");
                    channel = sync.channel();
                    netListener.successChannel(channel);
                } else {
                    sync.channel().close();
                    if (channel != null) {
                        client.clone();
                    }
                }
            });

            // 关闭
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                worker.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public Channel getChannel() {
        while (this.channel == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.channel;
    }


    public static void main(String[] args) throws InterruptedException {
        Client client = new Client(new SimpleProviderImpl() {
            @Override
            public void successChannel(Channel channel) {
                String s = "";
                Scanner sc = new Scanner(System.in);
                System.out.println("是否继续：Y|N");
                while ((s = sc.nextLine()).equals("Y") && channel.isWritable()) {
                    Protocol<Message> protocol = new Protocol<>();
                    System.out.println("\n操作：cat | enqueue | dequeue");
                    OperateEnum anEnum = OperateEnum.getEnum(sc.nextLine());
                    protocol.setHeader(anEnum.getValue());
                    System.out.println("消息topic：");
                    protocol.setTopic(sc.nextLine());
                    System.out.println("消息内容：");
                    Message message = new Message(sc.nextLine());
                    protocol.setData(message);

                    channel.writeAndFlush(JSONUtil.toJsonStr(protocol));
                    System.out.println("是否继续：Y|N");
                }
                System.out.println("退出");
            }
        });
//        new Thread(() -> {
//            while (client == null || client.getChannel() == null) {
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            Scanner scanner = new Scanner(System.in);
////            while (true) {
////                String msg = scanner.nextLine();
////                boolean sendMsg = sendMsg(msg);
////                if (sendMsg == false) {
////                    break;
////                }
////            }
//
//            String s = null;
//
//
//        }).start();
    }


}
