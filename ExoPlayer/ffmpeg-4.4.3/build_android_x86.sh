#!/bin/bash
#Change NDK to your Android NDK location
PLATFORM=$NDK/platforms/android-18/arch-x86/
PREBUILT=$NDK/toolchains/x86-4.9/prebuilt/linux-x86_64


GENERAL="\
--enable-small \
--enable-cross-compile \
--extra-libs="-lgcc" \
--arch=x86 \
--cc=$PREBUILT/bin/i686-linux-android-gcc \
--cross-prefix=$PREBUILT/bin/i686-linux-android- \
--nm=$PREBUILT/bin/i686-linux-android-nm \
--extra-cflags="-I${PREFIX}/x264/android/x86/include" \
--extra-ldflags="-L${PREFIX}/x264/android/x86/lib""

temp_prefix=${PREFIX}/ffmpeg/android/x86
rm -rf $temp_prefix
export PATH=$PREBUILT/bin/:$PATH/


function build_x86
{
  ./configure \
  --pkg-config="pkg-config --static" \
  --logfile=conflog.txt \
  --target-os=linux \
  --prefix=${temp_prefix} \
  --arch=x86 \
  ${GENERAL} \
  --sysroot=$PLATFORM \
  --extra-cflags=" -O3 -DANDROID -Dipv6mr_interface=ipv6mr_ifindex -fasm -Wno-psabi -fno-short-enums -fno-strict-aliasing -fomit-frame-pointer -march=k8" \
  --enable-shared \
  --disable-static \
  --extra-cflags="-march=i686 -mtune=intel -mssse3 -mfpmath=sse -m32" \
  --extra-ldflags="-lx264 -Wl,-rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib -nostdlib -lc -lm -ldl -llog" \
  --enable-zlib \
  --disable-doc \
  ${COMMON_SET}

  make clean
  make
  make install
}

build_x86


echo Android X86 builds finished