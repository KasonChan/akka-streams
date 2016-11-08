package demo

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * Created by kasonchan on 2/12/16.
  */
object FlowGraph extends App {
  val decider: Supervision.Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _ => Supervision.Restart
  }

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(decider))

  val pairs = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    // prepare graph elements
    val zip = b.add(Zip[Int, Int]())
    def ints = Source.fromIterator(() => Iterator.from(1))

    // connect the graph
    ints.filter(_ % 2 != 0) ~> zip.in0
    ints.filter(_ % 2 == 0) ~> zip.in1

    // expose port
    SourceShape(zip.out)
  })

  pairs.runWith(Sink.head).onComplete {
    case Success(s) => println(s)
    case Failure(f) => println(f)
  }

  val num = 2

  val source = Source.single(num)

  val flow = Flow[Int].map(_ + 10)

  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink)

  system.shutdown()

}
