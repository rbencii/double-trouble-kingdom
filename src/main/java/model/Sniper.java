package model;

import java.awt.*;

public class Sniper extends Tower {
    public Sniper(Point position, String side) {
        super(position, side);
        this.canUpgrade = true;
        this.value = 30;
    }

    public void attack() {
    }
}
