#!/usr/bin/env bash


export ANDROID_NDK=$(cat local.properties |grep ndk.dir|awk -F '=' '{print $2}')

# Download
[ -d external ] && rm -rf external
mkdir external
wget https://www.cypherpunk.at/ocat/download/Source/current/onioncat-0.2.2.r571.tar.gz
tar xzf onioncat-0.2.2.r571.tar.gz -C external/
rm onioncat-0.2.2.r571.tar.gz

# Build
cd external/onioncat-0.2.2.r571
wget https://www.cypherpunk.at/onioncat_trac/export/HEAD/trunk/android/android_configure
sh android_configure
make
rm ../../app/src/main/assets/empty
cp src/ocat ../../app/src/main/assets/