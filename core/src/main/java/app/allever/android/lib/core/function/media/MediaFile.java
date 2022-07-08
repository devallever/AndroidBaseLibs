package app.allever.android.lib.core.function.media;

import android.graphics.BitmapFactory;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * MediaScanner helper class.
 * <p>
 * {@hide}
 */
public class MediaFile {
    // comma separated list of all file extensions supported by the media scanner
    public static String sFileExtensions;

    // Audio file types
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_OGG = 7;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_OGG;

    // MIDI file types
    public static final int FILE_TYPE_MID = 11;
    public static final int FILE_TYPE_SMF = 12;
    public static final int FILE_TYPE_IMY = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    // Video file types
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_WMV = 25;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WMV;

    // Image file types
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_WBMP = 35;
    public static final int FILE_TYPE_WEBP = 36;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WEBP;

    // Playlist file types
    public static final int FILE_TYPE_M3U = 41;
    public static final int FILE_TYPE_PLS = 42;
    public static final int FILE_TYPE_WPL = 43;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL;


    public static final int FILE_TYPE_ENCRYPT = 50;

    public static final int FILE_TYPE_ENCRYPT_V = 51;

    public static final String MIME_TYPE_VIDEO_MP4 = "video/mp4";
    public static final String MIME_TYPE_VIDEO_3GPP = "video/3gpp";
    public static final String MIME_TYPE_VIDEO_3GPP2 = "video/3gpp2";

    public static class MediaFileType {

        public int fileType;
        public String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static final HashMap<String, MediaFileType> sFileTypeMap
            = new HashMap<String, MediaFileType>();
    private static final HashMap<String, Integer> sMimeTypeMap
            = new HashMap<String, Integer>();

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, fileType);
    }

    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");

        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");

        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");

        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");
        /**
         * 增加对WEBP格式的图片的处理
         */
        addFileType("WEBP", FILE_TYPE_WEBP, "image/webp");

        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");

        addFileType("PHOTOEDITOR", FILE_TYPE_ENCRYPT, "image/photoeditor");

        addFileType("PHOTOEDITORV", FILE_TYPE_ENCRYPT_V, "image/photoeditorv");

        // compute file extensions list for native Media Scanner
        StringBuilder builder = new StringBuilder();

        for (String s : sFileTypeMap.keySet()) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(s);
        }
        sFileExtensions = builder.toString();
    }

    public static final String UNKNOWN_STRING = "<unknown>";

    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }

    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE);
    }

    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }

    /**
     * 是加密的Image类型
     *
     * @param fileType
     * @return
     */
    public static boolean isEncryptImageType(int fileType) {
        return (fileType == FILE_TYPE_ENCRYPT);
    }

    /**
     * 是加密的Video类型
     *
     * @param fileType
     * @return
     */
    public static boolean isEncryptVideoType(int fileType) {
        return (fileType == FILE_TYPE_ENCRYPT_V);
    }


    /**
     * 是加密的Image/Video类型
     *
     * @param fileType
     * @return
     */
    public static boolean isEncryptType(int fileType) {
        return (fileType == FILE_TYPE_ENCRYPT) || (fileType == FILE_TYPE_ENCRYPT_V);
    }

    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }

    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase());
    }

    //重载方法，根据文件路径识别音频文件
    public static boolean isAudioFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isAudioFileType(type.fileType);
        }
        return false;
    }

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value);
    }

    /**
     * 是不是Gif文件
     *
     * @param path
     * @return
     */
    public static boolean isGifFileType(String path) {
        // TODO：暂时屏蔽gif
//    	 MediaFileType type = getFileType(path);
//         if(null != type) {
//             return (type.fileType == FILE_TYPE_GIF);
//         }
        return false;
    }

    public static boolean isGifFileType2(String mimeType) {
        int type = getFileTypeForMimeType(mimeType);
        return (type == FILE_TYPE_GIF);
    }

    /**
     * 用于判断图片是不是jpg
     *
     * @param path
     * @return
     */
    public static boolean isJPGFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return (type.fileType == FILE_TYPE_JPEG);
        }
        return false;
    }

    /**
     * 用于判断图片是不是jpg
     *
     * @param mimeType
     * @return
     */
    public static boolean isJPGFileType2(String mimeType) {
        int type = getFileTypeForMimeType(mimeType);
        return (type == FILE_TYPE_JPEG);
    }

    /**
     * 用于判断图片是不是jpg
     *
     * @param path
     * @return
     */
    public static boolean isPNGFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return (type.fileType == FILE_TYPE_PNG);
        }
        return false;
    }

    /**
     * 用于判断图片是不是jpg
     *
     * @param mimeType
     * @return
     */
    public static boolean isPNGFileType2(String mimeType) {
        int type = getFileTypeForMimeType(mimeType);
        return (type == FILE_TYPE_PNG);
    }

    public static boolean isImageFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options.outWidth != -1;
    }

    public static boolean isVideoFile(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(path);

        return MIME_TYPE_VIDEO_MP4.equalsIgnoreCase(type)
                || MIME_TYPE_VIDEO_3GPP.equalsIgnoreCase(type)
                || MIME_TYPE_VIDEO_3GPP2.equalsIgnoreCase(type);
    }


}