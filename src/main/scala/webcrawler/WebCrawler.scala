package webcrawler

import java.net.{URI, URL}

import scala.collection.mutable

class WebCrawler(webParser: WebParser, urlParser: UrlParser, domainParser: DomainParser.type, printer: UrlPrinter) {
  val visitedUrls = mutable.Set[String]()

  def crawl(address: String): Unit = {
    if (!visitedUrls.contains(address)) {

      visitedUrls.add(address)
      printer.printRoot(address)

      val currentPage = webParser.visit(address)
      val urls = urlParser.parse(currentPage).toList

      printer.printUrls(urls)

      visitAllLinksToCurrentDomain(address, urls)
    }
  }

  private def visitAllLinksToCurrentDomain(address: String, urls: List[String]): Unit = {
    val host = new URL(address).getHost
    urls.filter(isSameDomain(host, _))
      .map(url => if (url.contains(host)) url else address + url)
      .filterNot(visitedUrls.contains).foreach(crawl)
  }

  private def isSameDomain(host: String, url: String): Boolean = {
    Option(new URI(url).getHost).forall(_.contains(host))
  }
}


object WebCrawler {
  def apply(webParser: WebParser, urlParser: UrlParser, domainParser: DomainParser.type, printer: UrlPrinter): WebCrawler =
    new WebCrawler(webParser, urlParser, domainParser, printer)
}
