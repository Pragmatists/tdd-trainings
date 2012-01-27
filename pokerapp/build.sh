#!/bin/sh
#
# BASH script which will compile and build the PokerApp.jar file.
#
#
# Location of JAVA compiler and jar program should be verified.
#
JAVAC=/usr/local/bin/javac
JAR=/usr/lib/java/bin/jar

WD=$(pwd)
SRC=$WD/src
BLD=$WD/bld

echo
echo   ---- COMPILING JAVA SOURCE FILES ----
echo Using $JAVAC as the compiler
echo Source files are located at : $SRC
echo Classes will be built to    : $BLD
echo

$JAVAC -source 1.4 -d $BLD -classpath $BLD                           \
      $SRC/net/sourceforge/pokerapp/Card.java                        \
      $SRC/net/sourceforge/pokerapp/Dealer.java                      \
      $SRC/net/sourceforge/pokerapp/Deck.java                        \
      $SRC/net/sourceforge/pokerapp/Hand.java                        \
      $SRC/net/sourceforge/pokerapp/HandEvaluator.java               \
      $SRC/net/sourceforge/pokerapp/MessageFrame.java                \
      $SRC/net/sourceforge/pokerapp/NoPreferences.java               \
      $SRC/net/sourceforge/pokerapp/NoPreferencesFactory.java        \
      $SRC/net/sourceforge/pokerapp/PApp.java                        \
      $SRC/net/sourceforge/pokerapp/Picture.java                     \
      $SRC/net/sourceforge/pokerapp/Player.java                      \
      $SRC/net/sourceforge/pokerapp/PokerApp.java                    \
      $SRC/net/sourceforge/pokerapp/PokerApplet.java                 \
      $SRC/net/sourceforge/pokerapp/PokerClient.java                 \
      $SRC/net/sourceforge/pokerapp/PokerFrame.java                  \
      $SRC/net/sourceforge/pokerapp/PokerGame.java                   \
      $SRC/net/sourceforge/pokerapp/PokerMenuBar.java                \
      $SRC/net/sourceforge/pokerapp/PokerModel.java                  \
      $SRC/net/sourceforge/pokerapp/PokerMoney.java                  \
      $SRC/net/sourceforge/pokerapp/PokerMultiServerThread.java      \
      $SRC/net/sourceforge/pokerapp/PokerProtocol.java               \
      $SRC/net/sourceforge/pokerapp/PokerServerListener.java         \
      $SRC/net/sourceforge/pokerapp/PokerView.java                   \
      $SRC/net/sourceforge/pokerapp/ServerFrame.java                 \
      $SRC/net/sourceforge/pokerapp/ServerMenuBar.java               \
      $SRC/net/sourceforge/pokerapp/StartFrame.java                  \
      $SRC/net/sourceforge/pokerapp/StartPoker.java                  \
      $SRC/net/sourceforge/pokerapp/TalkFrame.java                   \
      $SRC/net/sourceforge/pokerapp/ai/AIAction.java                 \
      $SRC/net/sourceforge/pokerapp/ai/AIApp.java                    \
      $SRC/net/sourceforge/pokerapp/ai/AILogic.java                  \
      $SRC/net/sourceforge/pokerapp/ai/AILogic_simple.java           \
      $SRC/net/sourceforge/pokerapp/ai/AIFrame.java                  \
      $SRC/net/sourceforge/pokerapp/ai/PlayerModel.java              \
      $SRC/net/sourceforge/pokerapp/games/Anaconda.java              \
      $SRC/net/sourceforge/pokerapp/games/CrazyPineapple.java        \
      $SRC/net/sourceforge/pokerapp/games/FiveCardDraw.java          \
      $SRC/net/sourceforge/pokerapp/games/FiveCardShipwreck.java     \
      $SRC/net/sourceforge/pokerapp/games/FiveCardStud.java          \
      $SRC/net/sourceforge/pokerapp/games/HideTheSalami.java         \
      $SRC/net/sourceforge/pokerapp/games/HighChicagoShipwreck.java  \
      $SRC/net/sourceforge/pokerapp/games/IronCross.java             \
      $SRC/net/sourceforge/pokerapp/games/IronCrossAMod.java         \
      $SRC/net/sourceforge/pokerapp/games/IronCrossWildCenter.java   \
      $SRC/net/sourceforge/pokerapp/games/LowChicago.java            \
      $SRC/net/sourceforge/pokerapp/games/Omaha8.java                \
      $SRC/net/sourceforge/pokerapp/games/OmahaHoldEm.java           \
      $SRC/net/sourceforge/pokerapp/games/OmahaShipwreck.java        \
      $SRC/net/sourceforge/pokerapp/games/Pineapple.java             \
      $SRC/net/sourceforge/pokerapp/games/SevenCardStud.java         \
      $SRC/net/sourceforge/pokerapp/games/Shipwreck.java             \
      $SRC/net/sourceforge/pokerapp/games/TexasHoldEm.java           \
      $@

echo  ---- COMPILE COMPLETE ----
echo 
echo  ---- BUILDING POKERAPP.JAR AND POKERAPPLET.JAR FILES ----
echo Using $JAR to create .jar file
echo
cd $BLD
$JAR -cmf ../manifest_startpoker PokerApp.jar net/sourceforge/pokerapp/* Images/*
mv PokerApp.jar $WD/PokerApp.jar
$JAR -cmf ../manifest_pokerapplet PokerApplet.jar net/sourceforge/pokerapp/* Images/*
mv PokerApplet.jar $WD/PokerApplet.jar
cd $WD
echo  ---- BUILD COMPLETE ----
echo
