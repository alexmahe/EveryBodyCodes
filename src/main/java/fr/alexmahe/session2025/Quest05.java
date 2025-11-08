package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest05 {

    public static void main() throws IOException {
        // Part 01
        var swordList01 = Utils.readInputSplitOnNewLines(RESOURCES.formatted("session2025", "Quest05", "input01")).stream()
                .map(line -> line.split(":"))
                .map(Sword::new)
                .toList();
        log.info("Quality: {}", swordList01.getFirst().quality());

        // Part 02
        var swordList02 = Utils.readInputSplitOnNewLines(RESOURCES.formatted("session2025", "Quest05", "input02")).stream()
                .map(line -> line.split(":"))
                .map(Sword::new)
                .toList();
        var qualities = swordList02.stream()
                .map(Sword::quality)
                .sorted()
                .toList();
        var max = qualities.getLast();
        var min = qualities.getFirst();
        log.info("Qualtity max: {}, min: {}, diff: {}", max, min, max - min);

        // Part 03
        var input03 = Utils.readInputSplitOnNewLines(RESOURCES.formatted("session2025", "Quest05", "input03")).stream()
                .map(line -> line.split(":"))
                .map(Sword::new)
                .sorted(Comparator.reverseOrder())
                .map(Sword::id)
                .toList();
        var checksum = 0L;
        for (int index = 0; index < input03.size(); index++) {
            checksum += (index + 1) * input03.get(index);
        }
        log.info("Checksum: {}", checksum);
    }

    private record Sword(
            Long id,
            List<SpineSegment> spine,
            Long quality
    ) implements Comparable<Sword> {

        public Sword(String[] swordValues) {
            var id =  Long.parseLong(swordValues[0]);
            var spineValues = Arrays.stream(swordValues[1].split(",")).map(Long::parseLong).toList();
            this(id, spineValues);
        }

        public Sword(Long id, List<Long> spineValues) {
            var spine = buildSpine(spineValues);
            var quality = calcQuality(spine);
            this(id, spine, Long.valueOf(quality));
        }

        private static ArrayList<SpineSegment> buildSpine(List<Long> input) {
            var spine = new ArrayList<SpineSegment>();

            input.forEach(value -> {
                var added = false;
                for (var spineSegment : spine) {
                    if (value < spineSegment.getSpine() && spineSegment.getLeft() == null) {
                        spineSegment.setLeft(value);
                        added = true;
                        break;
                    } else if (value > spineSegment.getSpine() && spineSegment.getRight() == null) {
                        spineSegment.setRight(value);
                        added = true;
                        break;
                    }
                }

                if (!added) spine.add(new SpineSegment(value));
            });

            return spine;
        }

        private static String calcQuality(ArrayList<SpineSegment> spine) {
            return spine.stream()
                    .map(SpineSegment::getSpine)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }

        @Override
        public int compareTo(Sword o) {
            // First quality comparison
            var qualityComparison = this.quality.compareTo(o.quality);
            if (qualityComparison != 0) return qualityComparison;

            // Spine comparison
            for (int segmentIndex = 0; segmentIndex < spine.size(); segmentIndex++) {
                var spineComparisonValue = spine.get(segmentIndex).compareTo(o.spine.get(segmentIndex));
                if (spineComparisonValue != 0) {
                    return spineComparisonValue;
                }
            }

            // Last id comparison
            return id.compareTo(o.id);
        }
    }

    @Data
    private static class SpineSegment implements Comparable<SpineSegment> {
        private Long left;
        private Long right;
        private final Long spine;

        public SpineSegment(Long spine) {
            this.spine = spine;
        }

        private Long getSegmentValue() {
            return Long.parseLong("%s%s%s".formatted(
                    left == null ? "" : left,
                    spine == null ? "" : spine,
                    right == null ? "" : right));
        }

        @Override
        public int compareTo(SpineSegment o) {
            return getSegmentValue().compareTo(o.getSegmentValue());
        }

        @Override
        public String toString() {
            return "%s-%s-%s".formatted(left == null ? " " : left, spine, right == null ? " " : right);
        }
    }
}
