package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator extends SawToothGenerator {
    private int initialPeriod;
    private int currentPeriod;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double acceleration) {
        super(period);
        this.initialPeriod = period;
        this.currentPeriod = period;
        this.state = 0;
        this.factor = acceleration;
    }

    @Override
    public double next() {
        state = (state + 1);
        double value = 2 * (state / (double) currentPeriod - 0.5);
        if (state == currentPeriod) {
            state = 0;
            currentPeriod = (int) (currentPeriod * factor);
        }
        return value;
    }
}
