package domain.player.participant.betresult;

import domain.player.participant.Money;

public interface BetResultState {


    Money calculateBetOutComeOf(Money money);
}