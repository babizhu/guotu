package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Data;

import java.util.Arrays;

@Data
/**
 * Created by liulaoye on 17-2-22.
 * container
 */
public class MessageContainer{
    private final byte head;
    private final short len;
    private final short cmdId;

    private final ByteBuf data;

    private final byte[] checkSum;

    public MessageContainer( byte head, short len, short cmdId, ByteBuf data, byte[] checkSum ){
        this.head = head;
        this.len = len;
        this.cmdId = cmdId;
        this.data = data;
        this.checkSum = checkSum;
    }

    @Override
    public String toString(){
        return "MessageContainer{" +
                "head=" + head +
                ", len=" + len +
                ", cmdId=" + cmdId +
                ", data=" + ByteBufUtil.hexDump( data ) +
                ", checkSum=" + Arrays.toString( checkSum ) +
                '}';
    }
}