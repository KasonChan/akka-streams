package demo

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

/**
 * Created by kasonchan on 8/1/15.
 */
object Print extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  // Create a source with some integer and print them
  val source = Source(List(1, 2, 3))
  val sink = Sink.foreach(println)
  val runnable = source.to(sink)
  runnable.run()

  read

  def read: Unit = {
    val r = Console.in.readLine()

    r match {
      case "exit" => system.shutdown()
      case s: String =>
        println(s)
        read
    }

  }

}
