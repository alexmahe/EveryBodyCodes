package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest08 {

    static void main() throws IOException {
        var today = new Quest08();

        // Part 01
        var input = Arrays.stream(Utils.readInputJoinOnNewLines(RESOURCES.formatted("session2025", "Quest08", "input01")).split(","))
                .map(Integer::parseInt)
                .toList();
        var nbPoints = 32;
        var counter = 0;
        for (int pointIndex = 0; pointIndex < input.size() - 1; pointIndex++) {
            if (Math.abs(input.get(pointIndex) - input.get(pointIndex + 1)) == nbPoints / 2) {
                counter++;
            }
        }

        log.info("On passe {} fois par le milieu", counter);

        // Part 02
        input = Arrays.stream(Utils.readInputJoinOnNewLines(RESOURCES.formatted("session2025", "Quest08", "input02")).split(","))
                .map(Integer::parseInt)
                .toList();
        var knotNb = today.parseThroughNailsAndCountKnots(input);
        log.info("On a {} noeuds", knotNb);

        // Part 03
        input = Arrays.stream(Utils.readInputJoinOnNewLines(RESOURCES.formatted("session2025", "Quest08", "input03")).split(","))
                .map(Integer::parseInt)
                .toList();
        var allSegments = new ArrayList<Segment>();
        for (int segmentIndex = 0; segmentIndex < input.size() - 1; segmentIndex++) {
            allSegments.add(new Segment(input.get(segmentIndex), input.get(segmentIndex + 1)));
        }
        var maxKnots = Long.MIN_VALUE;
        for (int i = 1; i <= 256; i++) {
            for (int j = i + 1; j < 256; j++) {
                var segment = new Segment(i, j);
                var nbKnots = allSegments.stream()
                        .filter(segment::crossSegment)
                        .count();

                if (allSegments.contains(segment)) nbKnots++;
                if (nbKnots > maxKnots) {maxKnots = nbKnots;}
            }
        }
        log.info("On a {} fils coupés au max", maxKnots);
    }

    private long parseThroughNailsAndCountKnots(List<Integer> nails) {
        var segments = new HashSet<Segment>();
        var knotNb = 0L;
        for (int pointIndex = 0; pointIndex < nails.size() - 1; pointIndex++) {
            var segment = new Segment(nails.get(pointIndex), nails.get(pointIndex + 1));

            knotNb += segments.stream()
                    .filter(segment::crossSegment)
                    .count();

            segments.add(segment);
        }

        return knotNb;
    }

    private record Segment(
            int start,
            int end
    ) {
        private boolean crossSegment(Segment other) {
            return (isBetween(start, other.start(), other.end()) && isOutside(end, other.start(), other.end()))
                    ||(isBetween(end, other.start(), other.end()) && isOutside(start, other.start(), other.end()));
        }

        private boolean isBetween(int a, int b, int c) {
            return Math.min(b, c) < a && a < Math.max(b, c);
        }

        private boolean isOutside(int a, int b, int c) {
            return a < Math.min(b, c) || a > Math.max(b, c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.max(start, end), Math.min(start, end));
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Segment)) return false;
            Segment other = (Segment) obj;
            return (start == other.start && end == other.end) || (start == other.end && end == other.start);
        }

        @Override
        public String toString() {
            return "[%s,%s]".formatted(start, end);
        }
    }

}
