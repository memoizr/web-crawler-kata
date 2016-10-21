package webcrawler

object WebCrawlerApp extends App{
  val printer = new UrlPrinter(System.out)
  val webParser = new WebParser()
  val crawler = WebCrawler(webParser, UrlParser(), DomainParser, printer)
//  crawler.crawl("https://en.wikipedia.org/wiki/Main_Page")
  crawler.crawl("http://scalatest.org")
}
