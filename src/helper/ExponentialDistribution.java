package helper;

public class ExponentialDistribution {
    public double mean;
    public double number;
    public int x;

    public ExponentialDistribution(double mean, double number) {
        this.mean = mean;
        this.number = number;
        this.x = -1;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(int mean) {
        this.mean = mean;
    }

    public double getNext() {
        this.x += 1;
        return Math.log(1 - (((1 / this.number) * this.x)))/(1 / (-mean));
    }
}
