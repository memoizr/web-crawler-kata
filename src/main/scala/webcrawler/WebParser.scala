package webcrawler

import scala.io.Source.fromURL

class WebParser {
  def visit(url: String): String = fromURL(url).mkString
}
