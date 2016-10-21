package webcrawler

class UrlParser {
  def parse(content: String): Set[String] = {
    val validUrlCharacters =
    """A-Za-z0-9\_.\~\!\*\'\(\)\;\:\$\@\&\+\,\/\?\[\]\%\-"""
    val regex =
      s"""href\\s*=\\s*('([$validUrlCharacters]+)')?("([$validUrlCharacters]+)")?""".r
    val count = regex.findAllIn(content).matchData
    count.map(a => if (a.group(2) == null) a.group(4) else a.group(2)).filterNot(_ == null).toSet
  }
}

object UrlParser {
  def apply() = new UrlParser()
}
