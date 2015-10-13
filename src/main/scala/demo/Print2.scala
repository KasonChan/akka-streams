package demo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
 * Created by kasonchan on 10/13/15.
 */
object Print2 extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source = Source(Seq(1, 2, 3))
  val flow = Flow[Int].map(_ * 2)
  val sink = Sink.foreach(println)

  source.runForeach(println).onComplete(_ => println("Finished printing 1st source"))

  (source via flow via flow to sink).run()

  (source via flow via flow).runForeach(println).onComplete(_ => system.shutdown())

}
