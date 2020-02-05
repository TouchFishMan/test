package com.telek.hemsipc.http.netty;

import com.telek.hemsipc.http.handler.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class HttpServer {
    private final static Logger log = LoggerFactory.getLogger(HttpServer.class);
    private static int httpPort;

    public static void init() {
        try {
            EventLoopGroup bossgroup = new NioEventLoopGroup();
            EventLoopGroup workgroup = new NioEventLoopGroup();
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossgroup, workgroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(1024 * 1024));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("handler", new HttpRequestHandler());
                        }
                    });
            boot.childOption(ChannelOption.TCP_NODELAY, true);
            boot.childOption(ChannelOption.SO_KEEPALIVE, false);
            boot.bind(new InetSocketAddress(httpPort)).sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

    public static void setHttpPort(int port) {
        HttpServer.httpPort = port;
    }
}