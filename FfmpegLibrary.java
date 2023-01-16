package com.google.android.exoplayer2.ext.ffmpeg;

import androidx.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;

public final class FfmpegLibrary {
    private static String version;
    private static int inputBufferPaddingSize;

    public static native void ffmpegLogger(boolean z);

    private static native String ffmpegGetVersion();

    private static native int ffmpegGetInputBufferPaddingSize();

    private static native boolean ffmpegHasDecoder(String str);

    static {
        loadLibrary();
        registerModule();
        inputBufferPaddingSize = -1;
    }

    static void loadLibrary() {
        System.loadLibrary("exoplayer-ffmpeg");
        System.loadLibrary("exoplayer-ffmpeg-jni");
    }

    static void registerModule() {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ffmpeg");
    }

    private FfmpegLibrary() {
    }

    public static boolean isAvailable() {
        return true;
    }

    @Nullable
    public static String getVersion() {
        if (!isAvailable()) {
            return null;
        }
        if (version == null) {
            version = ffmpegGetVersion();
        }
        return version;
    }

    public static int getInputBufferPaddingSize() {
        if (!isAvailable()) {
            return -1;
        }
        if (inputBufferPaddingSize == -1) {
            inputBufferPaddingSize = ffmpegGetInputBufferPaddingSize();
        }
        return inputBufferPaddingSize;
    }

    public static boolean supportsFormat(Format format) {
        String codecName;
        if (!isAvailable() || (codecName = getCodecName(format)) == null) {
            return false;
        }
        if (!ffmpegHasDecoder(codecName)) {
            return false;
        }
        return true;
    }

    @Nullable
    public static String getCodecName(Format format) {
        String sampleMimeType = format.sampleMimeType;
        boolean z = true;
        switch (sampleMimeType.hashCode()) {
            case -2123537834:
                if (sampleMimeType.equals(MimeTypes.AUDIO_E_AC3_JOC)) {
                    z = true;
                    break;
                }
                break;
            case -1662541442:
                if (sampleMimeType.equals(MimeTypes.VIDEO_H265)) {
                    z = true;
                    break;
                }
                break;
            case -1606874997:
                if (sampleMimeType.equals(MimeTypes.AUDIO_AMR_WB)) {
                    z = true;
                    break;
                }
                break;
            case -1095064472:
                if (sampleMimeType.equals(MimeTypes.AUDIO_DTS)) {
                    z = true;
                    break;
                }
                break;
            case -1003765268:
                if (sampleMimeType.equals(MimeTypes.AUDIO_VORBIS)) {
                    z = true;
                    break;
                }
                break;
            case -432837260:
                if (sampleMimeType.equals(MimeTypes.AUDIO_MPEG_L1)) {
                    z = true;
                    break;
                }
                break;
            case -432837259:
                if (sampleMimeType.equals(MimeTypes.AUDIO_MPEG_L2)) {
                    z = true;
                    break;
                }
                break;
            case -53558318:
                if (sampleMimeType.equals(MimeTypes.AUDIO_AAC)) {
                    z = false;
                    break;
                }
                break;
            case 5751993:
                if (sampleMimeType.equals(MimeTypes.VIDEO_MPEG2)) {
                    z = true;
                    break;
                }
                break;
            case 187078296:
                if (sampleMimeType.equals(MimeTypes.AUDIO_AC3)) {
                    z = true;
                    break;
                }
                break;
            case 1331836730:
                if (sampleMimeType.equals(MimeTypes.VIDEO_H264)) {
                    z = true;
                    break;
                }
                break;
            case 1503095341:
                if (sampleMimeType.equals(MimeTypes.AUDIO_AMR_NB)) {
                    z = true;
                    break;
                }
                break;
            case 1504470054:
                if (sampleMimeType.equals(MimeTypes.AUDIO_ALAC)) {
                    z = true;
                    break;
                }
                break;
            case 1504578661:
                if (sampleMimeType.equals(MimeTypes.AUDIO_E_AC3)) {
                    z = true;
                    break;
                }
                break;
            case 1504619009:
                if (sampleMimeType.equals(MimeTypes.AUDIO_FLAC)) {
                    z = true;
                    break;
                }
                break;
            case 1504831518:
                if (sampleMimeType.equals(MimeTypes.AUDIO_MPEG)) {
                    z = true;
                    break;
                }
                break;
            case 1504891608:
                if (sampleMimeType.equals(MimeTypes.AUDIO_OPUS)) {
                    z = true;
                    break;
                }
                break;
            case 1505942594:
                if (sampleMimeType.equals(MimeTypes.AUDIO_DTS_HD)) {
                    z = true;
                    break;
                }
                break;
            case 1556697186:
                if (sampleMimeType.equals(MimeTypes.AUDIO_TRUEHD)) {
                    z = true;
                    break;
                }
                break;
            case 1903231877:
                if (sampleMimeType.equals(MimeTypes.AUDIO_ALAW)) {
                    z = true;
                    break;
                }
                break;
            case 1903589369:
                if (sampleMimeType.equals(MimeTypes.AUDIO_MLAW)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                return "aac";
            case true:
            case true:
            case true:
                return "mp3";
            case true:
                return "ac3";
            case true:
            case true:
                return "eac3";
            case true:
                return "truehd";
            case true:
            case true:
                return "dca";
            case true:
                return "vorbis";
            case true:
                return "opus";
            case true:
                return "amrnb";
            case true:
                return "amrwb";
            case true:
                return "flac";
            case true:
                return "alac";
            case true:
                return "pcm_mulaw";
            case true:
                return "pcm_alaw";
            case true:
                return "h264";
            case true:
                return "hevc";
            case true:
                return "mpeg2video";
            default:
                return null;
        }
    }
}
