import java.time.LocalDate;
import java.util.Date;

public class Data {
    LocalDate date;
    int calConsumed;
    int totalCalBurnt;
    double weight;
    double sleep;

    public Data(LocalDate date, int calConsumed, double weight, double sleep) {
        this.date = date;
        this.calConsumed = calConsumed;
        this.weight = weight;
        this.sleep = sleep;
    }

    public Data(LocalDate date, int calConsumed, int totalCalBurnt, double weight, double sleep) {
        this.date = date;
        this.calConsumed = calConsumed;
        this.totalCalBurnt = totalCalBurnt;
        this.weight = weight;
        this.sleep = sleep;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCalConsumed(int calConsumed) {
        this.calConsumed = calConsumed;
    }

    public void setTotalCalBurnt(int totalCalBurnt) {
        this.totalCalBurnt = totalCalBurnt;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setSleep(double sleep) {
        this.sleep = sleep;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getCalConsumed() {
        return calConsumed;
    }
    public int getTotalCalBurnt() {
        return totalCalBurnt;
    }
    public double getWeight() {
        return weight;
    }
    public double getSleep() {
        return sleep;
    }
}
