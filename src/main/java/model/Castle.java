package model;

import javax.naming.OperationNotSupportedException;
import java.awt.*;
import java.io.Serializable;

/**
 * Implementation of Castle Building type.
 */
public class Castle extends Building implements Serializable {

    /**
     * Constructs a new Castle instance.
     *
     * @param position the castle's position on the Map
     * @param side     the side it belongs to
     */
    public Castle(Point position, String side) {
        super(position, side);
        this.type = "Castle";
        this.isAnimated = false;
        this.healthPoints = 100;
        this.size = new Dimension(5, 15);
        //this.side = side;
    }

    /**
     * Upgrades the Building instance, Castles cannot be upgraded
     *
     * @return the cost of the upgrade
     */
    public int upgrade() {
        try {
            throw new OperationNotSupportedException();
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
