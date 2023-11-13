package uk.ac.ed.inf.data;

public class Move {
    private String orderNo = "";
    private double fromLong;
    private double fromLat;
    private double angle;
    private double toLong;
    private double toLat;


    public Move(String orderNo, double startLong, double startLat, double angle) {
        this.setOrderNo(orderNo);
        this.setFromLong(startLong);
        this.setFromLat(startLat);
        this.setAngle(angle);
    }

    public Move() {

    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getFromLong() {
        return this.fromLong;
    }

    public void setFromLong(double fromLong) {
        this.fromLong = fromLong;
    }

    public double getFromLat() {
        return this.fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public double getAngle()
    {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getToLong() {
        return this.toLong;
    }

    public void setToLong(double toLong) {
        this.toLong = toLong;
    }

    public double getToLat() {
        return this.toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }


}
