import java.io.{BufferedWriter, File, FileWriter}

import scala.collection.mutable

object CSVDemo extends App {

  def merge(baseDir: String, counterName: String, files: mutable.ListBuffer[String]): Unit = {
    var mergedFile = baseDir + "\\" + counterName + "_merged.csv"
    var headerExist = false;
    val bufferedFileWriter = new BufferedWriter(new FileWriter(mergedFile))
    files.toList.foreach(filePath => {
      var isHeader = true
      for (line <- io.Source.fromFile(filePath).getLines) {
        if (!headerExist) {
          val cols = line.split(";").map(_.trim)
          cols.foreach(col => {
            bufferedFileWriter.write(col)
            bufferedFileWriter.write(",")
          })
        } else if (headerExist && !isHeader) {
          val cols = line.split(";").map(_.trim)
          cols.foreach(col => {
            bufferedFileWriter.write(col)
            bufferedFileWriter.write(",")
          })
        }
        headerExist = true
        bufferedFileWriter.write(System.getProperty("line.separator"))
        isHeader = false
      }
    })
    bufferedFileWriter.close()
  }

  def parse(baseDir: String): Unit = {
    if (Option(baseDir).getOrElse("").isEmpty) {
      print("Base dir path is empty.")
    } else {
      val folder = new File(baseDir)
      var counterMap = new mutable.HashMap[String, mutable.ListBuffer[String]]
      if (folder.exists && folder.isDirectory) {
        folder.listFiles
          .toList
          .foreach(file => {
            file.listFiles
              .toList
              .foreach(file => {
                var filePath = file.getAbsolutePath
                var filename = file.getName
                var key = filename.substring(0, filename.lastIndexOf("-"))
                addFile(counterMap, key, filePath)
              })
          })
      }

      for ((counter, files) <- counterMap) {
           merge(baseDir, counter, files)
      }

      print("End")
    }
  }

  def addFile(map: collection.mutable.Map[String, mutable.ListBuffer[String]], key: String, file: String): Unit = {
    var list = map.get(key)
    list match {
      case Some(list) => {
        list += file
        map.put(key, (list))
      }
      case None => {
        print("None")
        list = Some(mutable.ListBuffer(file))
        map.put(key, list.get)
      }
    }
  }

  //Base folder
  var baseDir = "C:\\Users\\Marquinho\\Desktop\\UnifiedCharging\\Tickets\\RKPRD-4008\\dumpconfig_IPD_201805281128\\blrocsipd101\\advdump.blrocsipd101\\AdvDumpStat.blrocsipd101\\01-blrocsipd101"
  //var baseDir = args(0)
  //print("Base dir: " + args(0))
  parse(baseDir)
}