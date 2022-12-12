package day12

import readInput

fun main() {

    data class Point(val x: Int, val y: Int, var steps: Int = 0) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }

    val directions = listOf(
        Pair(0, 1),  // right
        Pair(1, 0),  // down
        Pair(0, -1), // left
        Pair(-1, 0)  // up
    )

    fun List<String>.toMatrix(): Array<CharArray> {
        return Array(this.size) { row ->
            this[row].toCharArray()
        }
    }

    fun Array<CharArray>.findStartAndDestination(): Pair<Point, Point> {
        var start = Point(-1, -1)
        var destination = Point(-1, -1)

        this.forEachIndexed { row, chars ->
            val startColumn = chars.indexOf('S')
            if (startColumn != -1) {
                start = Point(row, startColumn)
            }
            val destColumn = chars.indexOf('E')
            if (destColumn != -1) {
                destination = Point(row, destColumn)
            }
        }
        return start to destination
    }

    fun Array<CharArray>.findAllA(): List<Point> {
        val res = mutableListOf<Point>()

        this.forEachIndexed { row, chars ->
            val startColumn = chars.indexOf('S')
            val aColumn = chars.indexOf('a')

            if (startColumn != -1) {
                res.add(Point(row, startColumn))

            }

            if (aColumn != -1) {
                res.add(Point(row, aColumn))
            }
        }

        return res
    }

    fun Array<CharArray>.isValid(from: Point, to: Point): Boolean {
        //destination could be at most one higher
        //Start S has level 'a'
        //End E has level 'z'
        val fromChar = this[from.x][from.y]
        val toChar = this[to.x][to.y]
        return when {
            fromChar == 'S' -> true
            toChar == 'E' -> fromChar >= 'z' - 1
            else -> fromChar >= toChar - 1
        }
    }

    fun shortestPath(matrix: Array<CharArray>, start: Point, dest: Point): Int {
        //Using BFS to calc all path costs
        var shortestPath = -1

        val isVisited = Array(matrix.size) {
            BooleanArray(matrix[0].size) { false }
        }

        val queue = ArrayDeque<Point>().apply {
            add(start)
        }

        while (queue.isNotEmpty()) {
            val currentPoint = queue.removeFirst()
            if (isVisited[currentPoint.x][currentPoint.y]) continue

            isVisited[currentPoint.x][currentPoint.y] = true

            if (currentPoint.x == dest.x && currentPoint.y == dest.y) {
                if (shortestPath == -1 || currentPoint.steps < shortestPath) {
                    shortestPath = currentPoint.steps
                }
                continue
            }

            for (direction in directions) {
                val x = currentPoint.x + direction.first
                val y = currentPoint.y + direction.second

                if (x >= 0 && y >= 0 && x < matrix.size && y < matrix[0].size) {
                    if (!isVisited[x][y] && matrix.isValid(currentPoint, Point(x, y))) {
                        queue.addLast(Point(x, y, currentPoint.steps + 1))
                    }
                }
            }
        }

        return shortestPath
    }

    fun Array<CharArray>.printGrid() {
        this.forEach {
            println(it.joinToString(""))
        }
    }

    fun part1(input: List<String>): Int {
        val matrix = input.toMatrix()
        val (start, dest) = matrix.findStartAndDestination()
        return shortestPath(matrix, start, dest)
    }

    fun part2(input: List<String>): Int {
        val matrix = input.toMatrix()
        val (_, dest) = matrix.findStartAndDestination()
        val allAStarts = matrix.findAllA()
        return allAStarts.minOf {
            shortestPath(matrix, it, dest)
        }
    }

    val testInput = readInput("day12/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 31) { "Part ONE failed" }
    check(test2Result == 29) { "Part TWO failed" }

    val input = readInput("day12/input")
    val part1 = part1(input)
    val part2 = part2(input)

    check(part1 == 472)
    check(part2 == 465)
    println(part1)
    println(part2)
}