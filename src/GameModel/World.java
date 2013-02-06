package GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;

import GameModel.mobs.Mob;

/**
 * The dungeon in which the game is played. Holds the 35-room dungeon and all the
 * Area objects which represent each room. The gridMap has room for 400 areas.
 *
 */
@SuppressWarnings("serial")
public class World extends Observable implements Serializable {
	// FIELDS
	private Area[][] gridMap;
	private final int WORLD_WIDTH = 20;
	private final int WORLD_HEIGHT = 20;

	// METHODS
	/**
	 * Constructor. Generates a new 2D area array, and initializes all the rooms of
	 * the dungeon using the makeABunchOfRooms() method, which creates all the areas,
	 * places exits, and adds keys.
	 */
	public World() {
		gridMap = new Area[WORLD_WIDTH][WORLD_HEIGHT];
		makeABunchOfRooms();
		
	}
	
	private void makeABunchOfRooms() {
		Exit exit = new Exit(false, null);
		
		gridMap[10][0] = new Area(
						"Starting Area",
						"You're standing on the edge of"
						+ " a forest, north of your home town. You've heard rumours of an ancient"
						+ " keep built into the mountain in the forest, filled with treasures. Nobody"
						+ " has ever come back from the keep, which leaves you wondering how they know"
						+ " about the treasures, but nevertheless, you're ready to seek them out.");
		
		gridMap[10][0].addExit(exit, Direction.NORTH);

		gridMap[10][1] = new Area(
						"Forest Clearing",
						"You move north into the forest, the path"
						+ " growing more and more overgrown as you get deeper. After what feels like several"
						+ " hours of struggling through brush, you come to a clearing. The path you are on seems"
						+ " to bend around to the east; however, there is another path to the west, shaded by"
						+ " the trees overhead, which seems to have been made by someone - or something - trampling"
						+ " through the undergrowth.");

		
		gridMap[10][1].addExit(exit, Direction.SOUTH);
		gridMap[10][1].addExit(exit, Direction.WEST);
		gridMap[10][1].addExit(exit, Direction.EAST);

		gridMap[9][1] = new Area(
						"Cliff Face",
						"The trampled path leads up to a cliff face. It looks like"
						+ " there was once a tunnel built into the cliff; however, a rockslide has left it blocked off."
						+ " You see a glint of metal beneath one of the rocks, and on closer inspection, you find that"
						+ " it's a helmet on an old body - though where the body's arms are, or what made the scorch marks"
						+ " around the rock pile, you cannot say. It looks like there may be more loot, buried beneath" +
						"the rubble.");
		gridMap[9][1].addExit(exit, Direction.EAST);

		gridMap[11][1] = new Area(
						"Overgrown Path",
						"The path continues going east. You reach a break in the foliage overhead; looking" +
						"up, you see it's nearly noon.");
		gridMap[11][1].addExit(exit, Direction.WEST);
		gridMap[11][1].addExit(exit, Direction.EAST);

		gridMap[12][1] = new Area(
						"Dungeon Entrance",
						"You reach another clearing, and the path splits to the north and south."
						+ " To the south, the path is clearly well cleared, and you can hear the faint sound of music playing. To the north,"
						+ " however, the path leads up to the side of the mountain, and there you see your goal. A massive entryway looms out"
						+ " of the mountainside, its borders bound by iron bands, strange characters etched into the metal. You think you can"
						+ " make out a glint of light, somewhere down the tunnel, but you can't make out any sounds. And one thing's for certain:"
						+ " you want to avoid whatever left the massive clawprint in the mud just outside.");
		gridMap[12][1].addExit(exit, Direction.NORTH);
		gridMap[12][1].addExit(exit, Direction.SOUTH);
		gridMap[12][1].addExit(exit, Direction.WEST);		

		gridMap[12][0] = new Area(
						"Willie's Used Equipment Shop",
						"The path opens up into a circle of dirt that has been purposefully cleared;"
						+ " the forest is thick all around it. At the other end of the circle is the wrecked" +
						"remains of what was probably once a merchant's wagon. There are small tracks all around" +
						"the clearing, and the footprints remind you of times when you've had to hunt goblins out" +
						"from the woods around your village. Most of the merchant's goods are gone, and there's no" +
						"sign of the merchant himself but a large bloodstain.");
		gridMap[12][0].addExit(exit, Direction.NORTH);
		
		gridMap[12][2] = new Area(
				"Room #7",
				"The entrance hall must have been magnificent once. Now, it's mostly rubble. Giant stone columns line the hall, each"
				+ " covered in alcoves in which smashed statues sit. Large chunks of stone have fallen from the ceiling and walls. The"
				+ " sounds of small creatures skittering past comes from the shadows to either side.\n\n"
				+ " Someone has kept the torches on the pathway-side of the columns lit. You're not sure if this is lucky or not.");
		gridMap[12][2].addExit(exit, Direction.SOUTH);
		gridMap[12][2].addExit(exit, Direction.NORTH);
		
		gridMap[12][3] = new Area(
				"Room #8",
				"As the hallway leads into a corner passage, you start to see signs of what now lives in the fortress. Old skulls,"
				+ " mounted on crude spears, are arranged haphazardly around the room. In front of the west entryway, someone has painted"
				+ " the ground with dark red letters: \"Gob tertory! No huminz alowed!\" ");
		gridMap[12][3].addExit(exit, Direction.SOUTH);
		gridMap[12][3].addExit(exit, Direction.WEST);
		
		gridMap[11][3] = new Area(
				"Room #9",
				"You come to a large antechamber. Your path continues straight west, but to the north is a large gateway. Strangely, the"
				+ " goblins don't seem to have wanted to come near this door. Set in a large tablet in front of the door are 2 faintly glowing"
				+ " orbs. Both are clearly sealed tight into the tablet, but a third, empty socket sits above them. High above the door,"
				+ " something has seemingly melted a tunnel through the rock of the wall, but there's no way you could climb that high.");
		Item orbKey = new Item("Glowing Blue Orb", 2, "This glass orb has a bright blue aura about it, it is probably used for something");
	
		Exit lockedGate = new Exit(true, orbKey);
		gridMap[11][3].addExit(exit, Direction.WEST);
		gridMap[11][3].addExit(lockedGate, Direction.NORTH);
		//System.out.println("remember to lock the door");
		//gridMap[11][3].addExit(exit, Direction.NORTH);
		
		gridMap[11][3].addExit(exit, Direction.EAST);
		
		gridMap[10][3] = new Area(
				"Room #10",
				"Goblin sign is stronger here than ever; it looks like they may be using this as a dumping ground for anything they"
				+ " can't wield, wear, or eat. Piles of garbage line either side of the passage. Perhaps there could be something" +
				"buried in the piles.");
		gridMap[10][3].addExit(exit, Direction.EAST);
		gridMap[10][3].addExit(exit, Direction.WEST);
		
		gridMap[9][3] = new Area(
				"Room #11",
				"You come to a crossway in the fortress. The lit torches go around a bend to the south, and a rickety wall of spears"
				+" stands pointing out from the doorway; they clearly grasp the principle of defense, but haven't accounted for anyone"
				+" smart enough to walk between two sticks coming through. Neither the west or the north passage has any torches, but"
				+" to the north, you see a faint blue glow, and hear an odd clicking noise echoing down the hall. The west doorway is"
				+" pitch black.");
		gridMap[9][3].addExit(exit, Direction.NORTH);
		gridMap[9][3].addExit(exit, Direction.SOUTH);
		gridMap[9][3].addExit(exit, Direction.EAST);
		gridMap[9][3].addExit(exit, Direction.WEST);
		
		gridMap[9][2] = new Area(
				"Room #12",
				"You have found the goblin's camp. Primitive cloth tents are poorly pitched on sticks around this large gallery, and"
				+" spoils of raiding are piled in the corners. You are interested in the large chest, along the south wall of the room."
				+" You are somewhat more interested in the angry goblins staring at you from their homes.");
		gridMap[9][2].addExit(exit, Direction.NORTH);
		
		gridMap[8][3] = new Area(
				"Room #13",
				"You stumble forward in the dark, into what seems to be a small room. It is pitch black. You are likely to be"
				+" eaten by a grue.");	
		gridMap[8][3].addExit(exit, Direction.EAST);
		
		gridMap[9][4] = new Area(
				"Room #14",
				"You walk into the largest room you've seen since the entry hall. The walls to either side of you hold row after row"
				+" of alcoves, and every one you can see into holds a coffin. Strange blue lights stream like smoke around the room,"
				+" making the shadows of statues and monuments dance. At the northern end of the room is a pedestal, on which sits a"
				+" softly glowing blue sphere, much like the ones from the antechamber you passed through before.\n\n"

				+" You quickly realize not all the movements are shadows, as several skeletons step out from shadowed alcoves around"
				+" you, blue light seeping from their eye sockets.");
		gridMap[9][4].addExit(exit, Direction.SOUTH);
		gridMap[9][4].addItem(orbKey);
		
		gridMap[11][4] = new Area(
				"Room #15",
				"The doors open into a long, uniform passage, stretching further north than you " +
				"can see. The passage is lined with broken statues, and many of the walls are covered " +
				"in patches of some sort of eerily growing lichen, illuminating the path ahead.");
		gridMap[11][4].addExit(exit, Direction.NORTH);
		gridMap[11][4].addExit(exit, Direction.SOUTH);
		
		gridMap[10][5] = new Area(
				"Room #16",
				"The staircase leads up into what sounds like a very large room, judging from the echos; you can't actually see anything" +
				"that indicates what it was for. You can, however, hear rustling from around the corners of the room.");
		gridMap[10][5].addExit(exit, Direction.EAST);
		
		gridMap[11][5] = new Area(
				"Room 17",
				"You reach a crossroad in the passage. To either side are stairs leading up. The eastern staircase is lit by the same" +
				" fungi as the passage, but the western stairs turn pitch black after only a few steps; it looks like something ripped" +
				" the lights down from the wall. The passage itself keeps stretching north, and you are beginning to notice that it has" +
				" started to slowly slope downward.");
		gridMap[11][5].addExit(exit, Direction.SOUTH);
		gridMap[11][5].addExit(exit, Direction.NORTH);
		gridMap[11][5].addExit(exit, Direction.EAST);
		gridMap[11][5].addExit(exit, Direction.WEST);
		
		gridMap[12][5] = new Area(
				"Room 18",
				"The staircase leads up into what was clearly once the armory. Most of the armor and weapons have either rusted or " +
				"been looted. The ones on the skeletons standing around the room, however, look perfectly intact. ");
		gridMap[12][5].addExit(exit, Direction.WEST);
		
		gridMap[11][6] = new Area(
				"Room 19",
				"The passageway continues, but you come to a spot where the air feels fresher. You look up, and see a giant tunnel " +
				"has been carved out of the top, leading all the way up to the mountain's surface; some sunlight comes down, but most" +
				"of it is blocked by the largest spider web you've ever seen. Then the web starts moving.");
		gridMap[11][6].addExit(exit,  Direction.NORTH);
		gridMap[11][6].addExit(exit, Direction.SOUTH);
		
		gridMap[11][7] = new Area(
				"Room 20",
				"The passageway finally comes to an end, splitting off into two ornate gates to the east and west. The eastern gate has " +
				"a massive keyhole in the center, but the western gate is hanging off its hinges. There are several cracks in the northern" +
				"wall, as though someone tried to block an alcove after the room's construction.");
		
		Item royalKey = new Item("Royal Key", 2, "A small key with a crown engraved on it.");
		Exit lockedGate2 = new Exit(true, royalKey);
		gridMap[11][7].addExit(lockedGate2, Direction.EAST);
		gridMap[11][7].addExit(exit, Direction.SOUTH);
		gridMap[11][7].addExit(exit, Direction.WEST);
		
		
		
		gridMap[10][7] = new Area(
				"Room 21",
				"You walk along a gallery that may once have been some kind of barracks; it is lined with many small, uniform rooms, many " +
				"of which have shelves or old, rotted bed frames. At the end of the barracks, the path turns north.");
		gridMap[10][7].addExit(exit, Direction.EAST);
		gridMap[10][7].addExit(exit, Direction.NORTH);
		
		gridMap[10][8] = new Area(
				"Room 23",
				"As the twisting passage starts to turn back to the west, you start noticing many odd things around you. The walls have " +
				"rods which probably once held massive tapestries, that have long since rotted away. Every once in a while you pass under " +
				"an arch, with runes inscribed in the surface underneath; they don't seem to be doing anything to you, but looking at them " +
				"for longer than a glance makes your whole body start to ache.");
		gridMap[10][8].addExit(exit, Direction.SOUTH);
		gridMap[10][8].addExit(exit, Direction.WEST);
		
		gridMap[9][8] = new Area(
				"Room 24",
				"The twisting passage finally ends in the biggest library you've ever seen. Row upon row of stacked shelves line the walls, " +
				"with what must be thousands of books stacked along them. You nearly don't notice the runes inscribed in the floor in front " +
				"of each shelf. They're similar to the ones from the arches; the difference is, these are glowing. It's probably best not " +
				"to touch the books.\n\nAt the far end, a large key lies on a table. It, too, is covered in runes. These don't seem quite as " +
				"important. Maybe the small, fiery red creatures perched atop it and baring their teeth are distracting you.");
		gridMap[9][8].addExit(exit, Direction.EAST);
		
		gridMap[12][7] = new Area(
				"Room 22",
				"You enter what was clearly once the center of the fortress. A massive iron throne looms from atop a set of stairs across " +
				"the room, with a molding, faded carpet leading up to it. To the north, however, you see something that wasn't in the " +
				"plans for the fortress - a massive tunnel, seemingly melted into the rock. The same giant claw marks you first saw outside " +
				"mark the ground at the tunnel's entrance, and a rich red  glow comes from inside.");
		gridMap[12][7].addExit(exit, Direction.WEST);
		gridMap[12][7].addExit(exit, Direction.NORTH);
		
		gridMap[12][8] = new Area(
				"Room 25",
				"You find the source of the glow - small pools of lava dot the edge of the tunnel, heating the air inside. The tunnel curves " +
				"around to the east.");
		gridMap[12][8].addExit(exit, Direction.SOUTH);
		gridMap[12][8].addExit(exit, Direction.EAST);
		
		gridMap[13][8] = new Area(
				"Room 26",
				"As you keep going, the pools of lava start getting larger. You reach a strange formation that was clearly carved with more care " +
				"than the rest of the tunnel - a ring of stone extends in an arch above you, with massive runes etched into it. These are very " +
				"different than those you saw before - sharply angular, and deeply incised rather than carefully inscribed.");
		gridMap[13][8].addExit(exit, Direction.WEST);
		gridMap[13][8].addExit(exit, Direction.EAST);
		
		gridMap[14][8] = new Area(
				"Room 27",
				"Shortly after the runic ring, the tunnel splits off to the north and south. You are quickly distracted by an odor which is " +
				"somehow strong enough to overcome the sulfurous fumes from the lava pits around you; you recognize it from stories your  " +
				"parents used to tell you when they wanted to scare you. The stench of rotted meat and blood.\n\nThis tunnel smells like troll.");
		gridMap[14][8].addExit(exit, Direction.NORTH);
		gridMap[14][8].addExit(exit, Direction.SOUTH);
		gridMap[14][8].addExit(exit, Direction.WEST);
		
		gridMap[14][9] = new Area(
				"Room 28",
				"The tunnel curves from south to east. The troll smell has not gone away, and now you see gigantic dents in the tunnel walls, " +
				"as though something huge slammed them with a blunt instrument.");
		gridMap[14][9].addExit(exit, Direction.SOUTH);
		gridMap[14][9].addExit(exit, Direction.EAST);
		
		gridMap[15][9] = new Area(
				"Room 29",
				"The tunnel goes straight. A pile of bones sits against the northern wall. Something has been gnawing on them.");
		gridMap[15][9].addExit(exit, Direction.EAST);
		gridMap[15][9].addExit(exit, Direction.WEST);
		
		gridMap[16][9] = new Area(
				"Room 30",
				"The tunnel curves from west to south. The lava isn't merely pools anymore; bright streams slowly drip out of holes in the walls.");
		gridMap[16][9].addExit(exit, Direction.WEST);
		gridMap[16][9].addExit(exit, Direction.SOUTH);
		
		gridMap[14][7] = new Area(
				"Room 31",
				"The tunnel curves from north to east. There are large, single-toed footprints in the floor here. Something must have been " +
				"heavy to make them in the hard rock.");
		gridMap[14][7].addExit(exit, Direction.NORTH);
		gridMap[14][7].addExit(exit, Direction.EAST);
		
		gridMap[15][7] = new Area(
				"Room 32",
				"The tunnel goes straight, and narrows a bit. You see scratches along either side of the wall; something came through here " +
				"that was nearly too big to fit.");
		gridMap[15][7].addExit(exit, Direction.EAST);
		gridMap[15][7].addExit(exit, Direction.WEST);
		
		gridMap[16][7] = new Area(
				"Room 33",
				"The tunnel curves from west to north. A suit of armor is lying in the middle of your path. It's probably not much use to " +
				"you, what with the entire left side being cooled molten slag.");
		gridMap[16][7].addExit(exit, Direction.WEST);
		gridMap[16][7].addExit(exit, Direction.NORTH);
		
		gridMap[16][8] = new Area(
				"Room 34",
				"You reach another fork in the tunnel. It continues going north to south, but there is a massive slab of iron up against the " +
				"eastern wall; thin waves of smoke leak out from the corners of it. To one side of it is a giant-sized lever, with a large, " +
				"iron padlocked chain holding it still.");
		gridMap[16][8].addExit(exit, Direction.NORTH);
		gridMap[16][8].addExit(exit, Direction.SOUTH);
		Item leverKey = new Item("Lever Key", 2, "");
		Exit lockedGate3 = new Exit(true, leverKey);
		gridMap[16][8].addExit(lockedGate3, Direction.EAST);
		
		gridMap[17][8] = new Area(
				"Dragon's Hoard",
				"The iron slab slams down like a drawbridge, leading out over a moat of lava. You've finally reached the end of the dungeon, " +
				"and see that the stories of treasure are true. Piles of gold lay haphazardly scattered around the gigantic cavern, and you " +
				"see gems and ancient equipment gleaming among them. It's so distracting that you almost don't notice the massive, scaly " +
				"shape looming up from the gigantic central pile and blinking at you lazily. Then it roars. You've certainly noticed it now.");
		gridMap[17][8].addExit(exit, Direction.WEST);

		gridMap[10][0].setSunny();
		gridMap[12][0].setSunny();
		gridMap[9][1].setSunny();
		gridMap[10][1].setSunny();
		gridMap[11][1].setSunny();
		gridMap[12][1].setSunny();
	}

	/**
	 * Adds a Character object to the World, at the location specified in the Character.
	 * @param ch
	 * 			The character being added.
	 * @return
	 * 			True if hte character was added, false otherwise.
	 */
	public boolean addCharacter(Character ch) {
		Point point = ch.getLocation();
		Area targetArea = gridMap[point.getX()][point.getY()];
		if (targetArea != null) {
			ch.setLocation(point);
			targetArea.addOccupant(ch);
			addNeighbors(ch);
			sendNotification(new Notification(getArea(ch.getLocation()).getAllPlayersExcept(ch), ch.getName() + " appears!"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the Area object located at the given point.
	 * @param p
	 * 			The location of the Area object.
	 * @return
	 * 			The Area object at p.
	 */
	public Area getArea(Point p) {
		return gridMap[p.getX()][p.getY()];
	}

	/** 
	 * Checks the passed direction for exits, locked exits, and the possibility
	 * of going out of bounds. Returns a string
	 * describing to the client what happened and moves the player if successful
	 * @param ch
	 * 			The character which is being moved.
	 * @param dir
	 * 			The direction in which they are being moved.
	 * @return
	 * 			Either "You can't move in that direction", or "<ch.name> leaves to
	 * 			the <dir>"
	 */
	public synchronized String moveCharacter(Character ch, Direction dir) {
		Area startingArea = this.getArea(ch.getLocation());
		int i = startingArea.checkExit(dir);

		if (i == 0) {
			return "You can't move in that direction";
		} else if (i == 2) {
			if (dir.move(ch.getLocation()).getX() >= WORLD_WIDTH
					|| dir.move(ch.getLocation()).getX() < 0
					|| dir.move(ch.getLocation()).getY() >= WORLD_HEIGHT
					|| dir.move(ch.getLocation()).getY() < 0) {
				return "You can't move in that direction";
			}
			String note;
			this.getArea(ch.getLocation()).removeOccupant(ch);
			setChanged();
			if (ch instanceof Player) {
				note = ch.getName() + " leaves to the "
						+ dir.toString().toLowerCase();
			} else {
				note = "a " + ch.getName() + " leaves to the "
						+ dir.toString().toLowerCase();
			}
			notifyObservers(new Notification(this.getArea(ch.getLocation())
					.getPlayers(), note));
			removeCharacter(ch);
			ch.moveRoom(dir);
			ch.clearNeighbors();
			addNeighbors(ch);
			Area area = this.getArea(ch.getLocation());
			Iterator<Character> occupants = area.getOccupants();
			while(occupants.hasNext()){
				occupants.next().addNeighbor(ch);
			}
			if (ch instanceof Player) {
				note = ch.getName() + " enters from the "
						+ dir.opposite().toString().toLowerCase();
			} else {
				note = "a " + ch.getName() + " enters from the "
						+ dir.opposite().toString().toLowerCase();
			}
			setChanged();
			notifyObservers(new Notification(this.getArea(ch.getLocation())
					.getPlayers(), note));
			area.addOccupant(ch);

			String newRoomDescription = "Moving to the " + dir.toString().toLowerCase();
			newRoomDescription += "\n\n" + this.getArea(ch.getLocation()).getDescription(ch);
			if(ch instanceof Player){
				sendPlayer((Player)ch);
			}
			return newRoomDescription;
		} else {
			return "This exit is locked. If you have the key try the use command";
		}
	}
	
	private void addNeighbors(Character c){
		Area area = this.getArea(c.getLocation());
		Iterator<Character> occupants = area.getOccupants();
		while(occupants.hasNext()){
			Character occupant = occupants.next();
			occupant.addNeighbor(c);
			c.addNeighbor(occupant);
		}
	}
	
	/**
	 * Advances the character towards the target. This moves the character mover 1
	 * space towards the character target.
	 * @param mover
	 * 			The character advancing.
	 * @param target
	 * 			The character being advanced on.
	 */
	public void advance(Character mover, Character target){
		
		if(mover != target){
			int range;
			String rangeDescription;
			range = mover.closerTo(target, 1);
			if(range < 4 && range > 0 && mover.isDead() == false && target.isDead() == false){
				target.closerTo(mover, 1);
				if(range == 1){	rangeDescription = "melee";	}
				else if(range == 2){rangeDescription = "middle"; }
				else{ rangeDescription = "long"; }
				if(mover instanceof Player){
					sendNotification(new Notification((Player)mover, "You advance closer to " + target.getName() + ". You are now at " + rangeDescription + " range.", false));
				}
				if(target instanceof Player){
					sendNotification(new Notification((Player)target, mover.getName() + " advances closer to you. You are now at "
							+ rangeDescription + " range.", false));
				}
				sendNotification(new Notification(this.getArea(mover.getLocation()).getAllPlayersExcept(mover, target), mover.getName() + " advances to " + rangeDescription + " range with " + target.getName(), false));
			}			
		}
		else{
			if(mover instanceof Player){
				sendNotification(new Notification((Player)mover, "You can't advance on yourself", false));
			}
		}
	}
	/**
	 * Moves the character mover one space away from the character target.
	 * @param mover
	 * 			The character retreating.
	 * @param target
	 * 			The character being retreated from.
	 */
	public void retreat(Character mover, Character target){
		
		if(mover != target){
			int range;
			String rangeDescription;
			range = mover.furtherFrom(target, 1);
			target.furtherFrom(mover, 1);
			if(range == 1){	rangeDescription = "melee";	}
			else if(range == 2){rangeDescription = "middle"; }
			else{ rangeDescription = "long"; }
			if(mover instanceof Player){
				sendNotification(new Notification((Player)mover, "You retreat further from " + target.getName()
						+ ". You are now at " + rangeDescription + " range.", false));
			}
			if(target instanceof Player){
				sendNotification(new Notification((Player)target, mover.getName() + " retreats away from you. You are now at "
						+ rangeDescription + " range.", false));
			}
			sendNotification(new Notification(this.getArea(mover.getLocation()).getAllPlayersExcept(mover, target), mover.getName() + " retreats to " + rangeDescription + " range with " + target.getName(), false));
		}
		else{
			if(mover instanceof Player){
				sendNotification(new Notification((Player)mover, "You can't retreat from yourself", false));
			}
		}
	}

	/**
	 * Says the message to character ch's client as a notification, as well
	 * as anyone in the same room as ch.
	 * @param ch
	 * 			The character who is sending the message.
	 * @param message
	 * 			The message being sent.
	 * @param isSocial
	 * 			False if the name is meant to be displayed, true otherwise.
	 */
	public void say(Character ch, String message, boolean isSocial) {
		setChanged();
		if (isSocial) {
			notifyObservers(new Notification(this.getArea(ch.getLocation())
					.getPlayers(), message, true));
		} else {
			notifyObservers(new Notification(this.getArea(ch.getLocation())
					.getPlayers(), ch.getName() + ": " + message, true));
		}
	}
	
	private void removeCharacter(Character ch){
		Area a = this.getArea(ch.getLocation());
		a.removeOccupant(ch);
		ch.clearNeighbors();
		Iterator<Character> occupants = a.getOccupants();
		while(occupants.hasNext()){
			Character c = occupants.next();
			if(ch != c){
				c.removeNeighbor(ch);
			}
		}
		
	}

	/**
	 * Starts combat between the attacker and the defender.
	 * @param attacker
	 * 			The character who is striking in combat.
	 * @param defender
	 * 			The character who is being struck.
	 */
	public synchronized void attack(Character attacker, Character defender) {
		if(attacker.readyToAttack()){
			int distanceApart = attacker.distanceFrom(defender);
			if (distanceApart <= attacker.getAttackRange() && distanceApart != 0) {
				attacker.attack();
				int chanceToHit = attacker.getAccuracyBonus() - defender.getDodgeBonus()	+ 85;
				if (chanceToHit > 95) {	chanceToHit = 95;	}
				if (Math.random() * 100 < chanceToHit) {
					/*
					 * the formula below will make the defender absorb a percentage
					 * of the hit instead of a fixed number
					 */
					double initialDamage = attacker.getDamage();
					int finalDamage;
					Random rand = new Random();
					double randomModifier = ((rand.nextDouble()*50) + 75)/100;
					initialDamage *= randomModifier;
					initialDamage = initialDamage - (initialDamage	* (defender.getDamageReduction()) / 100); 
					finalDamage = (int)initialDamage;
					defender.takeDamage(finalDamage);

					sendNotification(new Notification((this.getArea(attacker.getLocation()).getAllPlayersExcept(attacker, defender)), attacker.getName() + " hits " + defender.getName() + " with his " + attacker.getAttackName()));
					if(defender instanceof Mob){
						((Mob) defender).attackedBy(attacker);
						if(attacker instanceof Player){
							sendNotification(new Notification((Player)attacker, "You have hit " + defender.getName() + " for " + finalDamage + " damage with your " + attacker.getAttackName() + "!", false));
							
						}					
					}
					else if(defender instanceof Player){
						if(attacker instanceof Player){
							sendNotification(new Notification((Player)attacker, "You have hit " + defender.getName() + " for " + finalDamage + " damage with your " + attacker.getAttackName() + "!", false));
						}
						sendNotification(new Notification((Player)defender, attacker.getName() + " has hit you for " + finalDamage + " damage with his " + attacker.getAttackName() + "!", false));
						sendPlayer((Player)defender);
					}
					
					if(defender.isDead()){
						//Drop the inventory of defender on the ground
						if(attacker instanceof Player || defender instanceof Player){
							for(int i = 0; i < defender.getInventory().size(); i++){
								this.getArea(defender.getLocation()).addItem(defender.getInventory().get(i));
							}
						}						
						removeCharacter(defender);
						if(defender instanceof Player){
							sendNotification(new Notification(this.getAllPlayers(), "The valiant hero " + defender.getName() + " has fallen!", false));
						}
						else if(defender instanceof Mob){
							if(attacker instanceof Player){
								sendNotification(new Notification((Player)attacker, "You have slain the " + defender.getName() + "! His possessions fall to the floor", false));
								if(((Player) attacker).gainExperience(((Mob)defender).getExperience()) == true){
									((Player)attacker).takeDamage(-999);
									sendNotification(new Notification((Player)attacker, "Congratulations, you have leveled up! You have earned 3 stat increases. \nTry using the increase <stat> command", false));
								}
								sendPlayer((Player)attacker);
							}
							sendNotification(new Notification(this.getArea(defender.getLocation()).getAllPlayersExcept(attacker), attacker.getName() + " has slain the " + defender.getName() + "!"));
							
							//STOP THE MOB's THREAD((Mob)defender)
							((Mob)defender).kill();							
						}
					}
				} else {
					if(defender instanceof Player){
						sendNotification(new Notification((Player)defender, attacker.getName() + " attacks you but you dodge nimbly out of the way!", false));
					}
					if(attacker instanceof Player){
						sendNotification(new Notification((Player) attacker, "You attack " + defender.getName() + " but it dodges out of the way", false));
					}
					sendNotification(new Notification(this.getArea(attacker.getLocation()).getAllPlayersExcept(attacker, defender), attacker.getName() + " attacks " + defender.getName() + " with his " + attacker.getAttackName() + " but misses.", false));
				}
			} 
			else if(distanceApart == 0){
				if (attacker instanceof Player) {
					String note = "There is nothing by that name in the area";
					setChanged();
					notifyObservers(new Notification((Player) attacker, note, false));
				} else {
					
					System.out.println("BUG For some reason a MOB is attempting to attack someone who is not in the room BUG");
				}
			}
			else{
				if(attacker instanceof Player){
					sendNotification(new Notification((Player) attacker, defender.getName() + " is out of range, advance closer or equip a ranged weapon", false));
				}
			}
		}
		else{
			if(attacker instanceof Player){
				sendNotification(new Notification((Player) attacker, "You are not ready to attack yet, you must regain your balance", false));
			}
			
		}
		
	}
	
	/**
	 * Generates an iterator of all the players in the world.
	 * @return
	 * 			An iterator of all the players in the world.
	 */
	public Iterator<Player> getAllPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (gridMap[i][j] != null) {
					Iterator<Player> temp = gridMap[i][j].getPlayers();
					while (temp.hasNext()) {
						players.add(temp.next());
					}
				}
			}
		}
		
		return players.iterator();
	}
	
	/**
	 * Makes player p take the given item from the room they are located in.
	 * @param p
	 * 			The player doing the taking.
	 * @param itemName
	 * 			The item being taken
	 * @return
	 * 			True if the item was taken successfully, false otherwise.
	 */
	public boolean takeItem(Player p, String itemName){
		itemName = itemName.toLowerCase();
		Item itemBeingTaken = this.getArea(p.getLocation()).removeItem(itemName);
		if(itemBeingTaken != null){
			p.giveItem(itemBeingTaken);
			setChanged();
			notifyObservers(new Notification(p, "You pick up the " + itemBeingTaken.getName(), false));
			setChanged();
			notifyObservers(new Notification(this.getArea(p.getLocation()).getAllPlayersExcept(p),p.getName() + " reaches down and picks up the " + itemName, false));
			return true;
		}
		setChanged();
		notifyObservers(new Notification(p, "There is no item by that name here", false));
		return false;
	}
	
	/**
	 * Sends the notification to the clients.
	 * @param n
	 * 			The notification being sent.
	 */
	public void sendNotification(Notification n){
		setChanged();
		notifyObservers(n);
	}
	
	/**
	 * Sends the player to the clients.
	 * @param p
	 * 			The player object being sent.
	 */
	public void sendPlayer(Player p){
		setChanged();
		notifyObservers(p);
	}
} 
