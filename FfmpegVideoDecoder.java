package com.google.android.exoplayer2.ext.ffmpeg;

import android.view.Surface;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.VideoDecoderInputBuffer;
import com.google.android.exoplayer2.decoder.VideoDecoderOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.List;

/* loaded from: lib_mediaplayer_exo_r2.18.2_ff4.4_video_release_20230116.aar:classes.jar:com/google/android/exoplayer2/ext/ffmpeg/FfmpegVideoDecoder.class */
final class FfmpegVideoDecoder extends SimpleDecoder<VideoDecoderInputBuffer, VideoDecoderOutputBuffer, FfmpegDecoderException> {
    private static final int VIDEO_DECODER_SUCCESS = 0;
    private static final int VIDEO_DECODER_ERROR_INVALID_DATA = -1;
    private static final int VIDEO_DECODER_ERROR_OTHER = -2;
    private static final int VIDEO_DECODER_ERROR_READ_FRAME = -3;
    private static final int VIDEO_DECODER_ERROR_SEND_PACKET = -4;
    private final String codecName;
    private long nativeContext;
    @Nullable
    private final byte[] extraData;
    private volatile int outputMode;

    private native long ffmpegInitialize(String str, @Nullable byte[] bArr, int i);

    private native long ffmpegReset(long j);

    private native void ffmpegRelease(long j);

    private native int ffmpegRenderFrame(long j, Surface surface, VideoDecoderOutputBuffer videoDecoderOutputBuffer, int i, int i2);

    private native int ffmpegSendPacket(long j, ByteBuffer byteBuffer, int i, long j2);

    private native int ffmpegReceiveFrame(long j, int i, VideoDecoderOutputBuffer videoDecoderOutputBuffer, boolean z);

    public FfmpegVideoDecoder(int numInputBuffers, int numOutputBuffers, int initialInputBufferSize, int threads, Format format) throws FfmpegDecoderException {
        super(new VideoDecoderInputBuffer[numInputBuffers], new VideoDecoderOutputBuffer[numOutputBuffers]);
        if (!FfmpegLibrary.isAvailable()) {
            throw new FfmpegDecoderException("Failed to load decoder native library.");
        }
        this.codecName = (String) Assertions.checkNotNull(FfmpegLibrary.getCodecName(format));
        this.extraData = getExtraData(format.sampleMimeType, format.initializationData);
        this.nativeContext = ffmpegInitialize(this.codecName, this.extraData, threads);
        if (this.nativeContext == 0) {
            throw new FfmpegDecoderException("Failed to initialize decoder.");
        }
        setInitialInputBufferSize(initialInputBufferSize);
    }

    @Nullable
    private static byte[] getExtraData(String mimeType, List<byte[]> list) {
        int num;
        try {
            if (null == list) {
                throw new Exception();
            }
            try {
                num = list.size();
            } catch (Exception e) {
                num = 0;
            }
            if (num <= 0) {
                throw new Exception();
            }
            int length = 0;
            for (int i = 0; i < num; i++) {
                length += list.get(i).length;
            }
            byte[] extra = new byte[length];
            int destPos = 0;
            for (int i2 = 0; i2 < num; i2++) {
                byte[] bytes = list.get(i2);
                System.arraycopy(bytes, 0, extra, destPos, bytes.length);
                destPos = bytes.length;
            }
            return extra;
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public String getName() {
        return "ffmpeg" + FfmpegLibrary.getVersion() + "-" + this.codecName;
    }

    public void setOutputMode(int outputMode) {
        this.outputMode = outputMode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public VideoDecoderInputBuffer createInputBuffer() {
        return new VideoDecoderInputBuffer(2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public VideoDecoderOutputBuffer createOutputBuffer() {
        return new VideoDecoderOutputBuffer((v1) -> {
            releaseOutputBuffer(v1);
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    @Nullable
    public FfmpegDecoderException decode(VideoDecoderInputBuffer inputBuffer, VideoDecoderOutputBuffer outputBuffer, boolean reset) {
        if (reset) {
            this.nativeContext = ffmpegReset(this.nativeContext);
            if (this.nativeContext == 0) {
                return new FfmpegDecoderException("Error resetting (see logcat).");
            }
        }
        ByteBuffer inputData = (ByteBuffer) Util.castNonNull(inputBuffer.data);
        int inputSize = inputData.limit();
        boolean needSendAgain = false;
        int sendPacketResult = ffmpegSendPacket(this.nativeContext, inputData, inputSize, inputBuffer.timeUs);
        if (sendPacketResult == -1) {
            outputBuffer.setFlags(Integer.MIN_VALUE);
            return null;
        }
        if (sendPacketResult == -3) {
            needSendAgain = true;
        } else if (sendPacketResult == -2) {
            return new FfmpegDecoderException("ffmpegDecode error: (see logcat)");
        }
        boolean decodeOnly = inputBuffer.isDecodeOnly();
        int getFrameResult = ffmpegReceiveFrame(this.nativeContext, this.outputMode, outputBuffer, decodeOnly);
        if (getFrameResult == -4) {
            return null;
        }
        if (getFrameResult == -2) {
            return new FfmpegDecoderException("ffmpegDecode error: (see logcat)");
        }
        if (getFrameResult == -1) {
            outputBuffer.addFlag(Integer.MIN_VALUE);
        }
        if (!decodeOnly) {
            outputBuffer.format = inputBuffer.format;
        }
        if (needSendAgain) {
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public FfmpegDecoderException createUnexpectedDecodeException(Throwable error) {
        return new FfmpegDecoderException("Unexpected decode error", error);
    }

    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void release() {
        super.release();
        ffmpegRelease(this.nativeContext);
        this.nativeContext = 0L;
    }

    public void renderToSurface(VideoDecoderOutputBuffer outputBuffer, Surface surface) throws FfmpegDecoderException {
        if (outputBuffer.mode != 1) {
            throw new FfmpegDecoderException("Invalid output mode.");
        }
        if (ffmpegRenderFrame(this.nativeContext, surface, outputBuffer, outputBuffer.width, outputBuffer.height) == -2) {
            throw new FfmpegDecoderException("Buffer render error: ");
        }
    }
}
