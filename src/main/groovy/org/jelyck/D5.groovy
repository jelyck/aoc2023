package org.jelyck

import org.jelyck.utils.DataFetcher
import groovy.time.*

static void main(String[] args) {
    def data = DataFetcher.get(5)

    println(lowestLocationForSeeds(data.collect()))

    def timeStart = new Date()
    println(lowestLocationForSeedsInterval(data.collect()))
    def timeStop = new Date()

    // 2 minutes, 57.918 seconds :')
    println(TimeCategory.minus(timeStop, timeStart))
}

static lowestLocationForSeeds(List<String> data) {
    def seeds = (data.pop() =~ /\d+/).findAll()*.toLong()
    def mappings = parseMappings(data)

    return seeds.collect { convert(mappings, "seed-to-soil", it) }.min()
}

static lowestLocationForSeedsInterval(List<String> data) {
    def seeds = (data.pop() =~ /(\d+)\s+(\d+)/).findAll()
            .collect { new Tuple2(it[1].toLong(), it[1].toLong() + it[2].toLong() - 1) }

    def mappings = parseMappings(data)

    return seeds.collect { convertInterval(mappings, "seed-to-soil", [it]).min { it.v1 }.v1 }.min()
}

static convert(Map map, String key, Long value) {
    if (key == null) {
        return value
    }

    def nextKey = map.keySet().find { k ->
        k.startsWith(key.split("-")[2])
    }

    for (m in map[key]) {
        if (value >= m.ss && value < m.ss + m.rl) {
            def nextVal = value + m.ds - m.ss
            return convert(map, nextKey, nextVal)
        }
    }

    return convert(map, nextKey, value)
}

static convertInterval(Map map, String key, List<Tuple2> values) {
    if (key == null) {
        return values
    }

    def nextKey = map.keySet().find { k ->
        k.startsWith(key.split("-")[2])
    }

    def nextValues = []
    while (values) {
        def value = values.pop()
        def mapped = []
        for (m in map[key]) {
            // |  | ---------
            //      --------- |  |
            if (value.v2 < m.ss || value.v1 >= m.ss + m.rl) {
                continue
            }
            //    ---|--|---
            if (value.v1 >= m.ss && value.v2 < m.ss + m.rl) {
                mapped.add(new Tuple2(value.v1 + m.ds - m.ss, value.v2 + m.ds - m.ss))
            }
            //  | ---|------
            if (value.v1 < m.ss && value.v2 >= m.ss && value.v2 < m.ss + m.rl) {
                values.add(new Tuple2(value.v1, m.ss - 1))
                mapped.add(new Tuple2(m.ss + m.ds - m.ss, value.v2 + m.ds - m.ss))
            }
            //    ------|--- |
            if (value.v1 >= m.ss && value.v1 < m.ss + m.rl && value.v2 > m.ss + m.rl) {
                mapped.add(new Tuple2(value.v1 + m.ds - m.ss, m.ss + m.rl - 1 + m.ds - m.ss))
                values.add(new Tuple2(m.ss + m.rl, value.v2))
            }
            //  | ---------- |
            if (value.v1 < m.ss && value.v2 > m.ss + m.rl) {
                values.add(new Tuple2(value.v1, m.ss - 1))
                mapped.add(new Tuple2(m.ss + m.ds - m.ss, m.ss + m.rl - 1 + m.ds - m.ss))
                values.add(new Tuple2(m.ss + m.rl, value.v2))
            }
        }

        if (mapped) {
            nextValues.addAll(mapped)
        } else {
            nextValues.add(value)
        }
    }

    return convertInterval(map, nextKey, nextValues)
}

static parseMappings(List<String> data) {
    def map = [:]
    def key = ""
    for (row in data.findAll()) {
        if (row.contains("map")) {
            key = row.split(" ")[0]
            map[key] = []
        } else {
            def (ds, ss, rl) = (row =~ /\d+/).findAll()*.toLong()
            map[key].add([ds: ds, ss: ss, rl: rl])
        }
    }
    return map
}
