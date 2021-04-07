import java.io.File

class Finder(private val recursive: Boolean, private val directory: File) {

    fun find(fileName: String): List<File> {
        val foundFiles = mutableListOf<File>()
        if (recursive) {
            directory.walkTopDown().forEach { file ->
                if (file.isFile && file.name == fileName) {
                    foundFiles.add(file)
                }
            }
        } else {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && file.name == fileName) {
                    foundFiles.add(file)
                }
            }
        }

        return foundFiles
    }
}


