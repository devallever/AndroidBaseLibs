package app.allever.android.lib.core.util;

import java.nio.ByteBuffer;

public class BytesUtils {


    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        return b & 0xFF;
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |

                (b[2] & 0xFF) << 8 |

                (b[1] & 0xFF) << 16 |

                (b[0] & 0xFF) << 24;

    }


    public static byte[] intToByteArray(int a) {

        return new byte[]{

                (byte) ((a >> 24) & 0xFF),

                (byte) ((a >> 16) & 0xFF),

                (byte) ((a >> 8) & 0xFF),

                (byte) (a & 0xFF)

        };

    }


    //byte 数组与 long 的相互转换

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);

        return buffer.array();

    }


    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.put(bytes, 0, bytes.length);

        buffer.flip();//need flip

        return buffer.getLong();

    }
}
