package fr.alexmahe.session2025;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class Quest02 {

    private static final Complex TEN = new Complex(10L, 10L);
    private static final Complex HUNDREDK = new Complex(100000L, 100000L);

    public static void main() {
        var today = new Quest02();

        // PART 01
        var A = new Complex(160L, 50L);
        var result = A.deepCopy();
        for (long repetition = 0; repetition < 2; repetition++) {
            result = today.cycle(result, A, TEN);
        }
        log.info("Resultat partie 01 : {}", result);

        // PART 02
        A = new Complex(-79027,-15068); // grid start
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
        var maxLimit = new BigDecimal(1000000);
        var minLimit = new BigDecimal(-1000000);
        for (long indexX = gridStart.X().longValueExact(); indexX <= gridEnd.X().longValueExact(); indexX += granularity) {
            for (long indexY = gridStart.Y().longValueExact(); indexY <= gridEnd.Y().longValueExact(); indexY += granularity) {
                var engrave = true;
                var point = new Complex(indexX, indexY);
                result = point.deepCopy(); // equivaut au 1er cycle sur les 100

                for (long repetition = 0; repetition < 99; repetition++) {
                    result = cycle(result, point, HUNDREDK);

                    if (isOutsideLimit(result, maxLimit, minLimit)) {
                        engrave = false;
                        break;
                    }
                }

                if (engrave) counterResult++;
            }
        }
        return counterResult;
    }

    private boolean isOutsideLimit(Complex result, BigDecimal maxLimit, BigDecimal minLimit) {
        return result.X().compareTo(maxLimit) > 0
               || result.X().compareTo(minLimit) < 0
               || result.Y().compareTo(maxLimit) > 0
               || result.Y().compareTo(minLimit) < 0;
    }

    private Complex cycle(Complex target, Complex A, Complex divider) {
        target = target.multiplyBy(target);
        target = target.divideBy(divider);
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
        public String toString() {
            return "[%s,%s]".formatted(X,Y);
        }
    }
}
