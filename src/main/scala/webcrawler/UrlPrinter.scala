package webcrawler

import java.io.PrintStream

class UrlPrinter(printStream: PrintStream) {
  def printRoot(rootUrl: String): Unit = printStream.println(s"Visiting root: $rootUrl")

  def printUrls(urls: List[String]): Unit = urls.foreach { url =>
    printStream.println(s"|_ $url")
  }
}
