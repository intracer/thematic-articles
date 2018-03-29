package org.wikipedia

import java.io.{StringWriter, Writer}

import com.github.tototoshi.csv.CSVWriter

object Csv {

  def writeStringBuffer(data: Seq[Seq[Any]]): StringBuffer = {
    val writer = new StringWriter()
    write(data, writer)
    writer.getBuffer
  }

  def writeRow(data: Seq[Any]): String = {
    val writer = new StringWriter()
    val csv = CSVWriter.open(writer)
    csv.writeRow(data)
    csv.close()
    writer.getBuffer.toString
  }

  def write(data: Seq[Seq[Any]], writer: Writer): Unit = {
    val csv = CSVWriter.open(writer)
    csv.writeAll(data)
    csv.close()
  }

  def addBom(data: Seq[Seq[String]]): Seq[Seq[String]] = {
    val BOM = "\ufeff"
    val header = data.head
    val headerWithBom = (BOM + header.head) +: header.tail
    headerWithBom +: data.tail
  }
}
