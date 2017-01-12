package cn.yescallop.darkxiangqi.network;

import java.net.InetSocketAddress;

import cn.yescallop.darkxiangqi.Board;
import cn.yescallop.darkxiangqi.BoardActivity;
import cn.yescallop.darkxiangqi.network.packet.MovePiecePacket;
import cn.yescallop.darkxiangqi.network.packet.Packet;
import cn.yescallop.darkxiangqi.network.packet.SelectPiecePacket;
import cn.yescallop.darkxiangqi.network.packet.StartGamePacket;
import cn.yescallop.darkxiangqi.network.packet.TurnPiecePacket;
import cn.yescallop.darkxiangqi.network.socket.TCPClientSocket;

public class Client {

    private static Client instance;
    private TCPClientSocket socket;

    public Client() {
        this(0);
    }

    public Client(int port) {
        this(port, "0.0.0.0");
    }

    public Client(int port, String interfaz) {
        instance = this;
        this.socket = new TCPClientSocket(port, interfaz);
    }

    public static Client getInstance() {
        return instance;
    }

    public void connect(final String dest, final int port) {
        new Thread() {
            @Override
            public void run() {
                connect(new InetSocketAddress(dest, port));
            }
        }.start();
    }

    public void connect(InetSocketAddress address) {
        this.socket.connect(address);
    }

    public void sendPacket(final Packet packet) {
        packet.encode();
        new Thread() {
            @Override
            public void run() {
                socket.writePacket(packet.getBuffer());
            }
        }.start();
    }

    public void handlePacket(Packet packet) {
        switch (packet.pid()) {
            case Packet.START_GAME:
                StartGamePacket startGamePacket = (StartGamePacket) packet;
                Board.getInstance().startGame(startGamePacket.first, startGamePacket.data);
                break;
            case Packet.MOVE_PIECE:
                MovePiecePacket movePiecePacket = (MovePiecePacket) packet;
                Board.getInstance().getPiece(movePiecePacket.fromX, movePiecePacket.fromY).move(movePiecePacket.toX, movePiecePacket.toY, false);
                break;
            case Packet.TURN_PIECE:
                TurnPiecePacket turnPiecePacket = (TurnPiecePacket) packet;
                Board.getInstance().getPiece(turnPiecePacket.x, turnPiecePacket.y).turn(false);
                break;
            case Packet.SELECT_PIECE:
                SelectPiecePacket selectPiecePacket = (SelectPiecePacket) packet;
                Board.getInstance().getPiece(selectPiecePacket.x, selectPiecePacket.y).select(false);
                break;
        }
        BoardActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Board.getInstance().updateImage();
            }
        });
    }

    public void close() {
        this.socket.close();
    }
}
