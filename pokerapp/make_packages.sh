#!/bin/sh
# 
# This script is used to put together the delivered PokerApp packages.
# It creates both .tar.gz and .zip packages using the utilities that should
# be on the system.  Update utility locations as required
# The windows installer is created using InnoSetup on a Windows machine, so this
# script is unable to help do that.
#
TAR=/usr/bin/tar
GZIP=/usr/bin/gzip
ZIP=/usr/bin/zip

VER=$1

# Make the packages directory
mkdir packages > /dev/null 2>&1

# Make PokerApplet gzipped tar file
echo "Making PokerApplet.tar.gz ..."
$TAR -cvf PokerApplet_$VER.tar PokerApplet.* applet_inputs.txt license.txt readme.txt
$GZIP -9 PokerApplet_$VER.tar
mv PokerApplet_$VER.tar.gz packages/.
echo "DONE."
echo

# Make PokerApp gzipped tar file
echo "Making PokerApp.tar.gz ..."
$TAR -cvf PokerApp_$VER.tar PokerApp.* poker_inputs.txt license.txt readme.txt
$GZIP -9 PokerApp_$VER.tar
mv PokerApp_$VER.tar.gz packages/.
echo "DONE."
echo

# Make PokerApplet zip file
echo "Making PokerApplet.zip file ..."
$ZIP PokerApplet_$VER.zip PokerApplet.* applet_inputs.txt license.txt readme.txt
mv PokerApplet_$VER.zip packages/.
echo "DONE."
echo

# Make PokerApp .zip file
echo "Making PokerApp.zip file ..."
$ZIP PokerApp_$VER.zip PokerApp.* poker_inputs.txt license.txt readme.txt
mv PokerApp_$VER.zip packages/.
echo "DONE."
echo

# Make the PokerApp source package
echo "Making PokerApp source.tar.gz file ..."
$TAR -cvf PokerApp_$VER-src.tar src/* bld/Images/* applet_inputs.txt poker_inputs.txt license.txt readme.txt
$GZIP -9 PokerApp_$VER-src.tar
mv PokerApp_$VER-src.tar.gz packages/.
echo "DONE."
echo

echo "Making PokerApp source.zip file ..."
$ZIP PokerApp_$VER-src.zip src/* bld/Images/* applet_inputs.txt poker_inputs.txt license.txt readme.txt
mv PokerApp_$VER-src.zip packages/.
echo "DONE."
echo
