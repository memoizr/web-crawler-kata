package webcrawler

object DomainParser {
  def parse(url: String) = {
    val regex = """(?:https?:)?//(?:www\.)?([^/]*)/?""".r
    regex.findFirstMatchIn(url).map(_.group(1))
  }
}
