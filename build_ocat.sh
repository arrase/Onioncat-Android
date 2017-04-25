#!/usr/bin/env bash


ANDROID_NDK=$(cat local.properties |grep ndk.dir|awk -F '=' '{print $2}')

# Download
[ -d external ] || mkdir external
[ -d external/onioncat ] && rm -rf external/onioncat
wget https://www.cypherpunk.at/ocat/download/Source/current/onioncat-0.2.2.r571.tar.gz
tar xzf onioncat-0.2.2.r571.tar.gz -O external/onioncat
rm onioncat-0.2.2.r571.tar.gz

# Build
cd external/onioncat
wget https://www.cypherpunk.at/onioncat_trac/export/HEAD/trunk/android/android_configure
sh android_configure
make