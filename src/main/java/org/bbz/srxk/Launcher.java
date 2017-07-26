package org.bbz.srxk;


import org.bbz.srxk.guotu.server.DefaultGuotuServer;
import org.bbz.srxk.guotu.server.IServer;
import org.bbz.srxk.guotu.server.IServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by liulaoye on 17-2-17.
 * Launcher
 * <p>
 * Launches a new service.
 */
public class Launcher{

    private static final Logger LOG = LoggerFactory.getLogger( Launcher.class );

    /**
     * Starts the server from the command line.
     *
     * @param args Any command line arguments.
     */
    public static void main( final String... args ){

        LOG.info( "Beging to running GUOTU server\n" );
//        System.out.println("About to start server on port: " + port);
        IServerBootstrap bootstrap = DefaultGuotuServer.bootstrapFromFile( "./server.properties" );

        final IServer server = bootstrap.start();
        LOG.info( "Server started at address: " + server.getListenAddress() );
        System.out.println( "------------------------------------------------------------" );
        System.out.println( "------------------------------------------------------------" );



    }

//
//    private static void printHelp(final Options options,
//                                  final String errorMessage) {
//        if (!StringUtils.isBlank(errorMessage)) {
//            LOG.error(errorMessage);
//            System.err.println(errorMessage);
//        }
//
//        final HelpFormatter formatter = new HelpFormatter();
//        formatter.printHelp("littleproxy", options);
//    }

//    private static void pollLog4JConfigurationFileIfAvailable() {
//        File log4jConfigurationFile = new File("src/test/resources/log4j.xml");
//        if (log4jConfigurationFile.exists()) {
//            DOMConfigurator.configureAndWatch(
//                    log4jConfigurationFile.getAbsolutePath(), 15);
//        }
//    }
}


