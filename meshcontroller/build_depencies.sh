#!/bin/bash

defaultTempDirectory="/tmp/install"


function install_rf24() {

    rf24Dir="$defaultTempDirectory/rf24"
    mkdir -p ${rf24Dir}

    echo "--- Creating libary directory ..."
    rf24LibraryDir="$rf24Dir/RF24"
    cd ${rf24Dir} && git clone https://github.com/nRF24/RF24.git ${rf24LibraryDir}> /dev/null

    echo "--- Configuring RF24 driver ..."
    (cd ${rf24LibraryDir} && ./configure --driver=RPi)
    echo "--- Installing RF24 ..."
    (cd ${rf24LibraryDir} && make install) > /dev/null

    cd ${rf24Dir} && git clone https://github.com/nRF24/RF24Network.git > /dev/null
    rf24NetworkLibraryDir="$rf24Dir/RF24Network"

    echo "--- Installing RF24Network ..."
    (cd ${rf24NetworkLibraryDir} && make install) > /dev/null

    cd ${rf24Dir} && git clone https://github.com/nRF24/RF24Mesh.git > /dev/null
    rf24MeshLibraryDir="$rf24Dir/RF24Mesh"

    echo "--- Installing Rf24Mesh ..."
    (cd ${rf24MeshLibraryDir} && make install) > /dev/null

}

function install_cpprest() {
    jobs=$1

    cd ${defaultTempDirectory}
    restsdkDir="$defaultTempDirectory/cpprestsdk"
    restsdkBuildDir="$restsdkDir/Build_release"

    echo "Cloning cpprestsdk ..."
    git clone https://github.com/Microsoft/cpprestsdk.git

    mkdir -p ${restsdkBuildDir}

    (cd "$restsdkBuildDir" && cmake ../Release -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=OFF -DBUILD_TESTS=OFF -DBUILD_SAMPLES=OFF)
	(cd "$restsdkBuildDir" && make -j ${jobs})
	(cd "$restsdkBuildDir" && make install) > /dev/null
}


mkdir -p ${defaultTempDirectory}
install_rf24
install_cpprest 4


rm -rf ${defaultTempDirectory}
