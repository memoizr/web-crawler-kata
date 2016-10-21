import webcrawler._

object Crawler extends App{
  override def main(args: Array[String]): Unit = {
    println("hahah")
    val webParser = new WebParser()
    val urlParser = new UrlParser()
    val domainParser = DomainParser
    val printer = new UrlPrinter(System.out)
    val crawler = WebCrawler(webParser, urlParser, domainParser, printer)

    crawler.crawl(args(0))
  }
}
