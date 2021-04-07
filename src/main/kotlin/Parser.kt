import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.File

class Parser {
    @Option(name="-d", usage = "Директория, в которой выполняется поиск")
    var dir: File? = null
    private set

    @Option(name="-r", usage = "Осуществлять рекурсивный поиск в дочерних директориях?")
    var recursive: Boolean = false
    private set

    @Argument(required = true, usage = "Имя файла, который необходимо найти")
    lateinit var fileName: String

    fun parse(args: Array<String>) {
        val parser = CmdLineParser(this)
        parser.parseArgument(args.toList())
    }
}

fun main(args: Array<String>) {
    val parser = Parser()
    parser.parse(args)

    // !isDirectory - не директория (включает в себя проверку на существование)
    if (parser.dir?.isDirectory == false) {
        error("Директории ${parser.dir} не существует!")
    }

    val finder = Finder(parser.recursive, parser.dir ?: File("").absoluteFile)

    val found = finder.find(parser.fileName)

    if (found.isEmpty()) {
        println("Файлов с именем ${parser.fileName} не найдено!")
    } else {
        println("Найденные файлы:")
        found.forEach { println("\t$it") }
    }
}