package webcrawler

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class HrefParserTest extends FreeSpec with TableDrivenPropertyChecks with Matchers {

  "A parser" - {
    val parser = UrlParser()
    "when parsing a string with" - {

      "no hrefs" - {
        val validHtmlWithNoTags = "<html></html>"
        val invalidHtml = "lsdkhfasldkhfsadhf<>J!!!"

        "should return an empty set" in {
          parser.parse(validHtmlWithNoTags) shouldBe empty
          parser.parse(invalidHtml) shouldBe empty
        }
      }

      "a single href value" - {

        val fancyUrl = """http://AaZz123456789'0_.~!*();:@&+$,/?$[%-].com"""
        val table = Table(
          ("input", "output"),
          ("""<a href='http://myurl.com'>>""", Set("http://myurl.com")),
          ("""<a href='http://myurl12.com'>>""", Set("http://myurl12.com")),
          ("""<a href="http://myurl.com">>""", Set("http://myurl.com")),
          (s"""<a href="$fancyUrl">>""", Set(fancyUrl)),
          ("""fhsdlf <buya! href="http://myurl.com"!! >xxcd//!**)(*>""", Set("http://myurl.com")),
          ("""<a href  =   "http://myurl.com">>""", Set("http://myurl.com"))
        )

        "should return that value" in {
          forAll(table) { (input, output) =>
            parser.parse(input) shouldBe output
          }
        }
      }

      "multiple href values" - {
        val table = Table(
          ("input", "output"),
          ("""<a href='http://myurl.com'>><boo href = 'baz' """, Set("/baz", "http://myurl.com")),
          ("""<a href='http://myurl.com'>><boo href = '/baz' """, Set("/baz", "http://myurl.com")),
          ("""<a href='http://myurl.com'>><boo href = ' /baz '><><href ="/baz">//>> """, Set("/baz", "http://myurl.com")),
          ("""<a href='http://myurl.com'>><boo href ="/baz" """, Set("/baz", "http://myurl.com"))
        )

        "should return those unique values" in {
          forAll(table) { (input, output) =>
            parser.parse(input) shouldBe output
          }
        }
      }
    }
  }
}
