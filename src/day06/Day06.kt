package day06

import readInput

fun main() {

    /**
     * As groupBy practically creates a map for each substring
     * performance: O(n)
     * space complexity: n/markerLength times allocation of markerLength map (LinkedHashMap)
     * (each time is eligible for GC after moving forward)
     */
    fun areSymbolsUnique(substring: String, markerLength: Int): Boolean {
        return substring.groupBy { it }.size == markerLength
    }

    fun findMarkerStart(dataStream: String, markerLength: Int): Int {
        val markerIndex = dataStream.windowed(markerLength, 1).indexOfFirst {
            areSymbolsUnique(it, markerLength)
        }
        return markerIndex
    }

    fun areSymbolsUnique(charFrequencies: HashMap<Char, Int>, markerLength: Int): Boolean {
        //if all characters occurs exactly once -> then they are all unique
        return charFrequencies.filterValues { charFreq -> charFreq == 1 }.size == markerLength
    }

    fun HashMap<Char, Int>.addChar(charToAdd: Char) {
        this[charToAdd] = this[charToAdd]?.let { it + 1 } ?: 1
    }

    fun HashMap<Char, Int>.removeChar(charToRemove: Char) {
        this[charToRemove] = this[charToRemove]?.let { it - 1 } ?: 0
    }

    /**
     * Sliding window approach with char frequencies in hashMap adjustments
     * performance: O(n)
     * space complexity: O(C) at worst case map with 25 keys (25 - English lowercase alphabet size)
     */
    fun findMarkerStartOptimized(dataStream: String, markerLength: Int): Int {
        if (dataStream.length < markerLength) throw IllegalArgumentException("Hey Elfs, the dataStream is too short")


        var startIndex = 0
        var endIndex = markerLength - 1
        val charFrequencies = hashMapOf<Char, Int>()

        //build initial charFrequencies
        var i = 0
        while (i <= endIndex) {
            charFrequencies.addChar(charToAdd = dataStream[i])
            i++
        }

        //go with a flow
        while (endIndex + 1 < dataStream.length) {
            val areUnique = areSymbolsUnique(charFrequencies, markerLength)
            if (areUnique) return endIndex + 1 - markerLength

            charFrequencies.removeChar(dataStream[startIndex])
            charFrequencies.addChar(dataStream[endIndex + 1])
            startIndex++
            endIndex++
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val markerLength = 4
        return findMarkerStartOptimized(
            dataStream = input[0],
            markerLength = markerLength
        ) + markerLength
    }

    fun part2(input: List<String>): Int {
        val markerLength = 14
        return findMarkerStartOptimized(
            dataStream = input[0],
            markerLength = markerLength
        ) + markerLength
    }

    val testInput = readInput("day06/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 7)
    check(test2Result == 19)

    val input = readInput("day06/input")
    val part1 = part1(input)
    val part2 = part2(input)
    println(part1)
    println(part2)

    check(part1 == 1848)
    check(part2 == 2308)

}