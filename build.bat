:: ==================================================
:: A batch file for building RINEARN Graph 3D
:: ==================================================

mkdir bin
cd src

:: --------------------------------------------------
:: Compile Vnano Engine
:: --------------------------------------------------

javac @org/vcssl/connect/sourcelist.txt -d ../bin -encoding UTF-8
javac @org/vcssl/nano/sourcelist.txt -d ../bin -encoding UTF-8

:: --------------------------------------------------
:: Compile Vnano Standard Plug-ins
:: --------------------------------------------------

javac @org/vcssl/nano/plugin/sourcelist.txt -d ../bin -encoding UTF-8

:: --------------------------------------------------
:: Compile RINEARN Graph 3D
:: --------------------------------------------------

javac @com/rinearn/graph3d/sourcelist.txt -d ../bin -encoding UTF-8

:: --------------------------------------------------
:: Generate a JAR file
:: --------------------------------------------------

cd ..
jar cvfm RinearnGraph3D.jar src/com/rinearn/graph3d/Manifest.mf -C bin com -C bin org

:: --------------------------------------------------
:: Compile a temporary example code
:: --------------------------------------------------

javac -cp ".;RinearnGraph3D.jar" TempExample.java

