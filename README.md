# RINEARN Graph 3D / Ver. 6
( &raquo; [Japanese README](./README_JAPANESE.md) )

![Signboard](./img/example_graph_images.png)

The repository for developing the next major version of RINEARN Graph 3D.

You can get the current version, Ver.5.6, from the following website:
<br />
[https://www.rinearn.com/graph3d/](https://www.rinearn.com/graph3d/)

## Requirements

* Java&reg; Development Kit (JDK), 17 or later

## How to Build

Execute 'build.bat' on Microsoft Windows&reg;, or execute 'build.sh' on other OSes as follows:

    cd <this_directory>
    sudo chmod +x ./build.sh
    ./build.sh

If Apache&reg; Ant&trade; is available on your environment, you can also build by Ant as follows:

    cd <this_directory>
    ant -f build.xml

As a result of the building, a JAR file "RinearnGraph3D.jar" will be generated.

## How to Run

Execute 'run.bat' on Microsoft Windows&reg;, or execute 'run.sh' on other OSes as follows:

    cd <this_directory>
    sudo chmod +x ./run.sh
    ./run.sh

## How to Use as a Library

Also, Let's compile an example code which uses RINEARN Graph 3D as a library:

    javac -cp ".;RinearnGraph3D.jar" TempExample.java    (for Windows)
    javac -cp ".:RinearnGraph3D.jar" TempExample.java    (for Linux)

(The difference between the above two lines is only a symbol ";" or ":".)

And execute as follows:

    java -cp ".;RinearnGraph3D.jar" TempExample    (for Windows)
    java -cp ".:RinearnGraph3D.jar" TempExample    (for Linux)



## API References

For RINEARN Graph 3D Ver.6, basically, we want to keep API compatibility with Ver.5.6.
You can see the API reference docs for Ver.5.6 from the followings:

* [English API Reference](https://www.rinearn.com/en-us/graph3d/api/)
* [Japanese API Reference](https://www.rinearn.com/ja-jp/graph3d/api/)

Please note that, on Ver.6, most of features in the above references have not been implemented yet.

Also, how to use RINEARN Graph 3D as a graph drawing library, using the above API, is explained in the following article:

* [Controlling and 3D Drawing by Java Language - RINEARN Graph 3D User's Guide](https://www.rinearn.com/en-us/graph3d/guide/api)


## About Us

RINEARN Graph 3D is being developed by a software development studio '[RINEARN](https://www.rinearn.com/)', based in Japan.
The author is Fumihiro Matsui, the founder of RINEARN.
Please free to contact us if you have any questions, feedbacks, and so on.

## References and Links

Following webpages may be useful if you need more information about RINEARN Graph 3D Ver.6, which is developed on this repository.

* "[RINEARN Graph 3D Updated and Next Version (Ver.6) Development Has Begun! - RINEARN News /  September 4, 2023](https://www.rinearn.com/en-us/info/news/2023/0904-software-update)": An article introducing the concept of Ver.6.


---

### Trademarks and Credits

- Oracle and Java are registered trademarks of Oracle and/or its affiliates. 

- Microsoft Windows is either a registered trademarks or trademarks of Microsoft Corporation in the United States and/or other countries. 

- Other names may be either a registered trademarks or trademarks of their respective owners. 
