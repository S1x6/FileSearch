package search

import java.io.File
import java.io.FileInputStream
import kotlin.math.max

class FileSubstringSearch(private val file: File) {

    fun find(string: String) {
        if (file.isFile) {
            findInFile(string, file, null)
        } else if (file.isDirectory) {
            val shiftsTable = makeShiftTable(string)
            file.walkTopDown().forEach { f ->
                if (f.isFile) {
                    findInFile(string, f, shiftsTable)
                }
            }
        }
    }

    private fun findInFile(string: String, f: File, shiftsTable: IntArray?) {
        val fis = FileInputStream(f)
        val frameSize = max(string.length * 2 + 1, 131072) // 128 kB
        val buffer = ByteArray(frameSize)
        val previousBuffer = ByteArray(frameSize)
        val shiftTable = shiftsTable ?: makeShiftTable(string)
        val templateLen = string.length
        var i = templateLen - 1
        var j: Int
        var k: Int
        var readBytes = fis.read(buffer)
        var globalIndex = i
        while (readBytes > 0) {
            j = templateLen - 1
            k = i
            while (j >= 0) {
                if (k >= 0) {
                    if (buffer[k] == string[j].toByte()) {
                        k--
                        j--
                    } else {
                        break
                    }
                } else if (previousBuffer[previousBuffer.size + k] == string[j].toByte()) {
                    k--
                    j--
                } else {
                    break
                }
            }
            if (j < 0) {
                println("File: ${file.name}${f.absolutePath.removePrefix(file.absolutePath)}; At: ${globalIndex - templateLen + 1}")
            }
            if (buffer[i].toInt() >= 0) {
                globalIndex += shiftTable[buffer[i].toInt()]
                i += shiftTable[buffer[i].toInt()]
            } else {
                globalIndex += shiftTable[templateLen]
                i += shiftTable[templateLen]
            }
            if (i >= readBytes) {
                buffer.copyInto(previousBuffer)
                readBytes = fis.read(buffer)
                i -= buffer.size
            }
        }
    }

    private fun makeShiftTable(string: String): IntArray {
        val bytes = IntArray(256)
        val stringBytes = string.toByteArray()
        for (i in 0 until 256) {
            bytes[i] = stringBytes.size
        }

        for (i in 0 until stringBytes.size - 1) {
            bytes[stringBytes[i].toInt()] = stringBytes.size - i - 1
        }
        return bytes
    }

}