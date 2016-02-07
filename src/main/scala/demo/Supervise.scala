package demo

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by ka-son on 8/2/15.
  */
object Supervise extends App {

  implicit val system = ActorSystem("system")

  val decider: Supervision.Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _ => Supervision.Stop
  }

  // Create materializer with a custom supervision strategy
  implicit val mat = ActorMaterializer(
    ActorMaterializerSettings(system).withSupervisionStrategy(decider))

  val source = Source(0 to 5).map(100 / _)
  val result = source.runWith(Sink.fold(0)(_ + _))

  val res = Await.result(result, Duration.Inf)
  println(res)
  result.map(r => println(r))
  system.shutdown()

}
