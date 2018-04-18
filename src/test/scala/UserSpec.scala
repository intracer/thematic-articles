import org.specs2.mutable.Specification
import org.wikipedia.ThematicArticlesBot

class UserSpec extends Specification {

  "bot" should {
    "getUser" in {
      ThematicArticlesBot.getUserName("{{Пишемо про інформаційну безпеку|[[Користувач:The Iron Addict]] }}") === Some("The Iron Addict")

      val signature = "{{Пишемо про інформаційну безпеку|" +
      "--[[User: Учитель|" +
      """'''<font face="chiller" color="#CD1076" style="text-shadow:grey 0.2em 0.2em 0.2em; font-size:115%">Учитель</font>''']]"""+
        """<sup>[[User talk: Учитель|<font size="1" style="text-shadow:grey 0.2em 0.2em 0.1em;">Обг</font>]]</sup> 21:02, 7 квітня 2018 (UTC)}}"""

      ThematicArticlesBot.getUserName(signature) === Some("Учитель")

      ThematicArticlesBot.getUserName("{{Пишемо про інформаційну безпеку|{{u|Евген Савич}}}}") === Some("Евген Савич")
    }
  }

}
