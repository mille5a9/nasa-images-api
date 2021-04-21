# nasa-images-api
An API implementation for retrieving images from NASA taken from the Mars rovers.

## contents
 The `.vscode/` folder includes the configuration settings I used when writing and testing the project with VS Code. The `lib/` folder includes the JUnit and org.json libraries which are used in this project. Finally, `src/` includes all of the source code as well as a few unit tests that serve to validate the cache functionality.

## installation
Clone this repository and compile `Program.java`. Make sure to insert a NASA API key in `key.properties`.

## usage
Call `Program` with one of a number of commands as an argument:
| Command   | Description |
| --- | --- |
| `help`  | Displays this list of command line arguments |
| `clean` | Removes any data older than ten days from the cache |
| `clear` | Removes all data from the cache |
| `query` | Displays image links for up to three Curiousity NAVCAM images from each of the past ten days |