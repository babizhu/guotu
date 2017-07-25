package org.bbz.srxk.guotu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


/**
 * Created by liulaoye on 17-3-2.
 *
 */
public enum RainFallDataProvider{

    INSTANCE;

    public void add( float rainfall, int clientId, long timeStamps ){
        PreparedStatement pst = null;
        String sql = "insert into Rainfall ( Timestamps,ClientId,RainfallAmount,CreateTime ) values ( ?,?,?,? )";
        Connection con = DatabaseUtil.INSTANCE.getConnection();

        try {
            pst = con.prepareStatement( sql );
            pst.setLong( 1, timeStamps );
            pst.setInt( 2, clientId );
            pst.setFloat( 3, rainfall );
            pst.setLong( 4, new Date().getTime() );

            pst.executeUpdate();



        } catch( SQLException e ) {
            e.printStackTrace();
        } finally {

            DatabaseUtil.INSTANCE.close( null, pst, con );
        }
    }

}
