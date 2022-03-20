package model;

import java.awt.*;

/**
 * Implementation of Kamikaze Soldier type.
 */
public class Kamikaze extends Soldier {

    /**
     * The percentage of attacking a nearby Tower.
     */
    private int splashPercent;

    /**
     * Constructs a new Kamikaze instance
     *
     * @param position the kamikaze's position on the Map
     * @param speed    the kamikaze's current speed
     */
    public Kamikaze(Point position, int speed) {
        super(position, speed);
        this.type = "Kamikaze";
        this.isAnimated = true;
        this.healthPoints = 10;
        this.size = new Dimension(1, 1);
        this.value = 5;
    }

    /**
     * Attacks the unit's target if nearby.
     */
    @Override
    public void attack() {
        super.attack();
    }
}
