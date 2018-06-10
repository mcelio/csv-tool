import java.io.{BufferedWriter, File, FileWriter}

import scala.collection.mutable

object FailedTransactionParser  extends App {


  def parse(baseDir: String): Unit = {
    var mergedFile = baseDir + "\\merged_0.csv"
    if (Option(baseDir).getOrElse("").isEmpty) {
      print("Base dir path is empty.")
    } else {
      val folder = new File(baseDir)
      var headerExist = false;
      var fileIndex = 0
      var bufferedFileWriter = new BufferedWriter(new FileWriter(mergedFile))
      if (folder.exists && folder.isDirectory) {
        folder.listFiles
          .toList
          .foreach(file => {
            var filename = file.getName
            if (filename.startsWith("failedtransactions_")) {
              var filePath = file.getAbsolutePath
              for (line <- io.Source.fromFile(filePath).getLines) {
                if (headerExist) {
                  val cols = line.split("\t").map(_.trim)
                  cols.foreach(col => {
                    bufferedFileWriter.write(col.trim)
                    bufferedFileWriter.write(",")
                  })
                  bufferedFileWriter.write(System.getProperty("line.separator"))
                }
                headerExist = true
              }

              if(io.Source.fromFile(mergedFile).length > 100000000){
                fileIndex += 1
                bufferedFileWriter.close()
                mergedFile = baseDir + "\\merged_" + fileIndex + ".csv"
                bufferedFileWriter = new BufferedWriter(new FileWriter(mergedFile))
                headerExist = false
              }
            }
          })
      }
      bufferedFileWriter.close()
      print("End")
    }
  }

  var baseDir = "C:\\eclipse\\ipd_analysis\\ipd-analysis\\AdvDumpLogs.blrocsipd101\\advdata\\log\\arm\\failedtransactionslog";

  parse(baseDir)



}
