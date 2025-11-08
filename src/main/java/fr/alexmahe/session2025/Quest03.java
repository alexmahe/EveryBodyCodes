package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest03 {

    public static void main() throws IOException {
        // PART 01
        var input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest03", "input01")).get(0);
        var cratesStream = Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt);
        var biggestChain = cratesStream.distinct()
                .sum();
        log.info("Biggest Chain part 1: {}", biggestChain);

        // PART 02
        input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest03", "input02")).get(0);
        cratesStream = Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt);
        var wantedChainSum = cratesStream.distinct()
                .sorted()
                .limit(20)
                .sum();
        log.info("Wanted Chain part 2: {}", wantedChainSum);

        // PART 03
        input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest03", "input03")).get(0);
        ArrayList<Integer> cratesList = Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        var mapOfCrateOccurrences = new HashMap<Integer,Integer>();
        cratesList.forEach(crate -> {
            mapOfCrateOccurrences.compute(crate, (crateInMap, nbOccurrence) -> nbOccurrence != null ? nbOccurrence + 1 : 1);
        });
        var minNbOfSets = mapOfCrateOccurrences.values().stream()
                .mapToInt(integer -> integer)
                .max().orElseThrow(RuntimeException::new);
        log.info("Number of sets needed : {}", minNbOfSets);
    }

}
