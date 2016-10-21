package webcrawler

import java.io.PrintStream

import org.mockito.Mockito._
import org.scalatest.{FreeSpec, Matchers}

class UrlPrinterTest extends FreeSpec with Matchers {
  "A UrlPrinter" - {
    val out = mock(classOf[PrintStream])
    val printer = new UrlPrinter(out)
    "should print" - {
      "the root" in {
        printer.printRoot("hello")

        verify(out).println("Visiting root: hello")
      }

      "the urls" in {
        val urls = List("/one", "/two")
        printer.printUrls(urls)

        verify(out).println("|_ /one")
        verify(out).println("|_ /two")
      }
    }
  }
}
