package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Data;
import org.bbz.srxk.guotu.handler.cmd.GongdiCmd;

@Data
/**
 * Created by liulaoye on 17-2-22.
 * container
 */
public class GongdiMessageContainer{
    private final byte head;
    private final short len;
    private final short funcId;

    private final ByteBuf data;


    public GongdiMessageContainer( byte head, short len, short cmdId, ByteBuf data){
        this.head = head;
        this.len = len;
        this.funcId = cmdId;
        this.data = data;
    }

    @Override
    public String toString(){
        return "MessageContainer{" +
                "head=" + head +
                ", len=" + len +
                ", funcId=" + GongdiCmd.fromNum( funcId ) + "(" + funcId + ")" +
                ", data=" + ByteBufUtil.hexDump( data ) +
                '}';
    }
}