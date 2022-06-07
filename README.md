# ece651-sp20-xl346-battleship
Extra features:
1. Computer AI based on grading. Add a 2D-arrays named matrix_scores to each player for grading. AI would follow rules below:
----At the beginning of the game:
    ----Initialze all points to -1
----When adding ships to the board:
    ----randomly choose valid rows, columns and orientations(for submarine and destroyer are 'H' and 'V', f    or battleships and carriers are 'U', 'R', 'D' and 'L'). 
----When choosing actions:
    ----Currently we have 3 kinds of action: fire, move and sonar scan. we get a random number between 0 to 2. If i    t's 0, choose fire. Else if it's 1 and still have uses of move and there is at least 1 sunk ship, choos    e move. Else if it's 2 and still have uses of sonar scanning, choose sonar scanning
    ----When firing: 
        ----get the points with largest scores and randomly choose one of them. After firing, minus 2 score         s of this point.
    ----When moving:
        ----randomly choose a sunk ship and then move to a random and valid placement.
    ----When sonar scanning:
        ----get the points with smallest scores and randomly choose one of them. After scanning, sum up the        number of ships and add to each point within the scanning range.
    
2. Option that lets computer play for to human player in the middle of the game.
----This option appears after player taking action. If the player inputs 'yes', then computer would play for player in the rest of the game. If the player input 'no', continue to play.
