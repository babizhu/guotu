package org.bbz.srxk.guotu.handler.cmd;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulaoye on 17-2-28.
 * CMD ENUM
 */
public enum GongdiCmd{
    ENTER(0xb5),//进入某区域
    EXIT(0xb4), //离开某区域
    ALERT(0xb6), //报警
    KAOQING(0xb3),//考勤
    UNDEFINED_CMD(0xff);//未知命令

    private final int number;

    private static final Map<Integer, GongdiCmd> numToEnum = new HashMap<Integer, GongdiCmd>();

    static{
        for( GongdiCmd t : values() ) {

            GongdiCmd s = numToEnum.put( t.number, t );
            if( s != null ) {
                throw new RuntimeException( t.number + "重复了" );
            }
        }
    }

    GongdiCmd( int number ){
        this.number = number;
    }

    public int toNum(){
        return number;
    }

    public static GongdiCmd fromNum( int n ){
        final GongdiCmd cmd = numToEnum.get( n );
        if( cmd == null){
            return GongdiCmd.UNDEFINED_CMD;
        }
        return cmd;
    }


}
