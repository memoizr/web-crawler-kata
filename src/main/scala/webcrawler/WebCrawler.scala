package webcrawler

import scala.collection.mutable

class WebCrawler(webParser: WebParser, urlParser: UrlParser, domainParser: DomainParser.type, printer: UrlPrinter) {
  val set = mutable.Set[String]()

  def crawl(address: String): Unit = {
    set add address

    printer.printRoot(address)

    val currentPage = webParser.visit(address)

    val urls = urlParser.parse(currentPage).toList
    printer.printUrls(urls)

    visitAllLinksToCurrentDomain(address, urls)
  }

  def visitAllLinksToCurrentDomain(address: String, urls: List[String]): Unit = {
    val domain = domainParser.parse(address).get
    val currentDomain = "http://" + domain
    urls.filter(isSameDomain(address, _)).map(x=> if (x.contains(domain)) x else currentDomain + x).filterNot(set.contains).foreach(crawl)
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
