package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest04 {

    public static void main() throws IOException {
        var today = new Quest04();
        // PART 01
        var mult = today.getMultFromInput("input01");
        var firstGearTurn = 2025;
        log.info("Last gear turn after {} turn of first gear : {}", firstGearTurn, mult * firstGearTurn);

        // PART 02
        var lastGearTurnWanted = 10_000_000_000_000d;
        mult = today.getMultFromInput("input02");
        var result = (long) (lastGearTurnWanted / mult);
        log.info("First gear turn needed for 10 billion turn [long : {}, double : {}]", result, lastGearTurnWanted / mult);

        // PART 03
        mult = today.getMultFromInput("input03");
        firstGearTurn = 100;
        var lastGearTurn = (long) (mult * firstGearTurn);
        log.info("Last gear turn after {} turn of first gear [long : {}, double : {}]", firstGearTurn, lastGearTurn, mult * firstGearTurn);
    }

    private double getMultFromInput(String input) throws IOException {
        var gears = Utils.readInputSplitOnNewLines(RESOURCES.formatted("session2025", "Quest04", input))
                .stream()
                .map(line -> Arrays.stream(line.split("\\|")).map(Double::parseDouble).toList())
                .toList();
        var mult = 1d;

        for (var gear = 1; gear < gears.size(); gear++) {
            var previousGear = gears.get(gear - 1);
            var currentGear = gears.get(gear);
            mult *= previousGear.get(previousGear.size() - 1) / currentGear.get(0);
        }

        return mult;
    }

}
