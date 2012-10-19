# Oxide [![Build Status](https://secure.travis-ci.org/ianbollinger/oxide.png)](http://travis-ci.org/ianbollinger/oxide)
Oxide is an integrated development environment for the [Rust programming language](http://rust-lang.org) that runs on the [Eclipse](http://eclipse.org) platform. This project is in its infancy, so expect things not to work properly.

## License
Oxide is licensed under [the MIT License](https://github.com/ianbollinger/oxide/blob/master/LICENSE.txt).

## Requirements
Oxide requires [Java](http://www.java.com) 7 and Eclipse 3.7 or 4.2.

## Building
1. Ensure [Apache Maven 3](http://maven.apache.org) is installed.
2. Run `mvn` in the project's base directory.
3. The built `jar` files will be located in the `build/` subdirectory.

See [build instructions](https://github.com/ianbollinger/oxide/wiki/Building-the-Oxide-plug-in) for additional information, like why building takes *forever*.

## Installation
Copy the plug-in `jar` files to your Eclipse `dropins/` directory.
