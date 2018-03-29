package org.wikipedia.cache

import java.io.File

import contest.Wiki
import net.openhft.chronicle.map.ChronicleMap

trait CachedWiki  {
  self: Wiki =>

  val cacheFile = new File("wiki_cache")

  val cache = ChronicleMap
    .of(classOf[String], classOf[String])
    .name("wiki_cache")
    .entries(50000)
    .averageKeySize(512)
    .averageValueSize(32768)
    .createPersistedTo(cacheFile)

  abstract override def fetch(url: String, caller: String): String = {
    Option(cache.get(url)).getOrElse {
      val response = self.fetch(url, caller)
      cache.put(url, response)
      response
    }
  }
}
