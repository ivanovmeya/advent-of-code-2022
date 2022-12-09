package day09

import readInput
import kotlin.math.abs
import kotlin.math.sign

enum class Direction {
    LEFT,
    UP,
    RIGHT,
    DOWN;

    companion object {
        fun from(letter: Char) = when (letter) {
            'L' -> LEFT
            'U' -> UP
            'R' -> RIGHT
            'D' -> DOWN
            else -> throw IllegalArgumentException("Wow wow wow, unknown move $letter")
        }
    }
}

fun main() {


    data class Move(val direction: Direction, val steps: Int)
    data class Point(val i: Int, val j: Int) {
        override fun toString(): String {
            return "$i,$j"
        }
    }

    fun List<String>.parseMoves(): List<Move> {
        return this.map {
            val (directionRow, stepsRow) = it.split(' ')
            Move(
                direction = Direction.from(directionRow.first()),
                steps = stepsRow.toInt()
            )
        }
    }

    fun processMoveStep(initial: Point, move: Move): Point {
        return when (move.direction) {
            Direction.LEFT -> initial.copy(j = initial.j - 1)
            Direction.UP -> initial.copy(i = initial.i + 1)
            Direction.RIGHT -> initial.copy(j = initial.j + 1)
            Direction.DOWN -> initial.copy(i = initial.i - 1)
        }
    }

    fun Point.atSamePosition(other: Point) = this.i == other.i && this.j == other.j
    fun Point.isTouching(other: Point) = !this.atSamePosition(other) &&
            abs(other.i - this.i) <= 1 && abs(other.j - this.j) <= 1


    fun adjustTailPosition(tail: Point, head: Point): Point {
        return when {
            tail.atSamePosition(head) || tail.isTouching(head) -> tail.copy()
            else -> Point(
                i = tail.i + (head.i - tail.i).sign,
                j = tail.j + (head.j - tail.j).sign
            )
        }
    }

    fun moveKnots(knotsSnake: MutableList<Point>) {
        for (i in 0..knotsSnake.size - 2) {
            knotsSnake[i + 1] = adjustTailPosition(knotsSnake[i + 1], knotsSnake[i])
        }
    }

    @Suppress("KotlinConstantConditions")
    fun tailVisitedCount(input: List<String>, numberOfKnots: Int): Int {
        val moves = input.parseMoves()
        val isVisited = hashMapOf<String, Boolean>()
        val knotsSnake = Array(numberOfKnots) { Point(0, 0) }.toMutableList()

        isVisited[knotsSnake[numberOfKnots - 1].toString()] = true
        moves.forEach { move ->
            repeat(move.steps) {
                knotsSnake[0] = processMoveStep(knotsSnake[0], move)
                moveKnots(knotsSnake)
                isVisited[knotsSnake[numberOfKnots - 1].toString()] = true
            }
        }

        return isVisited.size
    }

    fun part1(input: List<String>): Int {
        return tailVisitedCount(input, 2)
    }

    fun part2(input: List<String>): Int {
        return tailVisitedCount(input, 10)
    }

    val testInput = readInput("day09/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)
    val testInputLarge = readInput("day09/input_test_large")
    val test2LargeResult = part2(testInputLarge)

    println(test1Result)
    println(test2Result)
    println(test2LargeResult)

    check(test1Result == 13)
    check(test2Result == 1)
    check(test2LargeResult == 36)

    val input = readInput("day09/input")
    val part1 = part1(input)
    val part2 = part2(input)

    check(part1 == 6037)
    check(part2 == 2485)
    println(part1)
    println(part2)
}