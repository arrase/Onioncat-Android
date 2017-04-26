#!/usr/bin/env bash


export ANDROID_NDK=$(cat local.properties |grep ndk.dir|awk -F '=' '{print $2}')
export CROSS_COMPILE=arm-linux-androideabi
export ANDROID_PREFIX=${ANDROID_NDK}/toolchains/arm-linux-androideabi-4.8/prebuilt/linux-x86_64
export SYSROOT=${ANDROID_NDK}/platforms/android-16/arch-arm
export CROSS_PATH=${ANDROID_PREFIX}/bin/${CROSS_COMPILE}

export CPP=${CROSS_PATH}-cpp
export AR=${CROSS_PATH}-ar
export AS=${CROSS_PATH}-as
export NM=${CROSS_PATH}-nm
export CC=${CROSS_PATH}-gcc
export LD=${CROSS_PATH}-ld
export RANLIB=${CROSS_PATH}-ranlib

export CFLAGS="${CFLAGS} --sysroot=${SYSROOT} -I${SYSROOT}/usr/include -I${ANDROID_PREFIX}/include -fvisibility=default -fPIE"
export CPPFLAGS="${CFLAGS}"
export LDFLAGS="${LDFLAGS} -L${SYSROOT}/usr/lib -L${ANDROID_PREFIX}/lib -rdynamic -fPIE -pie"

# Download
[ -d external ] && rm -rf external
mkdir external
wget https://www.cypherpunk.at/ocat/download/Source/current/onioncat-0.2.2.r571.tar.gz
tar xzf onioncat-0.2.2.r571.tar.gz -C external/
rm onioncat-0.2.2.r571.tar.gz

# Build
cd external/onioncat-0.2.2.r571
./configure --host=${CROSS_COMPILE} "$@"
make

# Install
[ -d ../../app/src/main/assets ] || mkdir ../../app/src/main/assets/
cp src/ocat ../../app/src/main/assets/