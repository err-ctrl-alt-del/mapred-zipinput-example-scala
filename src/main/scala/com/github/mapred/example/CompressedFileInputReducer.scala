package com.github.mapred.example

import java.io.IOException
import java.lang

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

class CompressedFileInputReducer extends Reducer[Text, IntWritable, Text, IntWritable] {

  @throws[IOException]
  @throws[InterruptedException]
  override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text,
    IntWritable]#Context): Unit = {
    super.reduce(key, values, context)
  }

}
