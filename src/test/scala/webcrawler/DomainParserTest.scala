package webcrawler

import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class DomainParserTest extends FreeSpec with TableDrivenPropertyChecks with Matchers {

  "DomainParser" - {
    "when parsing a url" - {
      "should return the domain when present" in {
        val table = Table(
          ("input", "output"),
          ("http://google.com", "google.com"),
          ("http://google.com/", "google.com"),
          ("http://google.com/about", "google.com"),
          ("http://maps.google.com/about", "maps.google.com"),
          ("http://www.google.com/about", "google.com"),
          ("//google.com/about", "google.com"),
          ("https://google.com", "google.com")
        )

        forAll(table) { (input, output) =>
          DomainParser.parse(input).get shouldBe output
        }
      }

      "should return none when not present" in {
        val table = Table(
          ("input", "output"),
          ("/about/me", None)
        )

        forAll(table) { (input, output) =>
          DomainParser.parse(input) shouldBe None
        }
      }
    }
  }
}
