#!/bin/bash
rm -rf ../turbo-build/out-linux

./gradlew teavm:build

butler push teavm/build/dist/webapp lavaeater/jam-packed-christmas:html
