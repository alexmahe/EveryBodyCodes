package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.alexmahe.common.Utils.REGEX_NEW_LINE;
import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest07 {

    static void main() throws IOException {
        var today = new Quest07();

        // Part 01
        var input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest07", "input01"));
        var names = Arrays.stream(input.getFirst().split(",")).toList();
        var rules = today.parseRules(input.getLast());

        String correctName = null;
        for (String name : names) {
            if (today.doesNameRespectRules(name, rules)) {
                correctName = name;
                break;
            }
        }
        log.info("The name respecting the rules is {}", correctName);

        // Part 02
        input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest07", "input02"));
        names = Arrays.stream(input.getFirst().split(",")).toList();
        rules = today.parseRules(input.getLast());
        var sumOfIndices = 0;
        for (int nameIndex = 0; nameIndex < names.size(); nameIndex++) {
            if (!today.doesNameRespectRules(names.get(nameIndex), rules)) continue;
            sumOfIndices += nameIndex + 1;
        }
        log.info("Sum of indices of correct names: {}", sumOfIndices);

        // Part 03
        input = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("session2025", "Quest07", "input03"));
        names = Arrays.stream(input.getFirst().split(",")).toList();
        var rules03 = today.parseRules(input.getLast());
        var possibleNames = names.stream()
                .filter(name -> today.doesNameRespectRules(name, rules03))
                .map(name -> today.walkThroughRuleToFindNumberSuitableNameStartingWith(name, rules03, 7, 11))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        log.info("Number of possible correct names: {}", possibleNames.size());
    }

    private HashSet<String> walkThroughRuleToFindNumberSuitableNameStartingWith(String prefix, Map<String, String> rules, int minLength, int maxLength) {
        if (prefix.length() == maxLength) return new HashSet<>(List.of(prefix));

        var subPossibilities = new HashSet<String>();
        if (prefix.length() >= minLength) subPossibilities.add(prefix);

        var nextPossibleLetters = rules.get(String.valueOf(prefix.charAt(prefix.length() - 1)));
        if (nextPossibleLetters != null) {
            var next = Arrays.stream(nextPossibleLetters.split(""))
                    .map(letter -> {
                        var newName = prefix + letter;
                        return walkThroughRuleToFindNumberSuitableNameStartingWith(newName, rules, minLength, maxLength);
                    })
                    .flatMap(Collection::stream)
                    .collect(HashSet<String>::new, HashSet::add, HashSet::addAll);
            subPossibilities.addAll(next);
        }

        return subPossibilities;
    }

    private boolean doesNameRespectRules(String name, Map<String, String> rules) {
        for (int letterIndex = 0;  letterIndex < name.length() - 1; letterIndex++ ) {
            var ruleToCheck = rules.get(String.valueOf(name.charAt(letterIndex)));
            if (ruleToCheck == null || !ruleToCheck.contains(String.valueOf(name.charAt(letterIndex + 1)))) {
                return false;
            }
        }

        return true;
    }

    private Map<String, String> parseRules(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE))
                .map(rule -> rule.split(" > "))
                .collect(Collectors.toMap(rule -> rule[0], rule -> rule[1].replace(",", "")));
    }

}
