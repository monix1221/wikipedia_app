package zad1.weather;

public class WindInfo {
    private double speed;
    private int deg;

    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    @Override
    public String toString() {
        return "Wind speed: " + this.speed + ", deg: " + this.deg;
    }
}
