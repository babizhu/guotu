package org.bbz.srxk.guotu.exam;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Created by liulaoye on 17-3-7.
 */
public class SignalHandlerExample{
    public static void main( String[] args ){
        System.out.println( "Signal handling example." );
        SignalHandler handler = new MySignalHandler();
        // kill命令
        Signal termSignal = new Signal( "TERM" );
        Signal.handle( termSignal, handler );
        // ctrl+c命令
        Signal intSignal = new Signal( "INT" );
        Signal.handle( intSignal, handler );
        try {
            Thread.sleep( 50000 );
        } catch( InterruptedException e ) {
            System.out.println( "Interrupted: " + e.getMessage() );
        }
    }
}

@SuppressWarnings("restriction")
class MySignalHandler implements SignalHandler{

    @Override
    public void handle( Signal signal ){
        System.out.println( "Signal handler called for signal " + signal );
        try {
            System.out.println( "Handling " + signal.getName() );
        } catch( Exception e ) {
            System.out.println( "handle|Signal handler" + "failed, reason "
                    + e.getMessage() );
            e.printStackTrace();
        }
    }

}