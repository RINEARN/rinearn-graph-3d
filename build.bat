:: ==================================================
:: A batch file for building RINEARN Graph 3D
:: ==================================================

:: --------------------------------------------------
:: Build Vnano Engine
:: --------------------------------------------------

cd lib\app-dependencies\vnano-engine
mkdir bin
cd src
javac @org/vcssl/nano/sourcelist.txt -d ../bin -encoding UTF-8
cd ..
jar cvfm Vnano.jar src/org/vcssl/nano/meta/main.mf -C bin org -C src/org/vcssl/nano/meta META-INF
cd ..\..\..\

:: --------------------------------------------------
:: Compile Vnano Standard Plug-ins
:: --------------------------------------------------

cd plugin
javac -encoding UTF-8 @org/vcssl/connect/sourcelist.txt
javac -encoding UTF-8 @org/vcssl/nano/plugin/sourcelist.txt
cd ..

:: --------------------------------------------------
:: Build RINEARN Graph 3D
:: --------------------------------------------------

mkdir bin
cd src
javac @com/rinearn/graph3d/sourcelist.txt -cp "../lib/app-dependencies/vnano-engine/Vnano.jar" -d ../bin -encoding UTF-8
cd ..
jar cvfm RinearnGraph3D.jar src/com/rinearn/graph3d/Manifest.mf -C bin com

