#!/bin/bash
FFMPEG_MODULE_PATH="$(pwd)/extensions/ffmpeg/src/main"

NDK_PATH="/home/ubuntu/Desktop/andk"

HOST_PLATFORM="linux-x86_64"

ENABLED_DECODERS=(alac pcm_mulaw pcm_alaw mp3 aac ac3 eac3 dca mlp truehd h264 hevc mpeg2video)

FFMPEG_PATH="$(pwd)/ffmpeg"

cd ffmpeg && \
git clone git://source.ffmpeg.org/ffmpeg && \
cd ffmpeg && \
git checkout release/4.2 && \

cd "${FFMPEG_MODULE_PATH}/jni" && \
ln -s "$FFMPEG_PATH" ffmpeg
cd "${FFMPEG_MODULE_PATH}/jni" && \
./build_ffmpeg.sh \
  "${FFMPEG_MODULE_PATH}" "${NDK_PATH}" "${HOST_PLATFORM}" "${ENABLED_DECODERS[@]}"
  
 #/home/ubuntu/Desktop/ExoPlayer/extensions/ffmpeg/src/main/jni

