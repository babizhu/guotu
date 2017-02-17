package org.bbz.srxk;


import org.bbz.srxk.server.DefaultServer;
import org.bbz.srxk.server.IServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * Created by liulaoye on 17-2-17.
 * Launcher
 *
 * Launches a new service.
 */
public class Launcher {

    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

       /**
     * Starts the server from the command line.
     *
     * @param args
     *            Any command line arguments.
     */
    public static void main(final String... args) {

        LOG.info("Beging to running GUOTU server");
//        System.out.println("About to start server on port: " + port);
        IServerBootstrap bootstrap = DefaultServer.bootstrapFromFile("./server.properties");

        System.out.println("About to start...");
        bootstrap.start();
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


