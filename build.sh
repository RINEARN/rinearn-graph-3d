#!/bin/sh

# ==================================================
# A script for building RINEARN Graph 3D
# ==================================================

# --------------------------------------------------
# compile source files
# --------------------------------------------------

mkdir bin
cd src
javac @com/rinearn/graph3d/sourcelist.txt -d ../bin -encoding UTF-8
cd ..

# --------------------------------------------------
# create a JAR file
# --------------------------------------------------

jar cvfm RinearnGraph3D.jar src/com/rinearn/graph3d/meta/Manifest.mf -C bin com

