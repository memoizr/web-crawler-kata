package webcrawler

class WebCrawler(webParser: WebParser, urlParser: UrlParser, domainParser: DomainParser.type, printer: UrlPrinter) {
  def crawl(address: String): Unit = {
    printer.printRoot(address)

    val currentPage = webParser.visit(address)

    val urls = urlParser.parse(currentPage).toList
    printer.printUrls(urls)

    visitAllLinksToCurrentDomain(address, urls)
  }

  def visitAllLinksToCurrentDomain(address: String, urls: List[String]): Unit = {
    val currentDomain = "http://" + domainParser.parse(address).get
    urls.filter(isSameDomain(address, _)).map(currentDomain + _).foreach(crawl)
  }

  def isSameDomain(currentUrl: String, url: String): Boolean = {
    val currentDomain = domainParser.parse(currentUrl)
    val targetDomain = domainParser.parse(url)

    targetDomain.flatMap(x => currentDomain.map(_.contains(x))).getOrElse(true)
  }
}


object WebCrawler {
  def apply(webParser: WebParser, urlParser: UrlParser, domainParser: DomainParser.type, printer: UrlPrinter): WebCrawler =
    new WebCrawler(webParser, urlParser, domainParser, printer)
}
