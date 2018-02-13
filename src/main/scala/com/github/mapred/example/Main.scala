package com.github.mapred.example

import com.cotdp.hadoop.ZipFileInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

object Main {

  @throws[Exception]
  def main(args: Array[String]) : Unit = {
    val conf = new Configuration
    val job = Job.getInstance(conf)
    job.setJobName("ZipFileMapOnlyJob")
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])
    job.setMapperClass(classOf[CompressedFileInputMapper])
    job.setCombinerClass(classOf[CompressedFileInputReducer])
    job.setReducerClass(classOf[CompressedFileInputReducer])
    job.setNumReduceTasks(0)
    job.setInputFormatClass(classOf[ZipFileInputFormat])
    for (arg <- args) {
      if (arg == args(args.length - 1)) {
        FileOutputFormat.setOutputPath(job, new Path(arg))
      }
      else {
        MultipleInputs.addInputPath(job, new Path(arg), classOf[ZipFileInputFormat])
      }
    }
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }

}