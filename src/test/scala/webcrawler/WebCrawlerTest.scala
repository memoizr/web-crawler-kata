package webcrawler

import org.mockito.Mockito
import org.mockito.Mockito._
import org.mockito.verification.VerificationMode
import org.scalatest.{FreeSpec, Matchers}

class WebCrawlerTest extends FreeSpec with Matchers {
  val webParser = mock(classOf[WebParser])
  val printer = mock(classOf[UrlPrinter])

  val urlParser = UrlParser()


  "WebCrawler" - {

    "should recursevely match" - {
      val webCrawler = WebCrawler(webParser, urlParser, printer)
      val domain: String = "http://google.com"

      val about = "/about"
      val privacy = "/privacy"
      val jobs = domain + "/jobs"

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
      when(webParser.visit(jobs)).thenReturn(jobsPage)
      when(webParser.visit(domain + privacy)).thenReturn(privacyPage)

      webCrawler.crawl(domain)

      verify(webParser).visit(domain)
      verify(webParser).visit(domain + about)
      verify(webParser).visit(jobs)
      verify(webParser).visit(domain + privacy)

      verifyNoMoreInteractions(webParser)

      verifyRootAndUrlsPrinted(domain, firstPageUrls)
      verifyRootAndUrlsPrinted(domain + about, aboutPageurls)
      verifyRootAndUrlsPrinted(jobs, jobsPageUrls)
      verifyRootAndUrlsPrinted(domain + privacy, privacyPageUrls)

      verifyNoMoreInteractions(printer)
    }

    "should not visit same link twice" in {
      Mockito.reset(webParser)
      Mockito.reset(printer)
      val webCrawler = WebCrawler(webParser, urlParser, printer)

      val domain: String = "http://google.com"

      val about = "/about"
      val privacy = "/privacy"

      val firstPage = s"""<a href="$about"/><a href="$privacy"/>"""
      val aboutPage = s"""<a href="$domain"/>""""
      val privacyPage = s"""<a href="$domain"/>""""

      val firstPageUrls = List(about, privacy)
      val aboutPageUrls = List(domain)
      val privacyPageUrls = List(domain)

      when(webParser.visit(domain)).thenReturn(firstPage)
      when(webParser.visit(domain + about)).thenReturn(aboutPage)
      when(webParser.visit(domain + privacy)).thenReturn(privacyPage)

      webCrawler.crawl(domain)

      verify(webParser).visit(domain)
      verify(webParser).visit(domain + about)
      verify(webParser).visit(domain + privacy)

      verifyNoMoreInteractions(webParser)

      verify(printer).printRoot(domain)
      verify(printer).printUrls(firstPageUrls)

      verify(printer).printRoot(domain + about)
      verify(printer).printRoot(domain + privacy)

      verify(printer, times(2)).printUrls(privacyPageUrls)

      verifyNoMoreInteractions(printer)
    }
  }

  def verifyRootAndUrlsPrinted(url: String, urls: List[String], verificationMode: VerificationMode = times(1)): Unit = {
    verify(printer, verificationMode).printRoot(url)
    verify(printer, verificationMode).printUrls(urls)
  }
}



