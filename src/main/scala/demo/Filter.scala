package demo

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}

/**
 * Created by ka-son on 8/2/15.
 */
object Filter extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 3)
  val flow = Flow[Int].map(_ + 10).filter(_ % 2 == 0)
  val sink = Sink.foreach(println)
  source.via(flow).runWith(sink)

}
