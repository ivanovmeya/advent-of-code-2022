package day10


import readInput

fun main() {

    data class Instruction(val cycles: Int, val registryValue: Int? = null)

    fun parseInstructions(input: List<String>): List<Instruction> {
        return input.map {
            if (it.contains(' ')) {
                val (_, registry) = it.split(' ')
                Instruction(2, registry.toInt())
            } else {
                Instruction(1, null)
            }
        }
    }

    fun printRegistry(registry: IntArray) {
        registry.forEachIndexed { index, i ->
            println("r[$index] = $i ")
        }
    }

    fun part1(input: List<String>): Int {

        val instructions = parseInstructions(input)

        val registry = IntArray(250) { 0 }

        registry[1] = 1

        var currentCycle = 1
        instructions.forEach { instruction ->
            repeat(instruction.cycles) {
                currentCycle++
                registry[currentCycle] = registry[currentCycle - 1]
            }
            if (instruction.registryValue != null) {
                registry[currentCycle] = registry[currentCycle - 1] + instruction.registryValue
            }
        }

        return (20..220 step 40).sumOf {
            it * registry[it]
        }
    }

    fun part2(input: List<String>): Int {
        val instructions = parseInstructions(input)

        val registry = IntArray(250) { 0 }

        registry[1] = 1

        //CRT draws pixel at currentCycle Position
        //CRT has 40 wide, and 6 height
        var currentCycle = 1
        instructions.forEach { instruction ->
            repeat(instruction.cycles) {
                val spritePosition = currentCycle % 40
                val registryValue = registry[currentCycle]

                print(if (registryValue in (spritePosition - 2 .. spritePosition)) "#" else ".")

                if ((currentCycle  % 40) == 0) {
                    println()
                }
                currentCycle++
                registry[currentCycle] = registry[currentCycle - 1]
            }
            if (instruction.registryValue != null) {
                registry[currentCycle] = registry[currentCycle - 1] + instruction.registryValue
            }


        }
        println()

        return (20..220 step 40).sumOf {
            it * registry[it]
        }
    }

    val testInput = readInput("day10/input_test")
    val test1Result = part1(testInput)
    println(test1Result)
    check(test1Result == 13140)
    val test2Result = part2(testInput)


    val input = readInput("day10/input")
    println(part1(input))
    part2(input)
}