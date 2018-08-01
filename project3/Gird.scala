import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

def gridID(x:Int, y:Int):Int={
  return (x/20+1+20*(500-y/20-1))
}

def Nb(x:Int):List[Int]={
    if (x == 1)
        List(2,501,502)
    else if (x == 500)
        List(499,999,1000)
    else if (x == 249501)
        List(249001,249002,249502)
    else if (x ==250000)
        List(249499, 249500, 249999)
    else if ( x > 1 & x < 500)
        List(x-1, x+1, x+500, x+500-1, x+500+1)
    else if ( x > 249501 & x < 250000)
        List(x-1, x+1, x-500, x-500-1, x-500+1)
    else if ( x%500 == 1 )
        List(x+500, x+500+1, x+1, x-500, x-500+1)
    else if ( x%500 == 0 )
        List(x+500, x+500-1, x-1, x-500, x-500-1)
    else
        List(x-500-1, x-500, x-500+1, x-1, x+1, x+500-1, x+500, x+500+1)
}




val input = sc.textFile("hdfs://User/xc/proj3/data.txt")
val grids = input.map(x => (gridID(x.split(" ")(0).toInt, x.split(" ")(1).toInt)))
val gCounts=grids.map(p=>(p,1)).reduceByKey((x,y)=>x+y)
val gCkey = sc.broadcast(gCounts.collectAsMap) 
val gg=gCounts.map(x=>(x._1, x._1))
val gNID= gg.flatMapValues(x=>Nb(x))
val IDgN= gNID.map(_.swap)
val jg=IDgN.join(gCounts)





val gNgC=jg.values.sortByKey()


val gNMean= gNgC.combineByKey(
(v) => (v, 1), (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1), (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2) ).map{ case (key, value) => (key, value._1 / value._2.toFloat) } 

val gNMeankey = sc.broadcast(gNMean.collectAsMap)

val gCgNC=gCounts.join(gNMean)
val result1 = gCgNC.map(x=>(x,(x._2._1/x._2._2)))




def index(id:Int):Double={
    return gCkey.value(id)/r3key.value(id)
}

val mm = r3.map(x=>(x._1,index(x._1))).sortBy(-_._2)
mm.take(50)





val result2 = sc.broadcast(mm.collectAsMap)

def topN(x:Int): (List[Int], List[Double]) = {
    val n=Nb(x)
	var id:List[Int] = List()
	var index:List[Double] = List()
	for (y <- n) {id = id:::List(y)
		index =index:::List(result2.value(y))
		}
		return (id,index)}

val fin=mm.map(x=>(x._1,topN(x._1)))
fin.take(50)


