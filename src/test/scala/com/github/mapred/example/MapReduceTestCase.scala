package com.github.mapred.example

import java.io.File

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit.{After, Before, Test}
import junit.framework.TestCase
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{LocalFileSystem, Path}
import com.cotdp.hadoop.ZipFileInputFormat
import com.google.common.io.Resources
import org.apache.hadoop.io.{BytesWritable, IntWritable, Text}
import org.apache.hadoop.mapreduce.{RecordReader, TaskAttemptID}
import org.apache.hadoop.mapreduce.lib.input.FileSplit
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.apache.hadoop.util.ReflectionUtils

class MapReduceTestCase extends TestCase {

  private val mapDriver: MapDriver[Text, BytesWritable, Text, IntWritable] =
    MapDriver.newMapDriver(new CompressedFileInputMapper)
  private val conf: Configuration = createConfiguration()
  private var inputFormat: ZipFileInputFormat = _
  private var reader: RecordReader[Text, BytesWritable] = _

  @Before
  @throws[Exception]
  override def setUp(): Unit = {
    mapDriver.clearInput()
    inputFormat = ReflectionUtils.newInstance(classOf[ZipFileInputFormat], conf)
  }

  def createConfiguration(): Configuration = {
    val conf = new Configuration(false)
    conf.set("fs.file.impl", classOf[LocalFileSystem].getName)
    conf.set("fs.defaultFS", "file:///")
    conf
  }

  @Test
  def testMapper() {
    val testFile = new File(Resources.getResource("test.zip").getFile)
    val path = new Path(testFile.getAbsoluteFile.toURI)
    val split = new FileSplit(path, 0, testFile.length, null)
    val inputFormat = ReflectionUtils.newInstance(classOf[ZipFileInputFormat], conf)
    val context = new TaskAttemptContextImpl(conf, new TaskAttemptID())
    reader = inputFormat.createRecordReader(split, context)
    reader.initialize(split, context)
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

  @After
  @throws[Exception]
  override def tearDown(): Unit = {
    reader.close()
  }

}
