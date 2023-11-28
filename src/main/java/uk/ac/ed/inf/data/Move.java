package uk.ac.ed.inf.data;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Move implements Comparable<Move> {
    private String orderNo = "";
    private LngLat currLngLat;
    private double angle;
    private LngLat nextLngLat;

    double total, g, h; //Total, cost estimate and heuristic cost estimate.

    Move parent; //The position we came from


    public Move(String orderNo, LngLat position) {
        this.setOrderNo(orderNo);
        this.setCurrLngLat(position);
        this.setParent(null);
        this.total = 0;
        this.g = 0;
        this.h = 0;
    }


    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public LngLat getCurrLngLat() {
        return this.currLngLat;
    }

    public void setCurrLngLat(LngLat currLngLat) {
        this.currLngLat = currLngLat;
    }

    public double getAngle()
    {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public LngLat getNextLngLat() {
        return this.nextLngLat;
    }

    public void setNextLngLat(LngLat nextLong) {
        this.nextLngLat = nextLngLat;
    }

    public Move parent() {
        return this.parent;
    }

    public void setParent(Move parent) {
        this.parent = parent;
    }

    public double getG() {
        return this.g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(Move other) {
        return Double.compare(this.getTotal(), other.getTotal());
    }
}
