#!/usr/bin/env bash

chmod a+x build_android_*.sh





export COMMON_SET="--enable-static \
  --disable-symver \
  --disable-network \
  --disable-debug \
  --disable-doc \
  --disable-htmlpages \
  --disable-manpages \
  --disable-podpages \
  --disable-txtpages \
  --disable-ffplay \
  --disable-ffprobe \
  --disable-ffmpeg \
  --enable-avdevice \
  --enable-avcodec \
  --enable-avformat \
  --enable-swresample \
  --enable-swscale \
  --enable-postproc \
  --disable-avfilter \
  --enable-bsfs \
  --disable-devices \
  --disable-protocols \
  --enable-parsers \
  --enable-muxers \
  --enable-demuxers \
  --enable-decoders \
  --enable-encoders \
  --enable-swscale \
  --disable-filters \
  --enable-gpl \
  --enable-zlib \
  --disable-jni \
  --enable-nonfree \
  --disable-mediacodec \
  --enable-asm \
  --enable-version3 "

# Build arm v6 v7a
# ./build_android_armeabi.sh

# Build arm v7a
# make distclean
./build_android_armeabi_v7a.sh

# Build arm64 v8a
# make distclean
./build_android_arm64_v8a.sh

# Build x86
# ./build_android_x86.sh

# Build x86_64
# ./build_android_x86_64.sh

# Build mips
# ./build_android_mips.sh

# Build mips64   //may fail
# ./build_android_mips64.sh
