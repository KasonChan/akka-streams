package demo

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by kasonchan on 2/12/16.
  */
object Supervise extends App {

  val decider: Supervision.Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _ => Supervision.Restart
  }

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(decider))

  val source = Source(0 to 5).map(100 / _)
  val result = source.runWith(Sink.fold(0)(_ + _)).onComplete {
    case Success(s) => println(s)
      system.shutdown()
    case Failure(f) => println(f)
      system.shutdown()
  }

}
