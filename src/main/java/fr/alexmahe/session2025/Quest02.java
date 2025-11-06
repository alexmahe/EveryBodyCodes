package fr.alexmahe.session2025;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class Quest02 {

    private static final Complex TEN = new Complex(10L, 10L);
    private static final Complex HUNDREDK = new Complex(100000L, 100000L);

    public static void main(String[] args) {
        var today = new Quest02();

        // PART 01
        var A = new Complex(160L, 50L);
        var result = A.deepCopy();
        for (long repetition = 0; repetition < 2; repetition++) {
            result = today.cycle(result, A, TEN);
        }
        log.info("Resultat partie 01 : {}", result);

        // PART 02
        A = new Complex(35300, -64910); // grid start
        var counter = today.cycleGrid(A, 1000, 10);
        log.info("Points à graver : {}", counter);

        // PART 03
        counter = today.cycleGrid(A, 1000, 1);
        log.info("Points à graver : {}", counter);
    }

    private int cycleGrid(Complex gridStart, int gridMaxSize, int granularity) {
        Complex result;
        var gridEnd = gridStart.add(new Complex(gridMaxSize, gridMaxSize));
        var counterResult = 0;
        var counterPoint = 0;
        var maxLimit = new BigDecimal(1000000);
        var minLimit = new BigDecimal(-1000000);
        var nbPointGrille = gridMaxSize * gridMaxSize;
        var nbPointToCheck = granularity * granularity;
        var checkpoint = Math.max(1000, nbPointToCheck / 100);
        for (long indexX = gridStart.X().longValueExact(); indexX <= gridEnd.X().longValueExact(); indexX += granularity) {
            for (long indexY = gridStart.Y().longValueExact(); indexY <= gridEnd.Y().longValueExact(); indexY += granularity) {
                if (counterPoint % checkpoint == 0) {
                    log.info("{} points calculés", counterPoint);
                    log.info("Avancement : {}%", (100 * (counterPoint)) / (nbPointGrille / nbPointToCheck));
                }
                counterPoint++;

                var engrave = true;
                var point = new Complex(indexX, indexY);
                result = point.deepCopy(); // equivaut au 1er cycle sur les 100

                for (long repetition = 0; repetition < 99; repetition++) {
                    result = cycle(result, point, HUNDREDK);

                    if (result.X().compareTo(maxLimit) > 0
                            || result.X().compareTo(minLimit) < 0
                            || result.Y().compareTo(maxLimit) > 0
                            || result.Y().compareTo(minLimit) < 0) {
                        engrave = false;
                        break;
                    }
                }

                if (engrave) counterResult++;
            }
        }
        return counterResult;
    }

    private Complex cycle(Complex target, Complex A, Complex divider) {
        return cycle(target, A, divider, false);
    }

    private Complex cycle(Complex target, Complex A, Complex divider, boolean doLog) {
        target = target.multiplyBy(target);
        if (doLog) {
            log.info("target post square : {}", target);
        }
        target = target.divideBy(divider);
        if (doLog) {
            log.info("target post divide : {}", target);
        }
        return target.add(A);
    }

    private record Complex(
            BigDecimal X,
            BigDecimal Y
    ) {
        public Complex(long x, long y) {
            this(new BigDecimal(x), new BigDecimal(y));
        }

        public Complex add(Complex other) {
            return new Complex(X.add(other.X), Y.add(other.Y));
        }

        public Complex multiplyBy(Complex other) {
            return new Complex(
                    X.multiply(other.X).subtract(Y.multiply(other.Y)),
                    X.multiply(other.Y).add(Y.multiply(other.X))
            );
        }

        public Complex divideBy(Complex other) {
            return new Complex(X.divideToIntegralValue(other.X), Y.divideToIntegralValue(other.Y));
        }

        public Complex deepCopy() {
            return new Complex(X, Y);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Complex complex = (Complex) o;
            return Objects.equals(X, complex.X) && Objects.equals(Y, complex.Y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(X, Y);
        }

        @Override
        public String toString() {
            return "[%s,%s]".formatted(X,Y);
        }
    }
}
