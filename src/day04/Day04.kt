package day04

import readInput


fun main() {

    fun IntRange.hasOverlap(other: IntRange): Boolean {
        return !(this.last < other.first || this.first > other.last)
    }

    fun IntRange.fullyContains(other: IntRange): Boolean {
        return this.first >= other.first && this.last <= other.last
    }

    fun parseElfRanges(pairOfElfData: String) = pairOfElfData
        .split(',')
        .map { elfRange ->
            elfRange
                .split('-')
                .zipWithNext { start, end -> IntRange(start.toInt(), end.toInt()) }
                .first()
        }

    fun part1(input: List<String>): Long {
        return input.sumOf { pairOfElfData ->
            val (firstElfRange, secondElfRange) = parseElfRanges(pairOfElfData)
            if (
                firstElfRange.fullyContains(secondElfRange) || secondElfRange.fullyContains(firstElfRange)
            ) 1L else 0L
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { pairOfElfData ->
            val (firstElfRange, secondElfRange) = parseElfRanges(pairOfElfData)
            if (firstElfRange.hasOverlap(secondElfRange)) 1L else 0L
        }
    }

    val testInput = readInput("day04/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 2L)
    check(test2Result == 4L)

    val input = readInput("day04/input")
    check(part1(input) == 466L)
    check(part2(input) == 865L)

}