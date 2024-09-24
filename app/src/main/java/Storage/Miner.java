package Storage;

public class Miner {
    private String name;
    private double hashrate; // В TH
    private double powerConsumption; // В Вт

    public Miner(String name, double hashrate, double powerConsumption) {
        this.name = name;
        this.hashrate = hashrate;
        this.powerConsumption = powerConsumption;
    }

    public String getName() {
        return name;
    }

    public double getHashrate() {
        return hashrate;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }
}
