//spark-shell --conf spark.jars.ivySettings=ivysettings.xml --packages com.aamend.spark:spark-governance:1.0-SNAPSHOT

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.SparkSession
import com.aamend.spark.ml._

val spark = SparkSession.builder().getOrCreate()
val training = spark.createDataFrame(Seq((0L, "spark", 1.0), (1L, "jenkins", 0.0), (2L, "nexus", 1.0), (3L, "hadoop", 0.0))).toDF("id", "text", "label")
val tokenizer = new Tokenizer().setInputCol("text").setOutputCol("words")
val hashingTF = new HashingTF().setNumFeatures(1000).setInputCol(tokenizer.getOutputCol).setOutputCol("features")
val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.001)
val pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, lr))
val model = pipeline.fit(training)
val artifact = model.deploy("com.aamend.spark:hello-world:1.0")
println(artifact)
