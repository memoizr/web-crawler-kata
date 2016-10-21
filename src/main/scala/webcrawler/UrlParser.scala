package webcrawler

class UrlParser {
  val validUrlCharacters = """A-Za-z0-9\_.\~\!\*\'\(\)\;\:\$\@\&\+\,\/\?\[\]\%\-"""
  val regex = s"""href\\s*=\\s*('([$validUrlCharacters]+)')?("([$validUrlCharacters]+)")?""".r

  def parse(content: String): Set[String] = {
    val count = regex.findAllIn(content).matchData
    count.map(a => if (a.group(2) == null) a.group(4) else a.group(2))
      .filterNot(_ == null)
      .map(a => {
        if (a.startsWith("http") || a.startsWith("/")) {
          removeTrailingSlash(a)
        } else "/" + a
      })
      .toSet
  }

  def removeTrailingSlash(a: String): String = {
    if (a.endsWith("/")) a.dropRight(1) else a
  }
}

object UrlParser {
  def apply() = new UrlParser()
}
