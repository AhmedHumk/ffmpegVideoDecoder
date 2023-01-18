#!/usr/bin/env bash

chmod a+x build_android_*.sh


export COMMON_SET="--enable-static \
  --disable-symver \
  --disable-network \
  --disable-debug \
  --disable-programs \
  --disable-ffmpeg \
  --disable-ffplay \
  --disable-ffprobe \
  --disable-doc \
  --disable-htmlpages \
  --disable-manpages \
  --disable-podpages \
  --disable-txtpages \
  --disable-everything \
  --disable-avdevice \
  --disable-avformat \
  --disable-swscale \
  --disable-postproc \
  --disable-avfilter \
  --disable-symver \
  --disable-avresample \
  --enable-swresample \
  --enable-avresample \
  --enable-decoder=vorbis \
  --enable-decoder=opus \
  --enable-decoder=flac \
  --enable-decoder=alac \
  --enable-decoder=pcm_mulaw \
  --enable-decoder=pcm_alaw \
  --enable-decoder=mp3 \
  --enable-decoder=amrnb \
  --enable-decoder=amrwb \
  --enable-decoder=aac \
  --enable-decoder=ac3 \
  --enable-decoder=eac3 \
  --enable-decoder=dca \
  --enable-decoder=mlp \
  --enable-decoder=truehd \
  --extra-ldexeflags=-pie \
  --enable-gpl \
  --enable-zlib \
  --enable-jni \
  --enable-nonfree \
  --enable-asm \
  --enable-version3 "

# Build arm v6 v7a
# ./build_android_armeabi.sh

# Build arm v7a
#make distclean
./build_android_armeabi_v7a.sh

# Build arm64 v8a
#make distclean
./build_android_arm64_v8a.sh

# Build x86
./build_android_x86.sh

# Build x86_64
./build_android_x86_64.sh

# Build mips
# ./build_android_mips.sh

# Build mips64   //may fail
# ./build_android_mips64.sh
