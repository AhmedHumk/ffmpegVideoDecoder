package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.decoder.DecoderException;

public final class FfmpegDecoderException extends DecoderException {
    public FfmpegDecoderException(String message) {
        super(message);
    }

    public FfmpegDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
