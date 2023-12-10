package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(4)
    println(sumCardsTotalWorth(data))
    println(sumTotalCards(data))
}

static sumCardsTotalWorth(List<String> data) {
    data.sum {
        def (_, w, m) = (it =~ /Card[0-9 ]+:([0-9 ]+)\|([0-9 ]+)/)[0]
        def winning = (w =~ /\d+/).findAll()
        def mine = (m =~ /\d+/).findAll()

        return mine.inject(0) { points, num ->
            if (num in winning) {
                if (points == 0) {
                    return 1
                } else {
                    return points * 2
                }
            }
            return points
        }
    }
}

static sumTotalCards(List<String> data) {
    def cards = 0
    def queue = data as Queue

    while (queue.size()) {
        def card = queue.poll()

        def (_, c, w, m) = (card =~ /Card\s+(\d+):([0-9 ]+)\|([0-9 ]+)/)[0]
        def winning = (w =~ /\d+/).findAll()
        def mine = (m =~ /\d+/).findAll()

        mine.intersect(winning).size().times {
            queue.add(data.get(c.toInteger() + it))
        }

        cards++
    }

    return cards
}
