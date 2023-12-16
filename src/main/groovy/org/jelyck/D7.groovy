package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(7)
    println(camelCards(data))
    println(camelCardsWithJokers(data))
}

static camelCards(List<String> data) {
    def cards = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".reverse()

    def types = [
            "fiveofakind" : (List hand) -> { hand.countBy { it }.values().sort() == [5] },
            "fourofakind" : (List hand) -> { hand.countBy { it }.values().sort() == [1, 4] },
            "fullhouse"   : (List hand) -> { hand.countBy { it }.values().sort() == [2, 3] },
            "threeofakind": (List hand) -> { hand.countBy { it }.values().sort() == [1, 1, 3] },
            "twopair"     : (List hand) -> { hand.countBy { it }.values().sort() == [1, 2, 2] },
            "onepair"     : (List hand) -> { hand.countBy { it }.values().sort() == [1, 1, 1, 2] },
            "highcard"    : (List hand) -> { hand.countBy { it }.values().sort() == [1, 1, 1, 1, 1] },
    ]

    def ranked = data.sort { a, b ->
        def hand1 = a.split(" ")[0].toList()
        def hand2 = b.split(" ")[0].toList()

        for (entry in types) {
            if (entry.value.call(hand1) <=> entry.value.call(hand2)) {
                return entry.value.call(hand1) <=> entry.value.call(hand2)
            }
        }

        for (card in [hand1, hand2].transpose()) {
            if (cards.indexOf(card[0]) <=> cards.indexOf(card[1])) {
                return cards.indexOf(card[0]) <=> cards.indexOf(card[1])
            }
        }
    }

    return ranked.withIndex().sum { r, i -> (i + 1) * r.split(" ")[1].toInteger() }
}

static camelCardsWithJokers(List<String> data) {
    def cards = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J".reverse()

    def handIsType = (List hand, List type) -> {
        def possibleTypes = hand.collect { card ->
            hand.collect { it == "J" ? card : it }.countBy { it }
        }

        def highestType = possibleTypes
                .sort { it.values().sum { it * it } }
                .reverse()[0]

        return highestType.values().sort() == type
    }

    def types = [
            "fiveofakind" : (List hand) -> { handIsType(hand, [5]) },
            "fourofakind" : (List hand) -> { handIsType(hand, [1, 4]) },
            "fullhouse"   : (List hand) -> { handIsType(hand, [2, 3]) },
            "threeofakind": (List hand) -> { handIsType(hand, [1, 1, 3]) },
            "twopair"     : (List hand) -> { handIsType(hand, [1, 2, 2]) },
            "onepair"     : (List hand) -> { handIsType(hand, [1, 1, 1, 2]) },
            "highcard"    : (List hand) -> { handIsType(hand, [1, 1, 1, 1, 1]) },
    ]

    def ranked = data.sort { a, b ->
        def hand1 = a.split(" ")[0].toList()
        def hand2 = b.split(" ")[0].toList()

        for (entry in types) {
            if (entry.value.call(hand1) <=> entry.value.call(hand2)) {
                return entry.value.call(hand1) <=> entry.value.call(hand2)
            }
        }

        for (card in [hand1, hand2].transpose()) {
            if (cards.indexOf(card[0]) <=> cards.indexOf(card[1])) {
                return cards.indexOf(card[0]) <=> cards.indexOf(card[1])
            }
        }
    }

     return ranked.withIndex().sum { r, i -> (i + 1) * r.split(" ")[1].toInteger() }
}