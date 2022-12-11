package day11

import readInput

fun main() {

    data class Test(val testDivider: Long, val predicate: (Long) -> Boolean, val trueMonkey: Int, val falseMonkey: Int)

    data class Monkey(
        val worries: ArrayDeque<Long>,
        val operation: (Long) -> Long,
        val test: Test,
        var inspections: Int = 0
    ) {
        override fun toString(): String {
            return "items=${worries.joinToString(",")}, inspectionTimes = $inspections"
        }
    }

    fun String.contentPart() = substring(indexOfFirst { it == ':' } + 1)

    fun String.toWorries(): List<Long> {
        return contentPart().split(',').map { it.trim().toLong() }
    }

    fun String.toOperation(): (Long) -> Long {
        val operationRaw = contentPart()
        val mathOperationRaw = operationRaw[operationRaw.indexOf("old") + 4]
        val rightOperandRaw = operationRaw.split(' ').last()
        val operation: (Long) -> Long = { old ->
            val rightOperand = if (rightOperandRaw.last().isDigit()) {
                rightOperandRaw.trim().toLong()
            } else {
                old
            }

            when (mathOperationRaw) {
                '*' -> old * rightOperand
                '+' -> old + rightOperand
                else -> old + rightOperand
            }
        }
        return operation
    }

    fun parseMonkeys(input: List<String>): Array<Monkey> {
        val monkeysRaw = input.chunked(7)
        return monkeysRaw.map { monkeyRaw ->
            val worries = monkeyRaw[1].toWorries()
            val operation = monkeyRaw[2].toOperation()

            val testOperand = monkeyRaw[3].contentPart().substringAfter("by ").toLong()
            val trueMonkey = monkeyRaw[4].contentPart().substringAfter("monkey ").toInt()
            val falseMonkey = monkeyRaw[5].contentPart().substringAfter("monkey ").toInt()

            Monkey(
                worries = ArrayDeque(worries),
                operation = operation,
                test = Test(
                    testDivider = testOperand,
                    predicate = { it % testOperand == 0L },
                    trueMonkey = trueMonkey,
                    falseMonkey = falseMonkey
                ),
            )
        }.toTypedArray()
    }

    fun topTwoMonkeysBusinessMultiplied(monkeys: Array<Monkey>, rounds: Int, worryReliefMultiplier: Int = 1): Long {
        val lcm = monkeys
            .map { it.test.testDivider }
            .fold(1) { acc: Long, divider: Long -> acc * divider }

        repeat(rounds) {
            monkeys.forEach { monkey ->
                while (monkey.worries.isNotEmpty()) {
                    val worry = monkey.worries.removeFirst()
                    monkey.inspections++

                    val newWorry = (monkey.operation(worry) / worryReliefMultiplier.toLong()) % lcm

                    val monkeyToThrowIndex = if (monkey.test.predicate(newWorry)) {
                        monkey.test.trueMonkey
                    } else {
                        monkey.test.falseMonkey
                    }
                    monkeys[monkeyToThrowIndex].worries.addLast(newWorry)
                }
            }
        }

        val business = monkeys.sortedByDescending { it.inspections }.take(2).fold(1L) { acc: Long, monkey: Monkey ->
            acc * monkey.inspections
        }
        return business

    }

    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input)
        return topTwoMonkeysBusinessMultiplied(monkeys, 20, 3)
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)
        return topTwoMonkeysBusinessMultiplied(monkeys, 10000, 1)
    }

    val testInput = readInput("day11/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 10605L) { "Part ONE failed" }
    check(test2Result == 2713310158L) { "Part TWO failed" }

    val input = readInput("day11/input")
    val part1 = part1(input)
    val part2 = part2(input)

    check(part1 == 61503L)
    check(part2 == 14081365540L)
    println(part1)
    println(part2)
}