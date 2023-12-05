package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(1)
    println(sumCalibrationValues(data))
    println(sumCalibrationValuesWithText(data))
}

static sumCalibrationValues(List<String> data) {
    data.sum {
        def first = it.find(/\d/)
        def last = it.reverse().find(/\d/)

        return (first + last).toInteger()
    }
}

static sumCalibrationValuesWithText(List<String> data) {
    Map<String, String> map = [
            one  : "1",
            two  : "2",
            three: "3",
            four : "4",
            five : "5",
            six  : "6",
            seven: "7",
            eight: "8",
            nine : "9"
    ]
    Map<String, String> reversedMap = map.collectEntries { key, val -> [key.reverse(), val] }

    data.sum {
        def first = getCalibrationValueWithWords(it, map)
        def last = getCalibrationValueWithWords(it.reverse(), reversedMap)

        return (first + last).toInteger()
    }
}

static getCalibrationValueWithWords(String row, Map<String, String> words) {
    def tmp = ""
    for (c in row) {
        if (c.isInteger()) {
            return c
        }
        tmp = tmp + c
        for (word in words.keySet())
            if (tmp.contains(word)) {
                return words[word]
            }
    }
}




