package day08

import readInput
import java.lang.Integer.max

/**
 * indicates the highest tree to the left/up/right/down from this position
 */
data class HeightLookup(var left: Int, var up: Int, var right: Int, var down: Int)

fun main() {

    fun copyOuterTreeRing(
        lookupHeights: Array<Array<HeightLookup>>,
        heightMap: Array<IntArray>,
        n: Int,
        m: Int
    ) {
        lookupHeights[0] = heightMap[0].map { HeightLookup(0, it, 0, 0) }.toTypedArray()
        lookupHeights[n - 1] = heightMap[n - 1].map { HeightLookup(0, 0, 0, it) }.toTypedArray()

        heightMap.forEachIndexed { row, heights ->
            lookupHeights[row][0] = HeightLookup(heights[0], 0, 0, 0)
            lookupHeights[row][m - 1] = HeightLookup(0, 0, heights[m - 1], 0)
        }
    }

    fun parseHeightMap(input: List<String>): Array<IntArray> {
        if (input.isEmpty()) throw IllegalArgumentException("Ooops, there are no trees! Get out of here!")
        return input
            .map { line ->
                line.map { it.digitToInt() }.toIntArray()
            }
            .toTypedArray()
    }

    fun printHeights(input: Array<IntArray>) {
        input.forEach {
            println(it.joinToString())
        }
    }

    fun printLookup(input: Array<Array<HeightLookup>>) {
        input.forEach { row ->
            println(row.joinToString { it.left.toString() })
        }
    }

    fun buildMaxHeightLookup(heightMap: Array<IntArray>): Array<Array<HeightLookup>> {
        val n = heightMap.size
        val m = heightMap[0].size

        val lookupHeights = Array(n) { Array(m) { HeightLookup(0, 0, 0, 0) } }

        //copy outer ring of trees as they are totally visible
        copyOuterTreeRing(lookupHeights, heightMap, n, m)

        //4 traversal
        //max Left
        for (row in 1 until n - 1) {
            for (column in 1 until m - 1) {
                lookupHeights[row][column].left = max(lookupHeights[row][column - 1].left, heightMap[row][column - 1])
            }
        }

        //max Right
        for (row in 1 until n - 1) {
            for (column in m - 2 downTo 1) {
                lookupHeights[row][column].right = max(lookupHeights[row][column + 1].right, heightMap[row][column + 1])
            }
        }

        //max Up
        for (column in 1 until n - 1) {
            for (row in 1 until m - 1) {
                lookupHeights[row][column].up = max(lookupHeights[row - 1][column].up, heightMap[row - 1][column])
            }
        }

        //max Down
        for (column in 1 until n - 1) {
            for (row in m - 2 downTo 1) {
                lookupHeights[row][column].down = max(lookupHeights[row + 1][column].down, heightMap[row + 1][column])
            }
        }

        return lookupHeights
    }

    fun buildScenicScoreMap(heightMap: Array<IntArray>): Array<IntArray> {
        val rows = heightMap.size
        val columns = heightMap[0].size

        val scenicScoreMap = Array(rows) { IntArray(columns) }

        for (row in 1 until rows - 1) {
            for (column in 1 until columns - 1) {
                val height = heightMap[row][column]
                var scenicScore: Int

                //go left
                var i = column - 1
                var leftScore = 1
                while (i > 0 && heightMap[row][i] < height) {
                    leftScore++
                    i--
                }

                //go right
                i = column + 1
                var rightScore = 1
                while (i < columns - 1 && heightMap[row][i] < height) {
                    rightScore++
                    i++
                }

                //go up
                var j = row - 1
                var upScore = 1
                while (j > 0 && heightMap[j][column] < height) {
                    j--
                    upScore++
                }

                //go down
                j = row + 1
                var downScore = 1
                while (j < rows - 1 && heightMap[j][column] < height) {
                    downScore++
                    j++
                }

                scenicScore = leftScore * upScore * rightScore * downScore
                scenicScoreMap[row][column] = scenicScore
            }
        }
        return scenicScoreMap
    }

    fun part1(input: List<String>): Int {

        val heightMap = parseHeightMap(input)
        val lookupMap = buildMaxHeightLookup(heightMap)

        //now look at each tree and check if it is visible
        //the tree is visible if value more than at least one value in all dimensions in lookupMap[row][column]
        val corners = 4
        val outerRingSize = (heightMap.size + heightMap[0].size) * 2 - corners

        var visibleTreeCount = 0

        for (row in 1 until heightMap.size - 1) {
            for (column in 1 until heightMap[0].size - 1) {
                val height = heightMap[row][column]
                val lookup = lookupMap[row][column]

                val isVisible = height > lookup.left || height > lookup.up
                        || height > lookup.right || height > lookup.down
                if (isVisible) {
                    visibleTreeCount++
                }
            }
        }

        return outerRingSize + visibleTreeCount
    }

    fun part2(input: List<String>): Int {
        val heightMap = parseHeightMap(input)
        return buildScenicScoreMap(heightMap).maxOf { rows ->
            rows.maxOf { it }
        }
    }

    val testInput = readInput("day08/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 21)
    check(test2Result == 8)

    val input = readInput("day08/input")
    val part1 = part1(input)
    val part2 = part2(input)

    check(part1 == 1794)
    check(part2 == 199272)
    println(part1)
    println(part2)

}