package com.icecola.simplemq.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.bean.OperateEnum;
import com.icecola.simplemq.bean.Protocol;
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

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 16:17
 */
public class Client {

    static String addr = "127.0.0.1";

    static Integer port = 6666;

    static Channel channel;

    public Client() {
        Bootstrap client = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup();
        client.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder());
                    }
                });
        try {
            ChannelFuture sync = client.connect(addr, port).sync();
            System.out.println("客户端启动成功");

            sync.addListener((ChannelFutureListener) future -> {
                //如果连接成功
                if (future.isSuccess()) {
                    channel = sync.channel();
                    System.out.println("获取到channel");
                } else {
                    channel.close();
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

    public static boolean sendMsg(String str) {
        if (channel != null
                && channel.isWritable()) {
            channel.writeAndFlush(str);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while (channel == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                String msg = scanner.nextLine();
//                boolean sendMsg = sendMsg(msg);
//                if (sendMsg == false) {
//                    break;
//                }
//            }
            Protocol<Message> protocol = new Protocol<>();
            Message message = new Message("123123");
            protocol.setHeader(OperateEnum.CAT.getValue());
            protocol.setTopic("topic");
            protocol.setData(message);
            channel.writeAndFlush(JSONUtil.toJsonStr(protocol));
            System.out.println("退出");
        }).start();
        new Thread(() -> {
            Client client = new Client();
        }).start();


    }


}
