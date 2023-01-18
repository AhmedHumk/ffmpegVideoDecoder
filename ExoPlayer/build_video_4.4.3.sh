#!/bin/bash
export NDK=/home/ubuntu/Desktop/andk
export PREFIX=`pwd`/build
export SONAME=libexoplayer-ffmpeg.so

echo NDK-Dir=${NDK}
echo PREFIX=${PREFIX}

root_dir=`pwd`

cd $root_dir/ffmpeg-4.4.3
#./configure
chmod +x ./build_video.sh
./build_video.sh
