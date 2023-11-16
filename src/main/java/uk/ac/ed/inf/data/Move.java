package uk.ac.ed.inf.data;

public class Move {
    private String orderNo = "";
    private double currLong;
    private double currLat;
    private double angle;
    private double nextLong;
    private double nextLat;

    double total, g, h; //Total, cost estimate and heuristic cost estimate.

    Move parent; //The position we came from


    public Move(String orderNo, double startLong, double startLat) {
        this.setOrderNo(orderNo);
        this.setCurrLong(startLong);
        this.setCurrLat(startLat);
        this.setParent(null);
        this.total = 0;
        this.g = 0;
        this.h = 0;
    }

    public Move() {

    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getCurrLong() {
        return this.currLong;
    }

    public void setCurrLong(double currLong) {
        this.currLong = currLong;
    }

    public double getCurrLat() {
        return this.currLat;
    }

    public void setCurrLat(double currLat) {
        this.currLat = currLat;
    }

    public double getAngle()
    {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getNextLong() {
        return this.nextLong;
    }

    public void setNextLong(double nextLong) {
        this.nextLong = nextLong;
    }

    public double getNextLat() {
        return this.nextLat;
    }

    public void setNextLat(double nextLat) {
        this.nextLat = nextLat;
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
}
