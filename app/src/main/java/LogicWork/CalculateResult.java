package LogicWork;

public class CalculateResult {
    private static final double INCOME_PER_TH_BTC = 0.00000077;
    public static double profitabilityPerDay;
    private static double profitabilityPerWeek;
    private static double profitabilityPerMouth;
    public static double profitabilityPerDay(int hashRate, double btcToRub, double powerConsumptionW, double electricityCost) {
        // Затраты на электроэнергию за день
        double electricityCostPerDay = ((powerConsumptionW * 24) / 1000) * electricityCost;

        // Доход в BTC за день
        double incomeInBtcPerDay = hashRate * INCOME_PER_TH_BTC;

        // Перевод дохода в рубли
        double incomeInRubPerDay = incomeInBtcPerDay * btcToRub;
        profitabilityPerDay = incomeInRubPerDay - electricityCostPerDay;

        // Чистая прибыль за день
        return profitabilityPerDay;
    }

    public static double profitabilityPerWeek(int hashRate, double btcToRub, double powerConsumptionW, double electricityCost) {
        return profitabilityPerWeek = profitabilityPerDay(hashRate, btcToRub, powerConsumptionW, electricityCost) * 7;
    }

    public static double profitabilityPerMouth(int hashRate, double btcToRub, double powerConsumptionW, double electricityCost) {
        return profitabilityPerMouth = profitabilityPerDay(hashRate, btcToRub, powerConsumptionW, electricityCost) * 30.5;
    }

    public static String profitabilityPerDayToString() {
        return String.valueOf(profitabilityPerDay);
    }

    public static String profitabilityPerWeekToString() {
        return String.valueOf(profitabilityPerWeek);
    }

    public static String profitabilityPerMouthToString() {
        return String.valueOf(profitabilityPerMouth);
    }
}
