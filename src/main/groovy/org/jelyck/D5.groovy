package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(5)
    println(lowestLocationForSeeds(data))
}

static lowestLocationForSeeds(List<String> data) {
    def seeds = (data.pop() =~ /\d+/).findAll()*.toLong()
    def mappings = parseMappings(data)

    return seeds.collect { convert(mappings, "seed-to-soil", it) }.min()
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
            def nextVal = value - m.ss + m.ds
            return convert(map, nextKey, nextVal)
        }
    }

    return convert(map, nextKey, value)
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
