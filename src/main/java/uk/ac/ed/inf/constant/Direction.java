package uk.ac.ed.inf.constant;

public class Direction {
    public static final double NORTH = 90;
    public static final double SOUTH = 270;
    public static final double WEST = 180;
    public static final double EAST = 0;

    public static final double NE = 45;
    public static final double ENE = 27.5;
    public static final double NNE = 67.5;
    public static final double NNW = 112.5;

    public static final double NW = 135;
    public static final double WNW = 147.5;
    public static final double WSW = 202.5;
    public static final double SW = 215;

    public static final double SSW = 237.5;
    public static final double SSE = 292.5;
    public static final double SE = 315;
    public static final double ESE = 337.5;

    /**
     * Gets all the direction angles
     * @return An array of all the direction angles
     */
    public static double[] getAllDirections() {
        return new double[]{EAST, ENE, NE, NNE, NORTH, NNW, NW, WNW, WEST, WSW, SW, SSW, SOUTH, SSE, SE, ESE};
    }

}
