package org.bbz.srxk.misc;

import com.google.common.base.Strings;

import java.util.Properties;

/**
 * Created by liulaoye on 17-2-17.
 * java.util.Properties的工具类
 */
public class PropertiesUtil{
    public static int getInt( final Properties props, final String key, int defaultValue ){
        final String readThrottleString = props.getProperty( key );
        if( !Strings.isNullOrEmpty( readThrottleString ) ) {
            try {
                return Integer.parseInt( readThrottleString );
            } catch( Exception exception ) {
                exception.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static int getInt(String a){
        if( !Strings.isNullOrEmpty( a ) ) {
            try {
                return Integer.parseInt( a );
            } catch( Exception exception ) {
                exception.printStackTrace();
            }
        }
        return 20;
    }


}
