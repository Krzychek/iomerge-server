package pl.kbieron.iomerge.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

// TODO finish!!


@Component
public class ServerManager {

	@ConfigProperty
	private int port = 5555;

	public void startServer() {
		try {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

			// Configure the server.
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap b = new ServerBootstrap() //
						.group(bossGroup, workerGroup)//
						.channel(NioServerSocketChannel.class)//
						.handler(new ChannelInitializer<SocketChannel>() { // todo outbound
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline p = ch.pipeline();
								p.addLast(sslCtx.newHandler(ch.alloc()));
								p.addLast(new InboudHandler());
							}
						}) // todo inbound
						.childHandler(new ChannelInitializer<SocketChannel>() { // todo outbound
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline p = ch.pipeline();
								p.addLast(sslCtx.newHandler(ch.alloc()));
								p.addLast(new OutboudHandler());
							}
						});

				// Start the server.
				ChannelFuture f = b.bind(port).sync();

				// Wait until the server socket is closed.
				f.channel().closeFuture().sync();
			} finally {
				// Shut down all event loops to terminate all threads.
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		} catch (SSLException | CertificateException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
