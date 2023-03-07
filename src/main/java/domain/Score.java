package domain;

import java.util.Objects;

public class Score {

    public static final Score MIN = new Score(0);

    private static final Score UPPER_LIMIT_SCORE = new Score(21);

    private static final Score REMAIN_SCORE_ACE = new Score(10);

    private final int value;

    public Score(final int value) {
        this.value = value;
    }

    public boolean isBust() {
        return this.isGreaterThan(UPPER_LIMIT_SCORE);
    }

    public boolean canMoreCard() {
        return this.isLessThan(UPPER_LIMIT_SCORE);
    }

    public boolean isLessThan(final Score other) {
        return value < other.value;
    }

    public boolean isLessEqualThan(final Score other) {
        return value <= other.value;
    }

    public boolean isGreaterThan(final Score other) {
        return value > other.value;
    }

    public Score plusTenIfNotBurst() {
        final Score plusScore = this.plus(REMAIN_SCORE_ACE);

        if (plusScore.isLessEqualThan(UPPER_LIMIT_SCORE)) {
            return this.plus(REMAIN_SCORE_ACE);
        }

        return this;
    }

    public Score plus(Score other) {
        return new Score(value + other.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Score score = (Score) o;
        return value == score.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int value() {
        return value;
    }
}
