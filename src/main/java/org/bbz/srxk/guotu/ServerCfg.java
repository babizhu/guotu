package org.bbz.srxk.guotu;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerCfg{



    /**
     * 上传web地址
     */
    public static final String          WEB_HOST;

    static {
        Properties prop = new Properties();
        try {
            InputStream in = new BufferedInputStream( new FileInputStream( "resources/server.properties" ) );
            prop.load( in );

        } catch( IOException e ) {
            e.printStackTrace();
        }

        WEB_HOST = prop.getProperty( "webHost" ).trim();

    }

}