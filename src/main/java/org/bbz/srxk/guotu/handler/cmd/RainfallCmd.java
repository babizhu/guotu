package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liukun on 2017/2/25.
 * 雨量包
 * FF 13 01 03 04 00 00 00 00 FA 33 0A 23 1F 0C 06 01 05 02 AB
 * <p>
 * FF头 13 有19个字节  01 表示功能雨量反馈  03 04 00 00 00 00 FA 33 中最后两个 00 00 表示雨量数据 转换为10进制除以100为雨量数据毫米  0A 23 1F 0C 06 01 05  这个表示时分秒年月日星期 10点35分31秒12年6月1日星期5  02 AB为求和校验FF加到05 683 转换为16进制 02 AB
 * <p>
 * <p>
 * 服务器收到雨量数据反馈协议：
 * FF 55 88 88 88
 */
public class RainfallCmd extends AbstractCmd {

    private static final Logger LOG = LoggerFactory.getLogger(RainfallCmd.class);
    private final ChannelHandlerContext ctx;
    /**
     * 雨量
     */
    private float rainfall;
    /**
     * 时间戳
     */
    private Date timeStamp;

    public RainfallCmd(ChannelHandlerContext ctx, ByteBuf data) {
        super(data);
        this.ctx = ctx;
    }

    @Override
    public void run() {

        ByteBuf response = ctx.alloc().buffer();
        response.writeByte(0xff);
        response.writeByte(0x55);
        response.writeByte(0x88);
        response.writeByte(0x88);
        response.writeByte(0x88);

        ctx.writeAndFlush(response);
    }


    @Override
    void parse() {
//        ByteBufUtil.hexDump(data);
        rainfall = data.getUnsignedShort(4) / 100f;

        byte[] timeStampByes = new byte[7];


        data.getBytes(8, timeStampByes);

        this.timeStamp = parseTimeStamp(timeStampByes);
        LOG.debug(toString());

    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "RainfallCmd{" +
                "rainfall=" + rainfall +
                ", timeStamp=" + formatter.format(timeStamp) +
                '}';
    }

    private Date parseTimeStamp(byte[] timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(timeStamp[3] + 2000, timeStamp[4] - 1, timeStamp[5], timeStamp[0], timeStamp[1], timeStamp[2]);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52);
        return calendar.getTime();
    }
}
