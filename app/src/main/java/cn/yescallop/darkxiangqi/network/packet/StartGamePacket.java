package cn.yescallop.darkxiangqi.network.packet;

import cn.yescallop.darkxiangqi.network.util.Binary;

public class StartGamePacket extends Packet {

    public boolean first;
    public byte[][] data;

    @Override
    public byte pid() {
        return START_GAME;
    }

    @Override
    public void decode() {
        this.first = this.getBoolean();
        this.data = Binary.splitBytes(this.get(), 2);
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(first);
        this.put(Binary.appendBytes(data));
    }
}
