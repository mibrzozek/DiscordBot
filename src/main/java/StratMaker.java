/*
    Strat Maker
    Author :  Michal Brzozek

    Purpose : Provides random strategies based on map
    for pubg to make a more exciting and challenging match.

    Note about drop zone coordinates :

    The x axis goes from 0 to alphabetXxX.length - 1
    The y axis goes from alphabetXxX.length/2 to alphabet.length

    The first half of an alphabet list stores the x coordinates
    while the second stores the y coordinates
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class StratMaker
{

    private static int map = 0;
    private static Random random;

    private static String[] alphabet8x8 = { "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
    private static String[] alphabet6x6 = { "A","B","C","D","E","F","I","J","K","L","M","N"};
    private static String[] alphabet4x4 = { "A","B","C","D","I","J","K","L"};

    private static ArrayList<Point> invalidDropErangel = initializeInvalidDropsE();
    private static ArrayList<Point> invalidDropMiramar = initializeInvalidDropsM();
    private static ArrayList<Point> invalidDropSanhok = initializeInvalidDropsS();
    private static ArrayList<Point> invalidDropVikendi = initializeInvalidDropsV();

    private static ArrayList[] mapSelector = { initializeInvalidDropsE(), initializeInvalidDropsM(),
            initializeInvalidDropsS(), initializeInvalidDropsV()};

    private static  ArrayList<String> specialRules = initializeSpecialRules();
    private static  ArrayList<String> weaponRules = initializeWeaponRules();
    private static  ArrayList<String> medicalRules = initializeMedicalRules();
    private static  ArrayList<String> armorRules = initializeArmorRules();
    private static  ArrayList<String> vehicleRules = initializeVehicleRules();

    StratMaker()
    {


        map = 0;
        random = new Random();
    }
    public static String getSpecialRule()
    {
        return specialRules.get(random.nextInt(specialRules.size() + 1));
    }

    public String getErangelSpot()
    {
        map = 0;
        Point p = getValidPoint();
        String coordinates = "["+ alphabet8x8[p.x] + "]" + "["+ alphabet8x8[p.y] + "]" ;

        return coordinates;
    }

    public String getMiramarSpot()
    {
        map = 1;
        Point p = getValidPoint();
        String coordinates = "["+ alphabet8x8[p.x] + "]" + "["+ alphabet8x8[p.y] + "]" ;

        return coordinates;
    }
    public String getSanhokSpot()
    {
        map = 2;
        Point p = getValidPoint();
        String coordinates = "["+ alphabet4x4[p.x] + "]" + "["+ alphabet4x4[p.y] + "]";

        return coordinates;
    }
    public String getVikendiSpot()
    {
        map = 3;
        Point p = getValidPoint();
        String coordinates = "["+ alphabet6x6[p.x] + "]" + "["+ alphabet6x6[p.y] + "]" ;

        return coordinates;
    }

    public static Point getValidPoint()
    {
        Point p = new Point(1, 1);
        do
        {
            if(map == 0 || map == 1)
                p = new Point(random.nextInt((alphabet8x8.length/2)) , random.nextInt((7 + 1)) + 8);
            else if(map == 2)
                p = new Point(random.nextInt((alphabet4x4.length/2)) , random.nextInt((3 + 1)) + alphabet4x4.length/2);
            else if(map == 3)
                p = new Point(random.nextInt((alphabet6x6.length/2)) , random.nextInt(5 + 1) + alphabet6x6.length/2);
        }   while(!valid(p));

        return p;
    }
    public static boolean valid(Point p)
    {
        boolean truth = true;

        for (int i = 0; i < mapSelector[map].size(); i++)
        {
            if (p.equals((mapSelector[map].get(i))))
                truth = false;
        }
        return truth;
    }
    public static ArrayList<Point> initializeInvalidDropsM()
    {
        ArrayList<Point> invalid = new ArrayList();
        invalid.add(new Point(2, 8));
        invalid.add(new Point(0, 12));
        invalid.add(new Point(0, 13));
        invalid.add(new Point(7, 14));
        invalid.add(new Point(4, 15));
        invalid.add(new Point(5, 15));
        invalid.add(new Point(6, 15));
        invalid.add(new Point(7, 15));

        return invalid;
    }
    public static ArrayList<Point> initializeInvalidDropsE()
    {
        ArrayList<Point> invalid = new ArrayList();
        invalid.add(new Point(0, 8));
        invalid.add(new Point(1, 8));
        invalid.add(new Point(2, 8));
        invalid.add(new Point(3, 8));
        invalid.add(new Point(7, 8));
        invalid.add(new Point(7, 9));
        invalid.add(new Point(7, 10));
        invalid.add(new Point(7, 11));
        invalid.add(new Point(7, 13));
        invalid.add(new Point(7, 14));
        invalid.add(new Point(7, 15));
        invalid.add(new Point(0, 10));
        invalid.add(new Point(0, 11));
        invalid.add(new Point(0, 14));
        invalid.add(new Point(0, 15));
        invalid.add(new Point(1, 15));
        invalid.add(new Point(2, 14));
        invalid.add(new Point(2, 15));
        invalid.add(new Point(5, 15));
        invalid.add(new Point(6, 15));

        return invalid;
    }
    private static ArrayList<Point> initializeInvalidDropsS()
    {
        ArrayList<Point> invalid = new ArrayList();

        invalid.add(new Point(0, 4));
        invalid.add(new Point(0, 7));

        return invalid;
    }
    private static ArrayList<Point> initializeInvalidDropsV()
    {
        ArrayList<Point> invalid = new ArrayList();

        invalid.add(new Point(0, 6));
        invalid.add(new Point(1, 6));
        invalid.add(new Point(2, 6));
        invalid.add(new Point(3, 6));
        invalid.add(new Point(4, 6));
        invalid.add(new Point(5, 6));

        invalid.add(new Point(0, 6));
        invalid.add(new Point(0, 7));
        invalid.add(new Point(0, 8));
        invalid.add(new Point(0, 9));
        invalid.add(new Point(0, 10));

        invalid.add(new Point(5, 6));
        invalid.add(new Point(5, 7));
        invalid.add(new Point(5, 8));
        invalid.add(new Point(5, 9));
        invalid.add(new Point(5, 10));

        invalid.add(new Point(0, 11));
        invalid.add(new Point(1, 11));
        invalid.add(new Point(2, 11));
        invalid.add(new Point(3, 11));
        invalid.add(new Point(4, 11));
        invalid.add(new Point(5, 11));

        return invalid;
    }


    public static String getWeaponRule()
    {
        return "";
    }
    public static String getMedicalRule()
    {
        return "";
    }
    public static String getVehicleRule()
    {
        return "";
    }
    public static String getArmorRule()
    {
        return "";
    }
    public static ArrayList<String> initializeSpecialRules()
    {
        ArrayList<String> list = new ArrayList();

        list.add("Lucky Duck : No special rules");
        list.add("Chatty Kathy : You must use all voice chat and must tell players you will kill them before you do");
        list.add("Respect the Dead : No looting bodies");
        list.add("Bird Watcher : You must go to every airdrop you see falling");
        list.add("Grounded : You can only loot the bottom story/floor of any building/house/structure");
        list.add("Fuckboy : You can only loot fuckboy shacks");
        list.add("Vehicular Manslaughter : If you are in a vehicle you must attempt run over any player you see");
        list.add("Slim Pickings : You can only loot airdrops, get a vehicle and find that drop");
        list.add("WWII : Any gun found in WWII flies. No attachments");
        list.add("Gephyrophobia : You are deathly afraid of bridges, you must swim or use boats every time you encounter a bridge");
        list.add("Viking Funeral : If you have a Molotov you must throw it on an enemies body after killing them in order to send them to Valhalla");
        list.add("Spread Out : If in a group, all party members must pick separate locations to drop and then meet up");
        list.add("Carpooling Sucks : If in a group, everyone must drive their own vehicle");
        list.add("xxXMontageXxx : If you have a scoped weapon, you must do a 360 before you can shoot.");
        list.add("Punch-squad : Drop where the most people drop and try to get as many punch kills as possible before the first circle\n");
        list.add("Silence is Golden : No speaking to teammates only can communicate through in game movements");
        list.add("Hitman : Silenced pistol only, any pistol attachment. No armor/helmet");
        list.add("Farmer : Jump from plane with the AFK'ers\n");
        list.add("The Huntsman : Crossbow, shotguns, kar98k, revolver, m24, machete, sickle only");
        list.add("Crossdresser : You must switch clothes with every person you kill.");
        list.add("Medic : If in a squad you must have a designated medic who can only carry medical supplies, they are not allowed to harm others");
        list.add("Vape Nation : You must throw a smoke grenade every time you see an enemy.");
        list.add("LEYROY JENKINS : That is all.");
        list.add("Biker gang : Everyone in your squad must get their own bike and drive arounnd in the circle until they run out of gas");
        list.add("This is my Rifle : The first gun you find, whether it be a pistol, shotgun, crossbow, etc, is the only weapon you can use for the game.");
        list.add("WOKE AF : The 8x and AR silencer only allowed on S12K.");
        list.add("Loot Better : After every Kill you get, go to all chat and say 'Loot Better'");
        list.add("Wadu Hek : You can only communicate to your team using 'Wadu Hek'. Get creative.");
        list.add("Hitman 2 : You can't use your weapons unless they're silenced.");
        list.add("Cinematic Mode : Turn off HUD. Ctrl + U. ");
        list.add("It's a Bingo! : During pregame lobby everyone drops a marker on the white square where they think final circle will land.");
        list.add("Arrrr : A pirates life is a life like no other. Drop at a seaside town, loot up and stay in the boat as long as the circle lets you");


        return list;
    }
    public static ArrayList<String> initializeWeaponRules()
    {
        ArrayList<String> list = new ArrayList();

        return list;
    }
    public static ArrayList<String> initializeMedicalRules()
    {
        ArrayList<String> list = new ArrayList();

        return list;
    }
    public static ArrayList<String> initializeArmorRules()
    {
        ArrayList<String> list = new ArrayList();

        return list;
    }
    public static ArrayList<String> initializeVehicleRules()
    {
        ArrayList<String> list = new ArrayList();

        return list;
    }
}
