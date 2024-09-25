package Storage;

public class Miner {
    private String name;
    private double hashrate;
    private double powerConsumption;
    private String documentId;

    public Miner() {
        // Пустой конструктор для Firebase
    }

    public Miner(String name, double hashrate, double powerConsumption) {
        this.name = name;
        this.hashrate = hashrate;
        this.powerConsumption = powerConsumption;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getHashrate() { return hashrate; }
    public void setHashrate(double hashrate) { this.hashrate = hashrate; }

    public double getPowerConsumption() { return powerConsumption; }
    public void setPowerConsumption(double powerConsumption) { this.powerConsumption = powerConsumption; }
    public String getDocumentId() { return documentId; } // Добавлено
    public void setDocumentId(String documentId) { this.documentId = documentId; } // Добавлено
}
