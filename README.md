# ECE651-xl346-battleship
#### Run app:

```shell
./gradlew  installDist
./build/install/battleship/bin/battleship
```

To update your input/out files for testing main,

```shell
tee src/test/resources/input.txt | ./build/install/battleship/bin/battleship | tee src/test/resources/output.txt
```

#### Basic features:

- Four ship types with four orientations
  - 2 "Submarines"
  - 3 "Destroyers" 
  - 3 "Battleships"
  - 2 "Carriers" 

- Three possible actions for the player

  - F           Fire at a square
  - M         Move a ship to another square (at most 3 times)
  - S          Sonar scan (at most 3 times)

- Play against computer

  When the game starts, it should prompt the user for whether each of Player A and/or Player B is a human player or to be played by the computer (Any combination is valid: human vs human, human vs computer, computer vs human, or computer vs computer).

#### Extra Credit:

- Computer AI based on score matrix.

  Choose the point of greatest score to fire at and  select the point of lowest postive score as the center for a sonar scan. 

- Graphical User Interface.

  Replace the text-based interface with GUI that allows the player to use mouse to do the board initialization at the very beginning of the game.

- Option to convert to AI

  At the end of each turn, players can choose to have the AI do the rest of the game on their behalf.
