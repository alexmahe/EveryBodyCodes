package fr.alexmahe.common.session2024;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

@Slf4j
public class Quest2 {

    private static final String BEGINNING = "WORDS:";

    public static void main(String[] args) throws IOException {
        // Part 1
        var instructions = Utils.readInputSplitOnEmptyLines("src/main/resources/session2024/Quest2/Part1");
        var runicWords = instructions.get(0).substring(BEGINNING.length());
        var phrase = instructions.get(1);

        var runicWordsPattern = Pattern.compile("(?=%s)".formatted(runicWords.replaceAll(",", "|")));
        var results = runicWordsPattern.matcher(phrase).results().count();
        log.info("Part 1\nNumber of runic words : {}", results);

        // Part 2
        instructions = Utils.readInputSplitOnNewLines("src/main/resources/session2024/Quest2/Part2");
        var runicWordsAndReverse = Arrays.stream(instructions.get(0).substring(BEGINNING.length()).split(","))
                .map(str -> Arrays.asList(str, new StringBuilder(str).reverse().toString()))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        var phrases = instructions.stream()
                .skip(2)
                .toList();

        runicWordsPattern = Pattern.compile("(?=%s)".formatted(String.join("|", runicWordsAndReverse)));
        Pattern finalRunicWordsPattern = runicWordsPattern;
        results = phrases.stream()
                .map(line -> finalRunicWordsPattern.matcher(line).results().count())
                .reduce(0L, Long::sum);
        log.info("Part 2\nNumber of runic words : {}", results);
    }

}
