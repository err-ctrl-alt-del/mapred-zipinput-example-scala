package com.github.mapred.example

import java.io.IOException

import org.apache.hadoop.io.{BytesWritable, IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class CompressedFileInputMapper extends Mapper[Text, BytesWritable, Text, IntWritable] {

  @throws[IOException]
  @throws[InterruptedException]
  override def map(key: Text, value: BytesWritable,
                   context: Mapper[Text, BytesWritable, Text, IntWritable]#Context): Unit = {
    super.map(key, new BytesWritable(), context)
  }

}
