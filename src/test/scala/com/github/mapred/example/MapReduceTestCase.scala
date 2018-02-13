package com.github.mapred.example

import java.io.File

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit.{Before, Test}
import junit.framework.TestCase
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{LocalFileSystem, Path}
import org.apache.hadoop.mrunit.mapreduce.{MapDriver, MapReduceDriver, ReduceDriver}
import com.cotdp.hadoop.ZipFileInputFormat
import com.google.common.io.Resources
import org.apache.hadoop.mapreduce.TaskAttemptID
import org.apache.hadoop.mapreduce.lib.input.FileSplit
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl
import org.apache.hadoop.util.ReflectionUtils

class MapReduceTestCase extends TestCase {

  private val mapper = new CompressedFileInputMapper
  private val reducer = new CompressedFileInputReducer

  val mapDriver = MapDriver.newMapDriver(mapper)
  val reduceDriver = ReduceDriver.newReduceDriver(reducer)
  val mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer)

  @Before
  @throws[Exception]
  override def setUp(): Unit = {}

  @Test
  def testMapper() {
    val conf = new Configuration(false)
    conf.set("fs.file.impl", classOf[LocalFileSystem].getName)
    conf.set("fs.defaultFS", "file:///")
    val testFile = new File(Resources.getResource("test.zip").getFile())
    val path = new Path(testFile.getAbsoluteFile.toURI)
    val split = new FileSplit(path, 0, testFile.length, null)
    val inputFormat = ReflectionUtils.newInstance(classOf[ZipFileInputFormat], conf)
    val context = new TaskAttemptContextImpl(conf, new TaskAttemptID())
    val reader = inputFormat.createRecordReader(split, context)
    reader.initialize(split, context)
    mapDriver.setMapper(new CompressedFileInputMapper)
    while ( {
      reader.nextKeyValue
    }) {
      mapDriver.withInput(reader.getCurrentKey, reader.getCurrentValue)
    }
    val result = mapDriver.run
    assertNotNull(result)
    assertTrue(result.size() == 1)
    assertThat(result.toString, containsString("test.csv"))
  }

}
