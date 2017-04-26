#!/usr/bin/env bash


export ANDROID_NDK=$(cat local.properties |grep ndk.dir|awk -F '=' '{print $2}')

# Download
[ -d external ] && rm -rf external
mkdir external
wget https://www.cypherpunk.at/ocat/download/Source/current/onioncat-0.2.2.r571.tar.gz
tar xzf onioncat-0.2.2.r571.tar.gz -C external/
rm onioncat-0.2.2.r571.tar.gz

# Build
cp android_configure.sh external/onioncat-0.2.2.r571/
cd external/onioncat-0.2.2.r571
sh android_configure
make

# Install
[ -d ../../app/src/main/assets ] || mkdir ../../app/src/main/assets/
cp src/ocat ../../app/src/main/assets/