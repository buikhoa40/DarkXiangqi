package cn.yescallop.darkxiangqi.network.socket;

import java.net.SocketAddress;

import cn.yescallop.darkxiangqi.BoardActivity;
import cn.yescallop.darkxiangqi.R;
import cn.yescallop.darkxiangqi.network.Client;
import cn.yescallop.darkxiangqi.network.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TCPClientSocket extends ChannelInboundHandlerAdapter {

    protected Bootstrap bootstrap;
    protected EventLoopGroup group;
    protected Channel channel;

    public TCPClientSocket(int port, String interfaz) {
        try {
            bootstrap = new Bootstrap();
            group = new NioEventLoopGroup();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(this);
            channel = bootstrap.bind(interfaz, port).sync().channel();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public void close() {
        this.group.shutdownGracefully();
        try {
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(SocketAddress dest) {
        this.channel.connect(dest);
    }

    public void writePacket(byte[] buf) {
        this.channel.writeAndFlush(Unpooled.wrappedBuffer(buf));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() == 0) return;
        byte[] buf = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buf);
        Packet pk = Packet.fromBuffer(buf);
        if (pk != null) {
            Client.getInstance().handlePacket(pk);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        BoardActivity.getInstance().showSnackbar(R.string.connected);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        BoardActivity.getInstance().showSnackbar(R.string.disconnected);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        BoardActivity.getInstance().showSnackbar(cause.getLocalizedMessage());
    }
}
