package cn.cola.zentalk.server.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Netty WebSocket 服务器
 *
 * @author ColaBlack
 */
@Slf4j
@Configuration
public class NettyWebSocketServer {
    /**
     * WebSocket 端口
     */
    public static final int WEB_SOCKET_PORT = 1223;

    /**
     * 主事件循环组，负责监听客户端连接
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    /**
     * 从事件循环组，负责处理客户端的读写请求，执行Handler
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    /**
     * 启动
     */
    @PostConstruct
    public void start() throws InterruptedException {
        run();
    }

    /**
     * 销毁主从事件循环组
     */
    @PreDestroy
    public void destroy() {
        Future<?> future = bossGroup.shutdownGracefully();
        Future<?> future1 = workerGroup.shutdownGracefully();
        future.syncUninterruptibly();
        future1.syncUninterruptibly();
        log.info("关闭WebSocket 服务器成功");
    }

    public void run() throws InterruptedException {
        log.info("启动WebSocket 服务器中，即将监听端口：{}", WEB_SOCKET_PORT);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 配置服务器
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 最多保持128个等待连接请求，超过这个数量时，新连接将被拒绝
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 添加日志处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 心跳检测，如果30秒内客户端没有读写，则认为其已经下线，关闭连接
                        pipeline.addLast(new IdleStateHandler(30, 0, 0));
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 聚合http消息，解决浏览器发送大量数据时，会发出多次http请求的问题
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 升级http协议为websocket协议，增加处理WebSocket消息的处理器
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        // 自定义handler ，处理业务逻辑
                        pipeline.addLast(new NettyWebSocketServerHandler());
                    }
                });

        // 启动服务器
        log.info("WebSocket 服务器启动成功，监听端口：{}", WEB_SOCKET_PORT);
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
    }

}
