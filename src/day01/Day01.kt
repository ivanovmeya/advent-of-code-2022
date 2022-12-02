package day01

import readInput

fun main() {
    fun computeElfCalories(
        i: Int,
        input: List<String>
    ): Pair<Int, Int> {
        var i1 = i
        var currentElfCalories = 0
        while (i1 < input.size && input[i1].isNotEmpty() && input[i1].isNotBlank()) {
            currentElfCalories += input[i1].toInt()
            i1++
        }
        return currentElfCalories to i1
    }

    //O(n)
    fun part1(input: List<String>): Int {
        if (input.isEmpty()) return -1
        var maxCalories = 0

        var i = 0
        while (i < input.size) {
            val (currentElfCalories, iEnd) = computeElfCalories(i, input)
            i = iEnd
            if (currentElfCalories > maxCalories) {
                maxCalories = currentElfCalories
            }
            i++
        }

        return maxCalories
    }

    //O(n)
    fun part2(input: List<String>): Int {
        if (input.isEmpty()) return -1

        val topThreeMaxCalories = IntArray(3) { 0 }

        var i = 0
        while (i < input.size) {
            val (currentElfCalories, iEnd) = computeElfCalories(i, input)
            i = iEnd
            when {
                (currentElfCalories > topThreeMaxCalories[0]) -> {
                    topThreeMaxCalories[2] = topThreeMaxCalories[1]
                    topThreeMaxCalories[1] = topThreeMaxCalories[0]
                    topThreeMaxCalories[0] = currentElfCalories
                }
                (currentElfCalories > topThreeMaxCalories[1] && currentElfCalories != topThreeMaxCalories[0]) -> {
                    topThreeMaxCalories[2] = topThreeMaxCalories[1]
                    topThreeMaxCalories[1] = currentElfCalories
                }
                (currentElfCalories > topThreeMaxCalories[2] && currentElfCalories != topThreeMaxCalories[1]) -> {
                    topThreeMaxCalories[2] = currentElfCalories
                }
            }
            i++
        }
        return topThreeMaxCalories.sum()
    }

    val testInput = readInput("Day01_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 24000)
    check(test2Result == 45000)

    val input = readInput("day01")
    check(part1(input) == 74198)
    check(part2(input) == 209914)
}