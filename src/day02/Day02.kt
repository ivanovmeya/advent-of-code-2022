package day02

import readInput

enum class Figure(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

enum class RoundScore(val score: Int) {
    DRAW(3),
    WIN(6),
    LOOSE(0)
}

fun main() {

    fun Char.toRoundScore() = when(this) {
        'X' -> RoundScore.LOOSE
        'Y' -> RoundScore.DRAW
        'Z' -> RoundScore.WIN
        else -> throw IllegalArgumentException("Unknown input: $this")
    }

    fun Char.toFigure() = when(this) {
        'X','A' -> Figure.ROCK
        'Y','B' -> Figure.PAPER
        'Z','C' -> Figure.SCISSORS
        else -> { throw IllegalArgumentException("Unknown figure: $this")}
    }

    fun roundScorePart1(enemy: Char, you: Char): Int {
        val enemyFigure = enemy.toFigure()
        val youFigure = you.toFigure()

        val scoreForResult = when (enemyFigure) {
            youFigure -> 3
            Figure.ROCK -> if (youFigure == Figure.SCISSORS) RoundScore.LOOSE.score else RoundScore.WIN.score
            Figure.PAPER -> if (youFigure == Figure.ROCK) RoundScore.LOOSE.score else RoundScore.WIN.score
            Figure.SCISSORS -> if (youFigure == Figure.PAPER) RoundScore.LOOSE.score else RoundScore.WIN.score
        }

        return youFigure.score + scoreForResult
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { roundData ->
            val (enemy, you) = roundData.split(' ').map { it.first() }
            roundScorePart1(enemy, you)
        }
    }

    fun roundScorePart2(enemy: Char, wantedResult: Char): Int {
        val enemyFigure = enemy.toFigure()
        val figureScore = when (wantedResult.toRoundScore()) {
            RoundScore.LOOSE -> when (enemyFigure) {
                Figure.PAPER -> Figure.ROCK.score
                Figure.ROCK -> Figure.SCISSORS.score
                Figure.SCISSORS -> Figure.PAPER.score
            }
            RoundScore.WIN -> when (enemyFigure) {
                Figure.PAPER -> Figure.SCISSORS.score
                Figure.ROCK -> Figure.PAPER.score
                Figure.SCISSORS -> Figure.ROCK.score
            }
            RoundScore.DRAW -> when (enemyFigure) {
                Figure.PAPER -> Figure.PAPER.score
                Figure.ROCK -> Figure.ROCK.score
                Figure.SCISSORS -> Figure.SCISSORS.score
            }
        }

        return figureScore + wantedResult.toRoundScore().score
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { roundData ->
            val (enemy, you) = roundData.split(' ').map { it.first() }
            roundScorePart2(enemy, you)
        }
    }

    val testInput = readInput("day02/Day02_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 15)
    check(test2Result == 12)

    val input = readInput("day02/day02")
    println(part1(input))
    println(part2(input))
}