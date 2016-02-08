package demo

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by kasonchan on 2/8/16.
  */
object BackPressure extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source = Source(List("fastProducer", "slowSubscribe"))
  val fastPublisher = Flow[String].map(_ + " via fastProducer")
  val slowSubscriber = Flow[String].map { x => Thread.sleep(1000)
    x + " via slowSubscribe"
  }

  val sink = Sink.foreach(println)

  // connect source to sink with additional step
  source.via(fastPublisher)
    .via(slowSubscriber)
    .runForeach(println)
    .onComplete(_ => system.shutdown())

  val source2 = Source(1 to 100)
  val fastPublisher2 = Flow[Int].map(_ + 1)
  val slowSubscriber2 = Flow[Int].map { x => Thread.sleep(100)
    x + 2
  }

  source2.via(fastPublisher2)
    .buffer(100, OverflowStrategy.backpressure)
    .via(slowSubscriber2)
    .runWith(Sink.foreach(println))

}
