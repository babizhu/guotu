package org.bbz.srxk.guotu.handler.cmd;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulaoye on 17-2-28.
 * CMD ENUM
 */
public enum Cmd{
    LOGIN_CMD( 2 ),
    RAINFALL( 1 ), UNDEFINED( 0 );

    private final int number;

    private static final Map<Integer, Cmd> numToEnum = new HashMap<Integer, Cmd>();

    static{
        for( Cmd t : values() ) {

            Cmd s = numToEnum.put( t.number, t );
            if( s != null ) {
                throw new RuntimeException( t.number + "重复了" );
            }
        }
    }

    Cmd( int number ){
        this.number = number;
    }

    public int toNum(){
        return number;
    }

    public static Cmd fromNum( int n ){
        final Cmd cmd = numToEnum.get( n );
        if( cmd == null){
            return Cmd.UNDEFINED;
        }
        return cmd;
    }


}
