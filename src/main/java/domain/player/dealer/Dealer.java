package domain.player.dealer;

import domain.area.CardArea;
import domain.card.Card;
import domain.player.Name;
import domain.player.Player;

public class Dealer extends Player {

    private static final int DEALER_LIMIT_SCORE = 16;
    private static final String DEALER_NAME = "딜러";

    public Dealer(final CardArea cardArea) {
        super(new Name(DEALER_NAME), cardArea);
    }

    @Override
    public boolean canHit() {
        return score() <= DEALER_LIMIT_SCORE;
    }

    public Card faceUpFirstCard() {
        return cardArea.firstCard();
    }
}
