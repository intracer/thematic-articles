import org.specs2.mutable.Specification
import org.wikipedia.ThematicArticlesBot

class UserSpec extends Specification{

  "bot" should {
    "getUser" in {
      ThematicArticlesBot.getUserName("{{Пишемо про інформаційну безпеку|[[Користувач:The Iron Addict]] }}") === Some("The Iron Addict")
    }
  }

}
