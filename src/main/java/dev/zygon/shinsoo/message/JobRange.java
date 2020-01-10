package dev.zygon.shinsoo.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum JobRange {

    BEGINNER(0, 99),
    WARRIOR(100, 199),
    MAGICIAN(200, 299),
    BOWMAN(300, 399),
    THIEF(400, 499),
    PIRATE(500, 599),

    NOBLESSE(1000, 1099),
    DAWN_WARRIOR(1100, 1199),
    BLAZE_WIZARD(1200, 1299),
    WIND_ARCHER(1300, 1399),
    NIGHT_WALKER(1400, 1499),
    THUNDER_BREAKER(1500, 1599),

    LEGEND(2000, 2099),
    ARAN(2100, 2199),
    EVAN(2200, 2299),
    MERCEDES(2300, 2399),
    PHANTOM(2400, 2499),
    LUMINOUS(2700, 2799),

    CITIZEN(3000, 3099),
    DEMON(3100, 3199),
    BATTLE_MAGE(3200, 3299),
    WILD_HUNTER(3300, 3399),
    MECHANIC(3500, 3599),
    XENON(3600, 3699),

    HEIZAN(4000, 4099),
    HAYATO(4100, 4199),
    KANNA(4200, 4299),

    MIHILE(5000, 5199),

    KAISER(6100, 6199),
    ANGELIC_BUSTER(6500, 6599),

    ZERO(10000, 10199),

    UNKNOWN(-1, -1)
    ;

    private long start;
    private long end;

    private String toRoute() {
        return name()
                .toLowerCase()
                .replaceAll("_", "-");
    }

    public static JobRange fromName(String name) {
        return Arrays.stream(values())
                .filter(range -> range.toRoute().equals(name))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
