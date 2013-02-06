package GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import GameModel.mobs.BabySpider;
import GameModel.mobs.Dragon;
import GameModel.mobs.GiantRat;
import GameModel.mobs.GiantSpider;
import GameModel.mobs.Goblin;
import GameModel.mobs.GoblinShaman;
import GameModel.mobs.Grue;
import GameModel.mobs.Imp;
import GameModel.mobs.Mob;
import GameModel.mobs.SkeletonArcher;
import GameModel.mobs.SkeletonWarrior;
import GameModel.mobs.Troll;

/**
 * This class is in charge of managing the mobs in the MUD. It creates them on
 * startup and start's their run methods. It also handles the respawning of the
 * mobs
 * 
 * @author Mazen Shihab, Matthew Latura, Michael Hogue
 * 
 */
public class GameSimulation implements Serializable {

	private static final long serialVersionUID = 1L;
	LinkedList<Mob> mobs;

	/**
	 * The World that has the GameSimulation running.
	 */
	public World w;

	Runnable imp;
	private Map<Area, ArrayList<Item>> respawnables;

	/**
	 * The constructor for this will spawn Mobs in their appropriate starting
	 * locations within the passed World object and adds them to the mobs list
	 * 
	 * @param w
	 *            - the World in which to spawn the mobs
	 */
	public GameSimulation(World w) {
		this.w = w;

		respawnables = new HashMap<Area, ArrayList<Item>>();
		setUpRespawnables();
		respawn();

		mobs = new LinkedList<Mob>();

		// Spawns the dragon
		Runnable dragon = new Dragon(w, new Point(17, 8), this);
		mobs.add((Mob) dragon);

		// Spawns the troll
		Runnable troll = new Troll(w, new Point(16, 8), this);
		mobs.add((Mob) troll);

		// Spawns Skeleton Warriors
		// 2 in room 14, 3 in room 18
		for (int i = 0; i < 2; i++) {
			Runnable skeltonW = new SkeletonWarrior(w, new Point(9, 4), this);
			mobs.add((Mob) skeltonW);
		}

		for (int i = 0; i < 3; i++) {
			Runnable skeltonW = new SkeletonWarrior(w, new Point(12, 5), this);
			mobs.add((Mob) skeltonW);
		}

		// Spawns Skeleton Archers
		// 1 in room 14, 2 in room 18
		Runnable skeltonA = new SkeletonArcher(w, new Point(9, 4), this);
		mobs.add((Mob) skeltonA);
		for (int i = 0; i < 2; i++) {
			Runnable skeltonA2 = new SkeletonArcher(w, new Point(12, 5), this);
			mobs.add((Mob) skeltonA2);
		}

		// Spawns Goblins
		Runnable goblin = new Goblin(w, new Point(9, 3), this);
		mobs.add((Mob) goblin);
		Runnable goblin1 = new Goblin(w, new Point(10, 3), this);
		mobs.add((Mob) goblin1);
		Runnable goblin3 = new Goblin(w, new Point(11, 3), this);
		mobs.add((Mob) goblin3);

		for (int i = 0; i < 3; i++) {
			Runnable goblin2 = new Goblin(w, new Point(9, 2), this);
			mobs.add((Mob) goblin2);
		}

		// Spawns Goblin Shaman
		Runnable gshaman = new GoblinShaman(w, new Point(10, 3), this);
		mobs.add((Mob) gshaman);

		for (int i = 0; i < 2; i++) {
			Runnable goblinshaman = new GoblinShaman(w, new Point(9, 2), this);
			mobs.add((Mob) goblinshaman);
		}

		// Spawns Grue
		Runnable grue1 = new Grue(w, new Point(8, 3), this);
		mobs.add((Mob) grue1);
		Runnable grue2 = new Grue(w, new Point(10, 5), this);
		mobs.add((Mob) grue2);

		// Spawns Giant Rats
		for (int i = 0; i < 2; i++) {
			Runnable gr = new GiantRat(w, new Point(11, 1), this);
			mobs.add((Mob) gr);
		}
		for (int i = 0; i < 4; i++) {
			Runnable gr = new GiantRat(w, new Point(12, 3), this);
			mobs.add((Mob) gr);
		}

		// Spawns Imps
		for (int i = 0; i < 3; i++) {
			imp = new Imp(w, new Point(9, 8), this);
			mobs.add((Mob) imp);
		}

		// Spawns the Giant Spider
		for (int i = 0; i < 1; i++) {
			Runnable giantSpider = new GiantSpider(w, new Point(11, 6), this);
			mobs.add((Mob) giantSpider);
		}
	}

	private void lockDoors() {
		if (w.getArea(new Point(11, 3)).checkExit(Direction.NORTH) == 2) {
			Item orbKey = new Item(
					"Glowing Blue Orb",
					2,
					"This glass orb has a bright blue aura about it, it is probably used for something");
			Exit lockedGate = new Exit(true, orbKey);
			w.getArea(new Point(11, 3)).addExit(lockedGate, Direction.NORTH);
		}
		if (w.getArea(new Point(11, 7)).checkExit(Direction.EAST) == 2) {
			Item royalKey = new Item("Royal Key", 2,
					"A small key with a crown engraved on it.");
			Exit lockedGate = new Exit(true, royalKey);
			w.getArea(new Point(11, 7)).addExit(lockedGate, Direction.EAST);
		}
		if (w.getArea(new Point(16, 8)).checkExit(Direction.EAST) == 2) {
			Item leverKey = new Item("Lever Key", 2,
					"A large, crude key made from iron. It must fit a pretty big lock.");
			Exit lockedGate = new Exit(true, leverKey);
			w.getArea(new Point(16, 8)).addExit(lockedGate, Direction.EAST);
		}
	}

	private void setUpRespawnables() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(ItemFactory.makeArmor("Leather Helmet"));
		items.add(new Bomb());
		respawnables.put(w.getArea(new Point(10, 0)), items);

		ArrayList<Item> items2 = new ArrayList<Item>();
		items2.add(ItemFactory.makeWeapon("Iron Sword"));
		respawnables.put(w.getArea(new Point(10, 1)), items2);

		ArrayList<Item> items3 = new ArrayList<Item>();
		Item orbKey = new Item(
				"Glowing Blue Orb",
				2,
				"This glass orb has a bright blue aura about it, it is probably used for something");
		items3.add(orbKey);
		items3.add(ItemFactory.makeArmor("Iron Shield"));
		respawnables.put(w.getArea(new Point(9, 4)), items3);

		ArrayList<Item> items4 = new ArrayList<Item>();
		items4.add(ItemFactory.makeArmor("Leather Leggings"));
		items4.add(new SmallHealthPot());
		respawnables.put(w.getArea(new Point(9, 1)), items4);

		ArrayList<Item> items5 = new ArrayList<Item>();
		items5.add(ItemFactory.makeWeapon("Longbow"));
		respawnables.put(w.getArea(new Point(12, 1)), items5);

		ArrayList<Item> items6 = new ArrayList<Item>();
		items6.add(ItemFactory.makeWeapon("Dagger"));
		items6.add(new SmallHealthPot());
		respawnables.put(w.getArea(new Point(12, 0)), items6);

		ArrayList<Item> items7 = new ArrayList<Item>();
		items7.add(new Bomb());
		respawnables.put(w.getArea(new Point(12, 3)), items7);

		ArrayList<Item> items8 = new ArrayList<Item>();
		items8.add(ItemFactory.makeArmor("Leather Armor"));
		items8.add(ItemFactory.makeWeapon("Handaxe"));
		items8.add(new SmallHealthPot());
		items8.add(new SmallHealthPot());
		respawnables.put(w.getArea(new Point(10, 3)), items8);

		ArrayList<Item> items9 = new ArrayList<Item>();
		items9.add(ItemFactory.makeWeapon("Crossbow"));
		items9.add(new SmallHealthPot());
		items9.add(new SmallHealthPot());
		items9.add(new LargeHealthPot());
		respawnables.put(w.getArea(new Point(9, 2)), items9);

		ArrayList<Item> items10 = new ArrayList<Item>();
		items10.add(new Bomb());
		items10.add(new MediumHealthPot());
		respawnables.put(w.getArea(new Point(8, 3)), items10);

		ArrayList<Item> items11 = new ArrayList<Item>();
		items11.add(ItemFactory.makeArmor("Iron Helmet"));
		respawnables.put(w.getArea(new Point(10, 5)), items11);

		ArrayList<Item> items12 = new ArrayList<Item>();
		items12.add(new LargeHealthPot());
		items12.add(ItemFactory.makeWeapon("Claymore"));
		respawnables.put(w.getArea(new Point(15, 7)), items12);

		ArrayList<Item> items13 = new ArrayList<Item>();

		items13.add(ItemFactory.makeWeapon("Crossbow"));
		items13.add(ItemFactory.makeWeapon("Iron Sword"));
		items13.add(ItemFactory.makeWeapon("Iron Sword"));
		items13.add(new MediumHealthPot());
		items13.add(new MediumHealthPot());
		items13.add(new Bomb());
		items13.add(new Bomb());
		respawnables.put(w.getArea(new Point(12, 5)), items13);

		ArrayList<Item> items14 = new ArrayList<Item>();
		items14.add(ItemFactory.makeWeapon("Dagger"));
		items14.add(new MediumHealthPot());
		items14.add(new MediumHealthPot());
		respawnables.put(w.getArea(new Point(11, 7)), items14);

		ArrayList<Item> items15 = new ArrayList<Item>();
		items15.add(ItemFactory.makeArmor("Ancient Shield"));
		items15.add(new LargeHealthPot());
		respawnables.put(w.getArea(new Point(12, 7)), items15);

		ArrayList<Item> items16 = new ArrayList<Item>();

		items16.add(ItemFactory.makeArmor("Iron Leggings"));
		items16.add(new LargeHealthPot());
		items16.add(new LargeHealthPot());
		items16.add(new Item("Royal Key", 2,
				"A small key with a crown engraved on it."));
		respawnables.put(w.getArea(new Point(9, 8)), items16);

		ArrayList<Item> items17 = new ArrayList<Item>();
		items17.add(new LargeHealthPot());
		respawnables.put(w.getArea(new Point(14, 8)), items17);
	}

	private void respawn() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				ArrayList<Item> items = respawnables.get(w.getArea(new Point(i,
						j)));
				if (items != null) {
					for (int k = 0; k < items.size(); k++) {
						if (w.getArea(new Point(i, j)).hasItem(
								items.get(k).getName()) == null) {
							w.getArea(new Point(i, j)).addItem(items.get(k));
						}
					}
				}
			}
		}
	}

	/**
	 * Iterates through the list of mobs and starts their behavior threads
	 */
	public void start() {
		Iterator<Mob> itr = mobs.iterator();
		while (itr.hasNext()) {
			Mob moby = itr.next();
			if (!moby.isDead())
				moby.getGoing();
		}
		this.startRespawns();
	}

	/**
	 * This removes the passed mob from the mobs list. It also will spawn 3 baby
	 * spiders on top of a dead Giant Spider if it is the passed mob
	 * 
	 * @param m
	 *            - the dead mob
	 */
	public void mobDied(Mob m) {
		mobs.remove(m);
		if (m instanceof GiantSpider) {
			w.sendNotification(new Notification(
					w.getArea(m.getLocation()).getPlayers(),
					"The slain spider's abdomen burst's open releasing a horde of offspring!",
					false));
			for (int i = 0; i < 3; i++) {
				Runnable babySpider = new BabySpider(w, m.getLocation(), this);
				mobs.add((Mob) babySpider);
				((Mob) babySpider).getGoing();

			}
		}
	}

	/**
	 * This method starts the threads responsible for respawning mobs. Each
	 * thread will wait a certain amount of time and then count how many
	 * instances of each mob type are in the mobs list. If the number found is
	 * less than a certain value then it will respawn a new instance of that
	 * particular mob. Seperate threads are responsible for different tiers of
	 * mobs as weaker mobs should respawn more often then more powerful ones
	 */
	public void startRespawns() {
		// This thread respawns Giant Rats, Goblins, Goblin Shaman, Skeleton
		// Warriors, and Skeleton Archers
		Thread respawnWeakMobs = new Thread() {
			public void run() {
				try {
					while (true) {
						sleep(80000);
						System.out
								.println("Checking low level mob populations...");
						int rats = 0;
						int goblins = 0;
						int shaman = 0;
						int skellyWar = 0;
						int skellyArch = 0;
						// Counts the number of mobs remaining of each type
						for (int i = 0; i < mobs.size(); i++) {
							Mob m = mobs.get(i);
							if (m instanceof GiantRat) {
								rats++;
							} else if (m instanceof Goblin) {
								goblins++;
							} else if (m instanceof GoblinShaman) {
								shaman++;
							} else if (m instanceof SkeletonArcher) {
								skellyArch++;
							} else if (m instanceof SkeletonWarrior) {
								skellyWar++;
							}
						}
						Random rand = new Random();
						// Respawns Giant Rats
						if (rats < 6) {
							int randomInt = rand.nextInt(2);
							Point ratSpawn = new Point(12, 3);
							if (randomInt == 0) {
								ratSpawn = new Point(11, 1);
							}
							System.out.println("Spawning Rat at ("
									+ ratSpawn.getX() + "," + ratSpawn.getY()
									+ ")");
							Mob rat = new GiantRat(w, ratSpawn,
									GameSimulation.this);
							mobs.add(rat);
							rat.getGoing();
						}
						// Respawns Goblins
						if (goblins < 6) {
							int randomInt = rand.nextInt(3);
							Point gobSpawn = new Point(9, 2);
							if (randomInt == 0) {
								gobSpawn = new Point(10, 3);
							} else if (randomInt == 1) {
								gobSpawn = new Point(11, 3);
							}
							System.out.println("Spawning Goblin at ("
									+ gobSpawn.getX() + "," + gobSpawn.getY()
									+ ")");
							Mob gob = new Goblin(w, gobSpawn,
									GameSimulation.this);
							mobs.add(gob);
							gob.getGoing();
						}
						// Respawns Goblin SHaman
						if (shaman < 2) {
							int randomInt = rand.nextInt(2);
							Point shamSpawn = new Point(9, 2);
							if (randomInt == 0) {
								shamSpawn = new Point(10, 3);
							}
							System.out.println("Spawning Shaman at ("
									+ shamSpawn.getX() + "," + shamSpawn.getY()
									+ ")");
							Mob sham = new GoblinShaman(w, shamSpawn,
									GameSimulation.this);
							mobs.add(sham);
							sham.getGoing();
						}
						// Respawns Skeleton Archers
						if (skellyArch < 3) {
							int randomInt = rand.nextInt(2);
							Point archSpawn = new Point(9, 4);
							if (randomInt == 0) {
								archSpawn = new Point(12, 5);
							}
							Mob arch = new SkeletonArcher(w, archSpawn,
									GameSimulation.this);
							mobs.add(arch);
							arch.getGoing();
						}
						// Respawns Skeleton Warriors
						if (skellyWar < 3) {
							int randomInt = rand.nextInt(2);
							Point warSpawn = new Point(9, 4);
							if (randomInt == 0) {
								warSpawn = new Point(12, 5);
							}
							Mob war = new SkeletonWarrior(w, warSpawn,
									GameSimulation.this);
							mobs.add(war);
							war.getGoing();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		// This thread respawns the tougher mobs, Grues, Imps, Giant Spiders,
		// Trolls, and Dragons
		Thread respawnToughMobs = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(180000);
						int grues = 0;
						int giantSpiders = 0;
						int imps = 0;
						int trolls = 0;
						int dragons = 0;
						int babySpiders = 0;
						// Counts the remaining number of each mob type
						for (int i = 0; i < mobs.size(); i++) {
							Mob m = mobs.get(i);
							if (m instanceof Grue) {
								grues++;
							} else if (m instanceof GiantSpider) {
								giantSpiders++;
							} else if (m instanceof BabySpider) {
								babySpiders++;
							} else if (m instanceof Imp) {
								imps++;
							} else if (m instanceof Troll) {
								trolls++;
							} else if (m instanceof Dragon) {
								dragons++;
							}
						}
						// Respawns Grues
						if (grues < 2) {
							Point grueSpawn = new Point(8, 3);
							if (w.getArea(grueSpawn).hasCharacter("Grue") != null) {
								grueSpawn = new Point(10, 5);
							}
							if (w.getArea(grueSpawn).hasCharacter("Grue") == null) {
								System.out.println("Spawning Grue at ("
										+ grueSpawn.getX() + ","
										+ grueSpawn.getY() + ")");
								Mob grue = new Grue(w, grueSpawn,
										GameSimulation.this);
								mobs.add(grue);
								grue.getGoing();
							}
						}
						// Respawns Imps
						if (imps < 3) {
							Point impSpawn = new Point(9, 8);
							System.out.println("Spawning Imp at ("
									+ impSpawn.getX() + "," + impSpawn.getY()
									+ ")");
							Mob imp = new Imp(w, impSpawn, GameSimulation.this);
							mobs.add(imp);
							imp.getGoing();
						}
						// Respawns Giant Spiders
						if (babySpiders == 0 && giantSpiders == 0) {
							Point spiderSpawn = new Point(11, 6);
							System.out.println("Spawning Giant Spider at ("
									+ spiderSpawn.getX() + ","
									+ spiderSpawn.getY() + ")");
							Mob spider = new GiantSpider(w, spiderSpawn,
									GameSimulation.this);
							mobs.add(spider);
							spider.getGoing();
						}
						// Respawns Trolls
						if (trolls < 1) {
							Point trollSpawn = new Point(16, 8);
							System.out.println("Spawning Troll at ("
									+ trollSpawn.getX() + ","
									+ trollSpawn.getY() + ")");
							Mob troll = new Troll(w, trollSpawn,
									GameSimulation.this);
							mobs.add(troll);
							troll.getGoing();
						}
						// Respawns Dragons
						if (dragons < 1) {
							Point dragonSpawn = new Point(17, 8);
							System.out.println("Spawning Dragon at ("
									+ dragonSpawn.getX() + ","
									+ dragonSpawn.getY() + ")");
							Mob dragon = new Dragon(w, dragonSpawn,
									GameSimulation.this);
							mobs.add(dragon);
							dragon.getGoing();
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread respawnItems = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(240000);
					} catch (InterruptedException e) {
					}
					respawn();
					w.sendNotification(new Notification(
							w.getAllPlayers(),
							"There was a strange rumbling; it appears some items have replenished.",
							false));

				}
			}
		};
		Thread relock = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(240000);
					} catch (InterruptedException e) {
					}
					lockDoors();
					w.sendNotification(new Notification(
							w.getAllPlayers(),
							"You have heard a large rumbling sound; it appears that all doors have locked!",
							false));

				}
			}
		};
		// Starts all respawn threads
		respawnWeakMobs.start();
		respawnToughMobs.start();
		respawnItems.start();
		relock.start();
	}
}
