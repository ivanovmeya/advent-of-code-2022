package day05

import readInput

fun main() {

    data class Operation(val from: Int, val to: Int, val quantity: Int)

    fun String.toCrates(): List<Char> {
        return this.chunked(4).map {
            it.trim().ifBlank {
                "[0]"
            }
        }.map { it[1] }
    }

    fun parseInitialStacks(input: List<String>): MutableMap<Int, ArrayDeque<Char>> {
        val cratesStacks: MutableMap<Int, ArrayDeque<Char>> = mutableMapOf()
        input.forEach { line ->
            line.toCrates().forEachIndexed { index, crate ->
                if (crate != '0') {
                    val fixedIndex = index + 1
                    val stack = cratesStacks[fixedIndex]
                    if (stack == null) {
                        cratesStacks[fixedIndex] = ArrayDeque(listOf(crate))
                    } else {
                        stack.addFirst(crate)
                    }
                }
            }
        }
        return cratesStacks
    }

    fun parseOperationsSafe(input: List<String>): List<Operation> {
        val move = "move"
        val from = "from"
        val to = "to"
        return input.map { operationString ->
            val quantityStartIndex = operationString.indexOf(move) + move.length + 1

            val quantityEndIndex = operationString.indexOf(' ', quantityStartIndex)

            val quantity = operationString.substring(quantityStartIndex, quantityEndIndex).toInt()

            val fromStartIndex = operationString.indexOf(from) + from.length + 1
            val fromEndIndex = operationString.indexOf(' ', fromStartIndex)
            val fromStack = operationString.substring(fromStartIndex, fromEndIndex).toInt()

            val toStartIndex = operationString.indexOf(to) + to.length + 1
            val toStack = operationString.substring(toStartIndex, operationString.length).toInt()

            Operation(from = fromStack, to = toStack, quantity = quantity)
        }
    }

    fun parseOperations(input: List<String>): List<Operation> {
        return input.map { operationString ->
            val split = operationString.split(' ')
            Operation(from = split[3].toInt(), to = split[5].toInt(), quantity = split[1].toInt())
        }
    }

    fun performOperationCrane9000(operation: Operation, stacks: MutableMap<Int, ArrayDeque<Char>>) {
        repeat(operation.quantity) {
            val removed = stacks[operation.from]?.removeLast()
                ?: throw IllegalStateException("There are no crates in stack ${operation.from}")
            stacks[operation.to]?.addLast(removed)
        }
    }

    fun performOperationCrane9001(operation: Operation, stacks: MutableMap<Int, ArrayDeque<Char>>) {
        //remove top N crates from
        //add removed crates to
        //order is preserved

        val fromStack = stacks[operation.from]
            ?: throw IllegalStateException("There are no stack with number ${operation.from}")

        val toStack = stacks[operation.to]
            ?: throw IllegalStateException("There are no stack with number ${operation.to}")

        println("performing operation = $operation")

        val topNCrates = fromStack.subList(
            fromIndex = fromStack.size - operation.quantity,
            toIndex = fromStack.size
        )

        println("topNCrate = $topNCrates")
        toStack.addAll(topNCrates)
        repeat(operation.quantity) {
            fromStack.removeLast()
        }

        println("toStack = $toStack")
        println("fromStack = $fromStack")

    }

    fun parseInput(input: List<String>): Pair<MutableMap<Int, ArrayDeque<Char>>, List<Operation>> {
        val emptyLineIndex = input.indexOfFirst { it.isEmpty() }
        val stackInput = input.subList(0, emptyLineIndex - 1)
        val operationInput = input.subList(emptyLineIndex + 1, input.size)

        val cratesStacks = parseInitialStacks(stackInput)
        val operations = parseOperations(operationInput)
        return Pair(cratesStacks, operations)
    }

    fun topCratesOnEachStack(cratesStacks: MutableMap<Int, ArrayDeque<Char>>) =
        cratesStacks.toSortedMap().values.map { it.last() }.fold(
            initial = "",
            operation = { acc: String, char: Char -> acc + char }
        )

    fun part1(input: List<String>): String {

        val (cratesStacks, operations) = parseInput(input)

        operations.forEach {
            performOperationCrane9000(it, cratesStacks)
        }

        return topCratesOnEachStack(cratesStacks)
    }

    fun part2(input: List<String>): String {
        val (cratesStacks, operations) = parseInput(input)

        operations.forEach {
            performOperationCrane9001(it, cratesStacks)
        }

        return topCratesOnEachStack(cratesStacks)
    }

    val testInput = readInput("day05/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == "CMZ")
    check(test2Result == "MCD")

    val input = readInput("day05/input")
    println(part1(input))
    println(part2(input))

}