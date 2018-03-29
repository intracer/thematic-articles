package org.wikipedia

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util
import java.util.{Calendar, GregorianCalendar}

import org.wikipedia.cache.CachedWiki

object ThematicArticlesBot {

  val TemplateName = "Скандинавська весна"
  val TemplateRegex = "(?s).*\\{\\{\\s*" + TemplateName + "[|](.*?)\\}}.*"

  val StartTime = getCalendar(2018, 2, 28, 20, 59, 59)
  val EndTime = getCalendar(2018, 3, 31, 20, 59, 59)

  def getCalendar(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): GregorianCalendar = {
    val time = new GregorianCalendar
    time.set(year, month, day, hour, minute, second)
    time
  }

  def getBot(host: String, login: String, password: Option[String] = None): Wiki = {
    val w = new WMFWiki(host) with CachedWiki
    w.setUserAgent("IlyaBot")
    password.foreach(w.login(login, _))
    w.setMarkBot(true)
    w.setMarkMinor(true)
    w
  }

  def getUserName(text: String): Option[String] = {
    if (text.matches(TemplateRegex)) Some(fixUserName(text.replaceAll(TemplateRegex, "$1").trim))
    else None
  }

  def getSizeAt(article: String, time: Calendar)(implicit w: Wiki): Int = {
    val revsBeforeStart = w.getPageHistory(article, null, time, false)
    revsBeforeStart.headOption.map(_.getSize).getOrElse(0)
  }

  def getPageId(title: String)(implicit w: Wiki): String = {
    val articleInfo = w.getPageInfo(title)
    articleInfo.get("pageid").toString
  }

  def fixUserName(name: String): String = {
    if (name.contains("|")) {
      name.split("\\|").last.replaceAll("\\]", "")
    } else name
  }

  def main(args: Array[String]): Unit = {
    implicit val w: Wiki = getBot("uk.wikipedia.org", "IlyaBot")

    val talkPages = w.whatTranscludesHere("Template:" + TemplateName, 1)

    val articleSet = new util.HashSet[ThematicArticle]
    for (talk <- talkPages) {
      val article = talk.substring(talk.indexOf(':') + 1)

      val id = getPageId(article)
      val initialSize = getSizeAt(article, StartTime)
      val size = getSizeAt(article, EndTime)

      val tpt = w.getPageText(talk)

      getUserName(tpt)
        .filter(w.userExists)
        .map { user =>
          val userId = w.getUser(user).getUserInfo.get("userid").asInstanceOf[String]
          articleSet.add(ThematicArticle(article, id, user, userId, size, initialSize, ""))
        }
    }
    val articles = articleSet.toArray(new Array[ThematicArticle](articleSet.size))
    val csv = makeCsv(articles).toString
    Files.write(Paths.get("file.csv"), csv.getBytes(StandardCharsets.UTF_8))
  }

  def makeCsv(articles: Seq[ThematicArticle])= {
    val seq = Seq("article", "user", "size") +: articles.map(a => Seq(a.title, a.user, a.size.toString))
    Csv.writeStringBuffer(Csv.addBom(seq))
  }


}
