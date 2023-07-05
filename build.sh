#!/bin/sh

mkdir bin
cd src
javac @com/rinearn/graph3d/sourcelist.txt -d ../bin -encoding UTF-8
javac @temp/sourcelist.txt -d ../bin -encoding UTF-8
cd ..
