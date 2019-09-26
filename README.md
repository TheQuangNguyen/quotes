# Quotes

## Feature Tasks

1. Use the file [recentquotes.json](https://codefellows.github.io/code-401-java-guide/curriculum/08-oo-design-practice/recentquotes.json) to show random popular book quotes. Your program should use [GSON](https://github.com/google/gson) to parse the .json file. The app needs no functionality other than showing the quote and the author when it is run. The app should choose one quote each time it is run.
2. Allow users to type in an author (on the command line) and return a quote by that author.
3. Allow users to type in a word (on the command line) and return a quote that contains that word.
4. Allow both of those things at the same time, with two different command line args (for example, ```./gradlew run --args 'author Chimamanda'``` vs ```./gradlew run --args 'contains work'```)

## Application 

To run the application from the command line, type in ```./gradlew run``` at the root of the project directory and the program should return a string that contains a random quote from an author. 

Run the command ```./gradlew run --args 'author NameOfAuthorToSearch'``` and put in the name of the author that you want a quote from to return a quote from that author. If the author is not in the list of quotes, an exception will occur.

Run the command ```./gradlew run --args 'contains WordToSearch'``` and put the word that a quote you are searching will contain to return a quote that contains that word. If the word is not in the list of quotes, an exception will occur

