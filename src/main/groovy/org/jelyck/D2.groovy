package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(2)
    println sumIdsOfPossibleGames(data)
    println sumPowerOfFewestPossibleCubes(data)
}

static sumIdsOfPossibleGames(List<String> data) {
    def max = [red: 12, green: 13, blue: 14]

    data.sum {
        def (_, gameNum, records) = (it =~ /Game (\d+):(.*)/)[0]

        gameNum = gameNum as Integer
        records = records as String

        def draws = records.split(";")
        def isImpossible = draws.any { draw ->
            (draw =~ /(\d+) (red|green|blue)/).findAll()
                    .collectEntries { [it[2], it[1] as Integer] }
                    .any { color, amount -> amount > max[color] }
        }

        return isImpossible ? 0 : gameNum
    }
}

static sumPowerOfFewestPossibleCubes(List<String> data) {
    data.sum {
        def records = it.find(/Game \d+:(.*)/)
        def draws = records.split(";")

        def max = [red: 0, green: 0, blue: 0]

        draws.each { draw ->
            (draw =~ /(\d+) (red|green|blue)/).findAll()
                    .collectEntries { [it[2], it[1] as Integer] }
                    .forEach { color, amount ->
                        if (amount > max[color]) {
                            max[color] = amount
                        }
                    }
        }

        return max.red * max.green * max.blue
    }
}



