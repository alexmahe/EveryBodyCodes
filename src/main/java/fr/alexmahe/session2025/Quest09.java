package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest09 {

    static void main() throws IOException {
        var today = new Quest09();

        // Part 01
        var input = Utils.readInputSplitOnNewLines(RESOURCES.formatted("session2025", "Quest09", "input01")).stream()
                .map(line -> line.split(":")[1])
                .toList();
        input = today.findChild(input);
        var child = input.get(0);
        var parent1 = input.get(1);
        var parent2 = input.get(2);

        var sim1 = today.countSimilarChar(child, parent1);
        var sim2 = today.countSimilarChar(child, parent2);
        log.info("similarity 1: {}, 2: {}", sim1, sim2);
        log.info("Result part 01: {}", sim1 * sim2);
    }

    private List<String> findChild(List<String> dnas) {
        var parentSet = new HashSet<String>();
        var childSet = new HashSet<>(dnas);

        for (int charIndex = 0; charIndex < dnas.getFirst().length(); charIndex++) {
            var isFirstSecondEquals = dnas.getFirst().charAt(charIndex) == dnas.get(1).charAt(charIndex);
            var isFirstLastEquals = dnas.getFirst().charAt(charIndex) == dnas.getLast().charAt(charIndex);

            if (isFirstSecondEquals && isFirstLastEquals) {
                continue;
            }

            if (!isFirstSecondEquals && !isFirstLastEquals) {
                parentSet.add(dnas.getFirst());
                childSet.remove(dnas.getFirst());
            } else if (!isFirstLastEquals) {
                parentSet.add(dnas.getLast());
                childSet.remove(dnas.getLast());
            } else {
                parentSet.add(dnas.get(1));
                childSet.remove(dnas.get(1));
            }

            if (parentSet.size() == 2) {
                var returnList = new ArrayList<>(childSet);
                Collections.addAll(returnList, parentSet.toArray(new String[0]));
                return returnList;
            }
        }

        throw new UnsupportedOperationException("Data problem");
    }

    private Integer countSimilarChar(String str, String other) {
        var counter = 0;

        for (int charIndex = 0; charIndex < str.length(); charIndex++) {
            if (str.charAt(charIndex) == other.charAt(charIndex)) {
                counter ++;
            }
        }

        return counter;
    }

}
