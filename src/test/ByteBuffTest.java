import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * Created by liulaoye on 17-2-24.
 */
public class ByteBuffTest{

    @Test
    public void refCn(){
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.retain().retain();
        System.out.println( buffer.refCnt());
        final ByteBuf copy = buffer.duplicate();
        buffer.release();
        copy.writeByte( 2 );
        System.out.println(copy.refCnt());
    }
}
