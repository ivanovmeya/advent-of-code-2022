package day07

import readInput

fun main() {

    data class File(val name: String, val ext: String?, val size: Long)

    data class Dir(
        val name: String,
        val files: MutableList<File>,
        val dirs: MutableList<Dir>,
        val parentDir: Dir?,
        var calculatedSize: Long = -1
    )

    fun String.toDir(parent: Dir?) = Dir(
        name = this.split(' ').last(),
        files = mutableListOf(),
        dirs = mutableListOf(),
        parentDir = parent
    )

    fun String.toFile(): File {
        val (size, fileName) = this.split(' ')
        val (name, ext) = if (fileName.contains('.')) {
            val splits = fileName.split('.')
            splits.first() to splits.last()
        } else {
            fileName to null
        }

        return File(
            name = name,
            ext = ext,
            size = size.toLong()
        )
    }

    fun parseDirectories(input: List<String>): Dir {
        val root = Dir(
            name = "/",
            files = mutableListOf(),
            dirs = mutableListOf(),
            parentDir = null
        )

        var currentDir = root

        input.drop(1).forEach { line ->
            if (line.startsWith('$')) {
                //parse commands
                when {
                    line.contains("cd") -> {
                        currentDir = when {
                            line.contains("..") -> {
                                currentDir.parentDir
                                    ?: throw IllegalArgumentException("You are in the root dir = ${currentDir.name}, can't go up ")
                            }
                            else -> {
                                val dirName = line.split(' ').last()
                                currentDir.dirs.first { it.name == dirName }
                            }
                        }
                    }
                    line.contains("ls") -> { /*just skip*/
                    }
                }

            } else {
                //parse directory content
                when {
                    line.startsWith("dir") -> {
                        val dir = line.toDir(currentDir)
                        currentDir.dirs.add(dir)
                    }
                    else -> {
                        currentDir.files.add(line.toFile())
                    }
                }
            }

        }
        return root
    }

    fun spacePrefixByLevel(level: Int): String {
        val spaceBuilder = StringBuilder()
        repeat((level * 1.5).toInt()) {
            spaceBuilder.append(' ')
        }
        return spaceBuilder.toString()
    }

    fun printDir(dir: Dir, level: Int) {
        println("${spacePrefixByLevel(level)}- ${dir.name} (dir), calcSize = ${dir.calculatedSize}")
        dir.files.forEach { file ->
            println("${spacePrefixByLevel(level)}  - ${file.name}${file.ext?.let { ".$it" } ?: ""}  (file, size = ${file.size})")
        }

        dir.dirs.forEach {
            printDir(it, level + 1)
        }

    }

    fun calcDirSizes(dir: Dir, candidatesToDeletion: MutableList<Dir>, predicate: (Dir) -> Boolean = { false }): Long {
        val filesSize = dir.files.sumOf { it.size }
        dir.calculatedSize = if (dir.dirs.isEmpty()) {
            filesSize
        } else {
            filesSize + dir.dirs.sumOf { calcDirSizes(it, candidatesToDeletion, predicate) }
        }
        if (predicate(dir)) {
            candidatesToDeletion.add(dir)
        }
        return dir.calculatedSize
    }

    fun part1(input: List<String>): Long {
        val root = parseDirectories(input)

        val candidatesToDeletion = mutableListOf<Dir>()
        val totalSize = calcDirSizes(root, candidatesToDeletion) {
            it.calculatedSize <= 100000L
        }
        println("Total dir size = $totalSize")

        return candidatesToDeletion.sumOf { it.calculatedSize }
    }

    fun findCandidatesToDeletion(spaceToDelete: Long, root: Dir, candidatesToDeletion: MutableList<Long>) {

        if (root.calculatedSize >= spaceToDelete) {
            candidatesToDeletion.add(root.calculatedSize)
        }

        root.dirs.forEach {
            findCandidatesToDeletion(spaceToDelete, it, candidatesToDeletion)
        }
    }

    fun part2(input: List<String>): Long {
        val totalDiskSpace = 70000000L
        val spaceRequiredByUpdate = 30000000L

        val root = parseDirectories(input)
        val totalSize = calcDirSizes(root, mutableListOf())

        val unusedSpace = totalDiskSpace - totalSize
        val spaceToDelete = spaceRequiredByUpdate - unusedSpace

        val candidatesSizesToDelete = mutableListOf<Long>()
        findCandidatesToDeletion(spaceToDelete, root, candidatesSizesToDelete)

        return candidatesSizesToDelete.minOf { it }
    }

    val testInput = readInput("day07/input_test")
    val test1Result = part1(testInput)
    val test2Result = part2(testInput)

    println(test1Result)
    println(test2Result)

    check(test1Result == 95437L)
    check(test2Result == 24933642L)

    val input = readInput("day07/input")
    val part1 = part1(input)
    val part2 = part2(input)

    check(part1 == 1886043L)
    println(part1)
    println(part2)

}