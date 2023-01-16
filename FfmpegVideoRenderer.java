package com.google.android.exoplayer2.ext.ffmpeg;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.decoder.Decoder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.decoder.VideoDecoderOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.DecoderVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

public class FfmpegVideoRenderer extends DecoderVideoRenderer {
    private static final String TAG = "FfmpegVideoRenderer";
    private static final int DEFAULT_NUM_OF_INPUT_BUFFERS = 4;
    private static final int DEFAULT_NUM_OF_OUTPUT_BUFFERS = 4;
    private static final int DEFAULT_INPUT_BUFFER_SIZE = ((Util.ceilDivide(1280, 64) * Util.ceilDivide(720, 64)) * 6144) / 2;
    private final int numInputBuffers;
    private final int numOutputBuffers;
    private final int threads;
    @Nullable
    private FfmpegVideoDecoder decoder;

    public FfmpegVideoRenderer(long allowedJoiningTimeMs, @Nullable Handler eventHandler, @Nullable VideoRendererEventListener eventListener, int maxDroppedFramesToNotify) {
        this(allowedJoiningTimeMs, eventHandler, eventListener, maxDroppedFramesToNotify, Runtime.getRuntime().availableProcessors(), 4, 4);
    }

    public FfmpegVideoRenderer(long allowedJoiningTimeMs, @Nullable Handler eventHandler, @Nullable VideoRendererEventListener eventListener, int maxDroppedFramesToNotify, int threads, int numInputBuffers, int numOutputBuffers) {
        super(allowedJoiningTimeMs, eventHandler, eventListener, maxDroppedFramesToNotify);
        this.threads = threads;
        this.numInputBuffers = numInputBuffers;
        this.numOutputBuffers = numOutputBuffers;
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    @SuppressLint({"WrongConstant"})
    public final int supportsFormat(Format format) {
        String mimeType = (String) Assertions.checkNotNull(format.sampleMimeType);
        if (!FfmpegLibrary.isAvailable() || !MimeTypes.isVideo(mimeType)) {
            return 0;
        }
        if (!FfmpegLibrary.supportsFormat(format)) {
            return RendererCapabilities.create(1);
        }
        if (format.drmInitData != null) {
            return RendererCapabilities.create(2);
        }
        return RendererCapabilities.create(4, 16, 0);
    }

    @Override // com.google.android.exoplayer2.video.DecoderVideoRenderer
    protected Decoder<DecoderInputBuffer, VideoDecoderOutputBuffer, FfmpegDecoderException> createDecoder(Format format, @Nullable CryptoConfig cryptoConfig) throws FfmpegDecoderException {
        TraceUtil.beginSection("createFfmpegVideoDecoder");
        int initialInputBufferSize = format.maxInputSize != -1 ? format.maxInputSize : DEFAULT_INPUT_BUFFER_SIZE;
        Decoder decoder = new FfmpegVideoDecoder(this.numInputBuffers, this.numOutputBuffers, initialInputBufferSize, this.threads, format);
        this.decoder = (FfmpegVideoDecoder) decoder;
        TraceUtil.endSection();
        Log.d(TAG, "createDecoder: " + decoder);
        return decoder;
    }

    @Override // com.google.android.exoplayer2.video.DecoderVideoRenderer
    protected void renderOutputBufferToSurface(VideoDecoderOutputBuffer outputBuffer, Surface surface) throws FfmpegDecoderException {
        try {
            if (this.decoder == null) {
                throw new FfmpegDecoderException("Failed to render output buffer to surface: decoder is not initialized.");
            }
            this.decoder.renderToSurface(outputBuffer, surface);
            outputBuffer.release();
        } catch (Exception e) {
        }
    }

    @Override // com.google.android.exoplayer2.video.DecoderVideoRenderer
    protected void setDecoderOutputMode(int outputMode) {
        if (this.decoder != null) {
            this.decoder.setOutputMode(outputMode);
        }
    }

    @Override // com.google.android.exoplayer2.Renderer, com.google.android.exoplayer2.RendererCapabilities
    public String getName() {
        return TAG;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public void setPlaybackSpeed(float currentPlaybackSpeed, float targetPlaybackSpeed) throws ExoPlaybackException {
    }

    @Override // com.google.android.exoplayer2.video.DecoderVideoRenderer
    protected DecoderReuseEvaluation canReuseDecoder(String decoderName, Format oldFormat, Format newFormat) {
        boolean sameMimeType = Util.areEqual(oldFormat.sampleMimeType, newFormat.sampleMimeType);
        return new DecoderReuseEvaluation(decoderName, oldFormat, newFormat, sameMimeType ? 3 : 0, sameMimeType ? 0 : 8);
    }
}
