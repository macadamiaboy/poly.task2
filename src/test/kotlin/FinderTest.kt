import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FinderTest {

    private fun createFile(path: String, fileName: String): File = File(path, fileName).apply {
        this.createNewFile()
    }

    private fun createDirectory(path: String, fileName: String): File = File(path, fileName).apply {
        this.mkdir()
    }

    private fun assertFiles(expected: Collection<File>, actual: Collection<File>) {
        assertEquals(expected.size, actual.size, "Количество файлов ожидалось: ${expected.size}, количество файлов получено: ${actual.size}")
        for (file in expected) {
            assertNotNull(actual.find { it.toString() == file.toString() }, "Файл $file не найден")
        }
    }


    @Test
    fun testAll() {

        val rootDir = createDirectory("", "root")
        val innerDir = createDirectory(rootDir.path, "inner")


        val rootFile = createFile(rootDir.path, "file")
        val innerFile = createFile(innerDir.path, "file")
        val innerAnotherFile = createFile(innerDir.path, "another")

        val rootFinder = Finder(false, rootDir)
        assertFiles(rootFinder.find(innerAnotherFile.name), listOf())
        assertFiles(rootFinder.find(rootFile.name), listOf(rootFile))
        // несуществующий файл
        assertFiles(rootFinder.find("randomkgktkgtkg"), listOf())

        val recursiveFinder = Finder(true, rootDir)
        assertFiles(recursiveFinder.find(innerAnotherFile.name), listOf(innerAnotherFile))
        assertFiles(recursiveFinder.find(rootFile.name), listOf(rootFile, innerFile))
        assertFiles(recursiveFinder.find(innerFile.name), listOf(rootFile, innerFile))
        // несуществующий файл
        assertFiles(recursiveFinder.find("nothingfgkkg"), listOf())

        val innerFinder = Finder(false, innerDir)
        assertFiles(innerFinder.find(innerAnotherFile.name), listOf(innerAnotherFile))
        assertFiles(innerFinder.find(innerFile.name), listOf(innerFile))
        // несуществующий файл
        assertFiles(innerFinder.find("nothingfgkkg"), listOf())

        innerAnotherFile.delete()
        innerFile.delete()
        innerDir.delete()
        rootFile.delete()
        rootDir.delete()
    }
}