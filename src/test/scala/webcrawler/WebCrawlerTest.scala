package webcrawler

import org.mockito.Mockito
import org.mockito.Mockito.{mock, verify, verifyNoMoreInteractions, when}
import org.scalatest.{FreeSpec, Matchers}

class WebCrawlerTest extends FreeSpec with Matchers {
  val webParser = mock(classOf[WebParser])
  val printer = mock(classOf[UrlPrinter])

  val urlParser = UrlParser()
  val domainParser = DomainParser


  "WebCrawler" - {

    "should recursevely match" - {
      val webCrawler = WebCrawler(webParser, urlParser, domainParser, printer)
      val domain: String = "http://google.com"

      val about = "/about"
      val privacy = "/privacy"
      val jobs = "/jobs"

      val nope= "https://nope.com"
      val foobars = "http://foobars.com"
      val external = "https://external.com"

      val firstPage = s"""<a href="$about"/> <a href="$privacy"/> <a href="$nope" """
      val aboutPage = s"""<a href="$jobs"/>""""
      val jobsPage = """<a href="""" + foobars + """"/>""""
      val privacyPage = """<a href="""" + external + """""""

      val firstPageUrls = List(about, privacy, nope)
      val aboutPageurls = List(jobs)
      val jobsPageUrls = List(foobars)
      val privacyPageUrls = List(external)

      when(webParser.visit(domain)).thenReturn(firstPage)
      when(webParser.visit(domain + about)).thenReturn(aboutPage)
      when(webParser.visit(domain + jobs)).thenReturn(jobsPage)
      when(webParser.visit(domain + privacy)).thenReturn(privacyPage)

      webCrawler.crawl(domain)

      verify(webParser).visit(domain)
      verify(webParser).visit(domain + about)
      verify(webParser).visit(domain + jobs)
      verify(webParser).visit(domain + privacy)

      verifyNoMoreInteractions(webParser)

      verifyRootAndUrlsPrinted(domain, firstPageUrls)
      verifyRootAndUrlsPrinted(domain + about, aboutPageurls)
      verifyRootAndUrlsPrinted(domain + jobs, jobsPageUrls)
      verifyRootAndUrlsPrinted(domain + privacy, privacyPageUrls)

      verifyNoMoreInteractions(printer)
    }

    "should not visit same link twice" in {
      Mockito.reset(webParser)
      Mockito.reset(printer)
      val webCrawler = WebCrawler(webParser, urlParser, domainParser, printer)

      val domain: String = "http://google.com"

      val about = "/about"

      val firstPage = s"""<a href="$about"/>"""
      val aboutPage = s"""<a href="$domain"/>""""

      val firstPageUrls = List(about)
      val aboutPageurls = List(domain)

      when(webParser.visit(domain)).thenReturn(firstPage)
      when(webParser.visit(domain + about)).thenReturn(aboutPage)

      webCrawler.crawl(domain)

      verify(webParser).visit(domain)
      verify(webParser).visit(domain + about)

      verifyRootAndUrlsPrinted(domain, firstPageUrls)
      verifyRootAndUrlsPrinted(domain + about, aboutPageurls)
    }
  }

  def verifyRootAndUrlsPrinted(googlecom: String, firstPageUrls: List[String]): Unit = {
    verify(printer).printRoot(googlecom)
    verify(printer).printUrls(firstPageUrls)
  }
}



