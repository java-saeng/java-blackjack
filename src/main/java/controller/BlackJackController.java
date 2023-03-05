package controller;

import domain.cardtable.CardTable;
import domain.deck.CardDeck;
import domain.player.Name;
import domain.player.dealer.Dealer;
import domain.player.dealer.DealerResult;
import domain.player.participant.Participant;
import domain.player.participant.ParticipantResult;
import view.InputView;
import view.OutputView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class BlackJackController {

    public void run() {

        final CardDeck cardDeck = CardDeck.shuffledFullCardDeck();
        final CardTable cardTable = CardTable.readyToPlayBlackjack(cardDeck);

        final List<Participant> participants = dealParticipantsCards(cardTable);
        final Dealer dealer = dealDealerCards(cardTable);

        printStateAfterDealtCard(participants, dealer);
        hittingPlayer(cardDeck, participants, dealer);
        printStateAfterHittedCard(participants, dealer);

        final Map<Participant, ParticipantResult> playersResult = determineWinner(participants, dealer);
        final Map<DealerResult, Long> scoreBoard = countDealerResult(playersResult);

        printPlayerScoreBoard(participants, playersResult, scoreBoard);
    }

    private static void printPlayerScoreBoard(final List<Participant> participants,
                                              final Map<Participant, ParticipantResult> playersResult,
                                              final Map<DealerResult, Long> scoreBoard) {
        OutputView.showDealerScoreBoard(scoreBoard);
        OutputView.showParticipantsScoreBoard(playersResult, participants);
    }

    private static void printStateAfterHittedCard(final List<Participant> participants, final Dealer dealer) {
        OutputView.showPlayerStateResult(dealer);
        OutputView.showParticipantsStateResult(participants);
    }

    private void hittingPlayer(final CardDeck cardDeck, final List<Participant> participants, final Dealer dealer) {
        hitForParticipants(cardDeck, participants);
        hitForDealer(cardDeck, dealer);
    }

    private static void printStateAfterDealtCard(final List<Participant> participants, final Dealer dealer) {
        OutputView.showDealtCardTo(participants);
        OutputView.showStateOf(dealer);
        OutputView.showStateOf(participants);
    }

    private static Map<DealerResult, Long> countDealerResult(
            final Map<Participant, ParticipantResult> playersResult) {
        return playersResult.keySet()
                            .stream()
                            .collect(Collectors.groupingBy(participant -> playersResult.get(participant)
                                                                                       .convertToDealerResult(),
                                                           counting()));
    }

    private static Map<Participant, ParticipantResult> determineWinner(final List<Participant> participants,
                                                                       final Dealer dealer) {
        return participants.stream()
                           .collect(Collectors.toMap(
                                   Function.identity(),
                                   participant -> ParticipantResult.matchBetween(participant, dealer))
                           );
    }

    private void hitForDealer(final CardDeck cardDeck, final Dealer dealer) {
        while (dealer.canHit()) {
            OutputView.dealerOneMoreCard();
            dealer.hit(cardDeck.draw());
        }
    }

    private void hitForParticipants(final CardDeck cardDeck, final List<Participant> participants) {
        participants.forEach(participant -> hitForParticipant(cardDeck, participant));
    }

    private void hitForParticipant(final CardDeck cardDeck, final Participant participant) {
        while (participant.canHit() && inputHitOrStay(participant)) {
            participant.hit(cardDeck.draw());
            OutputView.showStateOf(participant);
        }
    }

    private boolean inputHitOrStay(final Participant participant) {
        return InputView.readMoreCard(participant).equals("y");
    }

    private Dealer dealDealerCards(final CardTable cardTable) {
        return new Dealer(cardTable.createCardArea());
    }

    private List<Participant> dealParticipantsCards(final CardTable cardTable) {
        return InputView.readParticipantsName()
                        .stream()
                        .map(Name::new)
                        .map(name -> new Participant(name, cardTable.createCardArea()))
                        .collect(Collectors.toList());

    }
}
