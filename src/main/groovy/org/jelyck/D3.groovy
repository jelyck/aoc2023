package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(3)
    println sumOfPartNumbers(data)
    println sumGearRatios(data)

}

static sumOfPartNumbers(List<String> data) {
    def schematic = data.collect { it.toList() }
    def sumOfPartNumbers = 0

    for (int i = 0; i < schematic.size(); i++) {
        def partNumber = ""
        def adjSymbol = false

        for (int j = 0; j < schematic[i].size(); j++) {
            if (schematic[i][j].isInteger()) {
                partNumber += schematic[i][j]

                List<String> adjacent = []
                if (i > 0) {
                    adjacent.add(schematic[i - 1][j])
                }
                if (i < schematic.size() - 1) {
                    adjacent.add(schematic[i + 1][j])
                }
                if (j > 0) {
                    adjacent.add(schematic[i][j - 1])
                }
                if (j < schematic[i].size() - 1) {
                    adjacent.add(schematic[i][j + 1])
                }
                if (i < schematic.size() - 1 && j < schematic[i].size() - 1) {
                    adjacent.add(schematic[i + 1][j + 1])
                }
                if (i < schematic.size() - 1 && j > 0) {
                    adjacent.add(schematic[i + 1][j - 1])
                }
                if (i > 0 && j < schematic[i].size() - 1) {
                    adjacent.add(schematic[i - 1][j + 1])
                }
                if (i > 0 && j > 0) {
                    adjacent.add(schematic[i - 1][j - 1])
                }

                if (adjacent.any { !it.find(/[0-9\.]/) }) {
                    adjSymbol = true
                }

            } else {
                if (partNumber && adjSymbol) {
                    sumOfPartNumbers += partNumber.toInteger()
                }
                partNumber = ""
                adjSymbol = false
            }
        }

        if (partNumber && adjSymbol) {
            sumOfPartNumbers += partNumber.toInteger()
        }
    }

    return sumOfPartNumbers
}

static sumGearRatios(List<String> data) {
    def schematic = data.collect { it.toList() }
    Map<Tuple2, List<Integer>> partMap = [:]

    for (int i = 0; i < schematic.size(); i++) {
        def partNumber = ""
        Set<Tuple2> adjacent = []

        for (int j = 0; j < schematic[i].size(); j++) {
            if (schematic[i][j].isInteger()) {
                partNumber += schematic[i][j]

                if (i > 0) {
                    if (schematic[i - 1][j] == "*") {
                        adjacent.add(new Tuple2(i - 1, j))
                    }
                }
                if (i < schematic.size() - 1) {
                    if (schematic[i + 1][j] == "*") {
                        adjacent.add(new Tuple2(i + 1, j))
                    }
                }
                if (j > 0) {
                    if (schematic[i][j - 1] == "*") {
                        adjacent.add(new Tuple2(i, j - 1))
                    }
                }
                if (j < schematic[i].size() - 1) {
                    if (schematic[i][j + 1] == "*") {
                        adjacent.add(new Tuple2(i, j + 1))
                    }
                }
                if (i < schematic.size() - 1 && j < schematic[i].size() - 1) {
                    if (schematic[i + 1][j + 1] == "*") {
                        adjacent.add(new Tuple2(i + 1, j + 1))
                    }
                }
                if (i < schematic.size() - 1 && j > 0) {
                    if (schematic[i + 1][j - 1] == "*") {
                        adjacent.add(new Tuple2(i + 1, j - 1))
                    }
                }
                if (i > 0 && j < schematic[i].size() - 1) {
                    if (schematic[i - 1][j + 1] == "*") {
                        adjacent.add(new Tuple2(i - 1, j + 1))
                    }
                }
                if (i > 0 && j > 0) {
                    if (schematic[i - 1][j - 1] == "*") {
                        adjacent.add(new Tuple2(i - 1, j - 1))
                    }
                }

            } else {
                if (partNumber && adjacent.size()) {
                    adjacent.each {
                        if (partMap[it]) {
                            partMap[it].add(partNumber.toInteger())
                        } else {
                            partMap[it] = [partNumber.toInteger()]
                        }
                    }
                }
                partNumber = ""
                adjacent = []
            }
        }

        if (partNumber && adjacent.size()) {
            adjacent.each {
                if (partMap[it]) {
                    partMap[it].add(partNumber.toInteger())
                } else {
                    partMap[it] = [partNumber.toInteger()]
                }
            }
        }
    }

    return partMap
            .findAll { it.value.size() == 2 }
            .collect { it.value[0] * it.value[1] }
            .sum()
}