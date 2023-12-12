package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(6)
    println(waysToBeatRecord(data))
    println(waysToBeatRecordOneRace(data))
}

static waysToBeatRecord(List<String> data) {
    def races = data
            .collect {
                (it =~ /\d+/)
                        .findAll()
                        *.toLong()
            }
            .transpose()

    return races
            .collect { calcRecords(it[0], it[1]) }
            .inject(1) { total, score -> total * score }
}

static waysToBeatRecordOneRace(List<String> data) {
    def (totalTime, recordDistance) = data
            .collect {
                (it =~ /\d+/)
                        .findAll()
                        .join()
                        .toLong()
            }

    return calcRecords(totalTime, recordDistance)
}

static calcRecords(Long totalTime, Long recordDistance) {
    def numRecords = 0

    for (holdTime in 0..totalTime) {
        def speed = holdTime
        def time = totalTime - holdTime
        def newDistance = speed * time

        if (newDistance > recordDistance) {
            numRecords++
        }
    }

    return numRecords
}