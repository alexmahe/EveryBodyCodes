package fr.alexmahe.common.session2024;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
public class Quest1 {

    private final Map<String, Integer> mapOfCreatureValues = Map.of(
            "A", 0,
            "B", 1,
            "C", 3,
            "D", 5,
            "x", 0
    );

    public static void main(String[] args) throws IOException {
        var quest1 = new Quest1();
        // Part 1
        var creatures = Utils.readInputJoinOnNewLines("src/main/resources/session2024/Quest1/Part1");
        var nbPot1 = quest1.categorizeGroupOfNCreatures(creatures, 1).entrySet().stream()
                .map(entry -> entry.getValue() * quest1.calcCostWithMalus(entry.getKey()))
                .reduce(0, Integer::sum);
        log.info("Part 1\nNumber of potions to use : {}", nbPot1);

        // Part 2
        creatures = Utils.readInputJoinOnNewLines("src/main/resources/session2024/Quest1/Part2");
        var nbPot2 = quest1.categorizeGroupOfNCreatures(creatures, 2).entrySet().stream()
                .map(entry -> entry.getValue() * quest1.calcCostWithMalus(entry.getKey()))
                .reduce(0, Integer::sum);
        log.info("Part 2\nNumber of potions to use : {}", nbPot2);

        // Part 3
        creatures = Utils.readInputJoinOnNewLines("src/main/resources/session2024/Quest1/Part3");
        var nbPot3 = quest1.categorizeGroupOfNCreatures(creatures, 3).entrySet().stream()
                .map(entry -> entry.getValue() * quest1.calcCostWithMalus(entry.getKey()))
                .reduce(0, Integer::sum);
        log.info("Part 3\nNumber of potions to use : {}", nbPot3);
    }

    private Map<String, Integer> categorizeGroupOfNCreatures(String creatures, int groupSize) {
        var map = new HashMap<String, Integer>();
        var pattern = Pattern.compile(".{1,%s}".formatted(groupSize));
        pattern.matcher(creatures).results()
                .map(MatchResult::group)
                .forEach(group -> map.compute(group, (k, v) -> v != null ? v + 1 : 1));

        return map;
    }

    private int calcCostWithMalus(String creatures) {
        var nbOfX = Pattern.compile("x").matcher(creatures).results().toList().size();
        var malusValue = (creatures.length() - nbOfX) * (creatures.length() - nbOfX - 1);
        var value = Arrays.stream(creatures.split(""))
                .map(mapOfCreatureValues::get)
                .reduce(0, Integer::sum);

        return malusValue + value;
    }
}
