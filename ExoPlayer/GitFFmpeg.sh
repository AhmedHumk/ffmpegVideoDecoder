#!/bin/bash
FFMPEG_MODULE_PATH="$(pwd)/extensions/ffmpeg/src/main"
NDK_PATH="/home/ubuntu/Desktop/andk"
HOST_PLATFORM="linux-x86_64"
FFMPEG_PATH="$(pwd)/ffmpeg"
ENABLED_DECODERS=(vorbis opus aac ac3 eac3 aac_latm flv h264 pcm_mulaw pcm_alaw wmav* mp3* mp2* vp6f flac alac hevc vp8 vp9 mpeg2video mpegvideo)
#git clone git://source.ffmpeg.org/ffmpeg && \
#cd ffmpeg && \
#git checkout release/4.3 && \

cd "${FFMPEG_MODULE_PATH}/jni" && \
ln -s "$FFMPEG_PATH" ffmpeg
cd "${FFMPEG_MODULE_PATH}/jni" && \
./build_ffmpeg.sh \
  "${FFMPEG_MODULE_PATH}" "${NDK_PATH}" "${HOST_PLATFORM}" "${ENABLED_DECODERS[@]}"
