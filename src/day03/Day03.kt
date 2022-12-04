package day03

import readInput

data class IndexCount(val stringIndex: Int, val commonCount: Int)

fun main() {

    fun Char.toPriority() = when {
        this.isLowerCase() -> this - 'a' + 1
        else -> this - 'A' + 27
    }

    fun String.toCompartments() = substring(0, length / 2) to substring(length / 2, length)

    fun findSameChar(strings: List<String>): Char? {
        val frequencyTable = hashMapOf<Char, IndexCount>()

        strings.forEachIndexed { index, s ->
            s.forEach {
                val indexCount = frequencyTable[it]
                if (indexCount == null) {
                    frequencyTable[it] = IndexCount(index, 1)
                } else {
                    if (indexCount.stringIndex != index) {
                        frequencyTable[it] = IndexCount(index, indexCount.commonCount + 1)
                    }
                }
            }
        }

        val sameChar = frequencyTable.firstNotNullOfOrNull {
            if (it.value.commonCount == strings.size) {
                it.key
            } else {
                null
            }
        }
        return sameChar
    }

    fun findSameChar(vararg params: String): Char? {
        return findSameChar(params.toList())
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { rucksack ->
            val (first, second) = rucksack.toCompartments()
            findSameChar(first, second)?.toPriority()
                ?: throw IllegalStateException("Rucksack $rucksack does not share element in it's compartmets")
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            findSameChar(it[0], it[1], it[2])?.toPriority()
            ?: throw IllegalStateException("Group of three Elfs ${it.joinToString("\n")} do not have shared element")
        }
    }

    val testInput = readInput("day03/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 157)
    check(test2Result == 70)

    val input = readInput("day03/input")
    check(part1(input) == 7850)
    check(part2(input) == 2581)
}