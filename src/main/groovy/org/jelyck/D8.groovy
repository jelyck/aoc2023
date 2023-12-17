package org.jelyck

import org.jelyck.utils.DataFetcher

static void main(String[] args) {
    def data = DataFetcher.get(8)
    println(escapeHauntedWasteland(data.collect()))
    println(escapeHauntedWastelandAsGhost(data.collect()))
}

static escapeHauntedWasteland(List<String> data) {
    def instructions = data.pop().split("").toList() as Queue

    def positions = data.findAll().collectEntries {
        def (_, p, l, r) = (it =~ /(\w+)\s+=\s+\((\w+),\s+(\w+)\)/)[0]
        return [p, [L: l, R: r]]
    }

    return countSteps('AAA', 'ZZZ', positions, instructions)
}

static escapeHauntedWastelandAsGhost(List<String> data) {
    def instructions = data.pop().toList() as Queue

    def positions = data.findAll().collectEntries {
        def (_, p, l, r) = (it =~ /(\w+)\s+=\s+\((\w+),\s+(\w+)\)/)[0]
        return [p, [L: l, R: r]]
    }

    def pos = positions.keySet()
            .findAll { it.takeRight(1) == "A" }

    def stepsToEnds = pos.collect {
        countSteps(it, 'Z', positions, instructions)
    }

    return lcm(stepsToEnds)
}

static countSteps(pos, end, positions, instructions) {
    def queue = instructions.collect() as Queue
    def steps = 0

    while (pos.takeRight(end.size()) != end) {
        def turn = queue.poll()
        pos = positions[pos][turn]
        queue.add(turn)
        steps++
    }

    return steps
}

static lcm(List numbers) {
    return numbers.inject { a, b -> ((a * b) / gcd(a, b)) }
}

static gcd(a, b) {
    return b == 0 ? a : gcd(b, a % b)
}