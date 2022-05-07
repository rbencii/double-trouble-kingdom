package DTK_model;

import com.sun.jdi.InvalidTypeException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public enum ObjectTypes {
    CASTLE("Castle", Color.LIGHT_GRAY),
    BARRACKS("Barracks",new Color(64, 37, 19)),
    BARRICADE("Barricade",new Color(208, 146, 110)),
    SHOTGUN("Shotgun",new Color(17, 15, 15)),
    SNIPER("Sniper",new Color(118, 110, 106)),
    SOLDIER("Soldier",new Color(190, 30, 30)),
    ASSASSIN("Assassin",new Color(45, 15, 80)),
    KAMIKAZE("Kamikaze",new Color(72, 18, 25)),
    DIVER("Diver",new Color(22, 107, 107)),
    CLIMBER("Climber",new Color(175, 112, 81)),
    PLAINS("Plains", Color.GREEN),
    DESERT("Desert",Color.YELLOW),
    SWAMP("Swamp",Color.BLUE),
    MOUNTAIN("Mountain",Color.DARK_GRAY),
    NONE("None"),
    NO_SELECTION("NoSelection"),
    DELETE("Delete" );

    public final String text;
    public final Color color;

    ObjectTypes(String text) {
        this.text = text;
        this.color = null;

    }
    ObjectTypes(String text, Color color) {
        this.text = text;
        this.color = color;
    }




    public static ArrayList<ObjectTypes> getSoldierTypes() {
        return new ArrayList<>(Arrays.asList(SOLDIER, ASSASSIN, KAMIKAZE, DIVER, CLIMBER));
    }

    public static ArrayList<ObjectTypes> getBuildingTypes() {
        return new ArrayList<>(Arrays.asList(CASTLE, BARRACKS, BARRICADE, SHOTGUN, SNIPER));
    }

    public static ArrayList<ObjectTypes> getTerrainTypes() {
        return new ArrayList<>(Arrays.asList(PLAINS, DESERT, SWAMP, MOUNTAIN));
    }

    public static ArrayList<ObjectTypes> getTowerTypes() {
               return new ArrayList<>(Arrays.asList(SHOTGUN, SNIPER, BARRICADE));

    }

    public static ArrayList<ObjectTypes> getUpgradeable() {
        return new ArrayList<>(Arrays.asList(SHOTGUN, SNIPER ));

    }

    public static Building buildingFactory(ObjectTypes objectType, int x, int y) {
         switch (objectType) {
             case CASTLE -> {
                 return new Castle(new Point(x, y), "");
             }
             case BARRACKS -> {
                 return new Barracks(new Point(x, y), "");
             }
             case BARRICADE -> {
                 return new Barricade(new Point(x, y), "");
             }
             case SHOTGUN -> {
                 return new Shotgun(new Point(x, y), "");
             }
             case SNIPER -> {
                 return new Sniper(new Point(x, y), "");
             }

         }
         return  null;
    }

    public static Terrain terrainFactory(ObjectTypes objectType, ArrayList<Entity> entities) {
         switch (objectType) {
            case PLAINS -> {return new Plains(entities);}
            case DESERT -> {return new Desert(entities);}
            case SWAMP -> {return new Swamp(entities);}
            case MOUNTAIN -> {return new Mountain(entities);}
        }
        return null;
    }

    public static Soldier soldierFactory(ObjectTypes objectType, int x, int y) {
         switch (objectType) {
            case SOLDIER -> {return new Soldier(new Point(x, y));}
            case ASSASSIN -> {return new Assassin(new Point(x, y));}
            case KAMIKAZE -> {return new Kamikaze(new Point(x, y));}
            case DIVER -> {return new Diver(new Point(x, y));}
            case CLIMBER ->{ return new Climber(new Point(x, y));}
        }
        return null;
    }





}