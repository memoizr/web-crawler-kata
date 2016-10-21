package webcrawler

class WebParser {
  def visit(url: String): String = {
    import scala.io.Source
    val html = Source.fromURL(url)
    val s = html.mkString
    s
  }
}
