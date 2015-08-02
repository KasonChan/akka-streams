package demo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by ka-son on 7/26/15.
 */
object Sum {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()

    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)

    val sum1 = source.runWith(sink)
    sum1.foreach(println)
    sum1.map(s => println(s))

    // Connect the Source to the Sink, obtaining a RunnableGraph
    val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

    // Materialize the flow and get the value of the FoldSink
    val sum2: Future[Int] = runnable.run()

    sum2.onComplete {
      case Success(s) =>
        println(s)
        system.shutdown()
      case Failure(f) =>
        println(f)
        system.shutdown()
    }

  }

}
