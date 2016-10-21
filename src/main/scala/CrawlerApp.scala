import webcrawler._

object CrawlerApp extends App{
  override def main(args: Array[String]): Unit = {
    val webParser = new WebParser()
    val urlParser = new UrlParser()
    val printer = new UrlPrinter(System.out)
    val crawler = WebCrawler(webParser, urlParser, printer)

    crawler.crawl(args(0))
  }
}
