package org.bbz.srxk.guotu.db;


import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * User: liukun
 * Date: 13-11-17
 * Time: 下午5:39
 */
public enum DatabaseUtil{
    INSTANCE();

    DatabaseUtil(){
        init();
    }

    private DataSource dataSource;
    private static final String cfgFile = "./dbconfig.properties";

    private void init(){
        try {
            InputStream inputStream = new FileInputStream( cfgFile );
            Properties prop = new Properties();
            prop.load( inputStream );
            dataSource = DruidDataSourceFactory.createDataSource( prop );
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        } catch( Exception e ) {
            e.printStackTrace();
        }

    }

    public Connection getConnection(){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭本次查询所有的打开的资源，除了rs，其余两个资源应该不可能为null，有待考证
     *
     * @param rs  recordset
     * @param st  statement
     * @param con connection
     */
    public void close( ResultSet rs, Statement st, Connection con ){
        try {
            if( rs != null ) {
                rs.close();
            }
            if( st != null ) {
                st.close();
            }
            con.close();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws SQLException{

        long begin = System.nanoTime();
        for( int i = 0; i < 500; i++ ) {

            test2();
        }
        System.out.println( (System.nanoTime() - begin) / 1000000000f );
    }

    @SuppressWarnings("UnusedDeclaration")
    private static void test2() throws SQLException{
//        Connection con = DatabaseUtil.INSTANCE.getConnection();
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT 1");
//        DatabaseUtil.INSTANCE.close(rs, stmt, con);


    }

    @SuppressWarnings("UnusedDeclaration")
    private static void test1(){

//        Connection con = DatabaseUtil.INSTANCE.getConnection();
//
//        PreparedStatement pst = null;
//        ResultSet rs;
//
//        String sql = "select count(*) from city";
//
//        try {
//            pst = con.prepareStatement(sql);
//            rs = pst.executeQuery();
//
//            if (rs.next()) {
//                System.out.println(rs.getLong(1));
//            }
//        } catch (SQLException e) {
//            //logger.debug( e.getLocalizedMessage(), e );
//            System.out.println(e);
//        } finally {
//            DatabaseUtil.INSTANCE.close(null, pst, con);
//        }
    }
}