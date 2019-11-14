package search

import java.io.File

class FileSearch(private val file: File) {

    fun find(name: String) {
        file.walkTopDown().forEach { f ->
            if (f.name.contains(name)) {
                if (f.isDirectory) {
                    print("dir: ")
                }
                println("${file.name}${f.absolutePath.removePrefix(file.absolutePath)}")
            }
        }
    }

}