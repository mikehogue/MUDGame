## MUD (Multi User Dungeon)
The game was developed as the final project of my CSC 335: Object Oriented Programming and Design course in December, 2011 in a team of four. The game received a mark of 105% (as there were extra-tricky options that could be added for bonus points) and was a lot of fun to develop.

The 4 students who contributed to the game were...
- Michael Hogue
- Matthew Latura
- Mazen Shihab
- Christopher Conway

### Instructions

The game is server/client based. In order to play you must run Network.Server (Server UI) and then connect to it through the
View.MainFrame (Client UI). If playing locally, simply connect using the default settings. Otherwise, a port must be opened on the servers system, and the IP address of the server must be put into the client.

Gameplay is controlled through text commands. The player will enter commands into the textbox at the bottom of the MainFrame once they are logged in. A list of the acceptable commands can be found in the Help menu of the UI. 

### Screenshots

[Welcome to the MUD!](http://i.imgur.com/NKtHq)

[Login](http://i.imgur.com/kgzsh)

[Server Startup](http://i.imgur.com/OtyyK)

### Possible Improvements and Known Bugs

Because the application was built so rapidly, there are some bugs in the application.

	1. The use of multithreading for each individual enemy introduces way too much complexity to the code and is by far the most common
	source of any random crashes while playing. A much simpler implementation could have utilized a single extra thread which loops
	through each enemy character and requests and carries out their next action.
	
	2. Sometimes the game will not recognize that certain items are equipped. However, the items are still usable.
	
	3. On a semi-frequent basis, the Client UI will not load properly upon login. There is likely an issue with the impletation of Java's Swing API.
	
	4. The game is very unsophisticated and basic. With more time, the game could have had much more features, such as color-coded text, more items, a bigger map, more enemies, etc.