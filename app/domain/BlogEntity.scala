package domain
import akka.actor._
import akka.pattern.pipe
import akka.persistence._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
/**
  * Created by eeugene on 07/07/2017.
  */

sealed trait BlogCommand
case class GetAllPost() extends BlogCommand
case class GetPost(id: PostId) extends BlogCommand
case class AddPost(content: PostContent) extends BlogCommand
case class UpdatePost(id: PostId, content: PostContent) extends BlogCommand

sealed trait BlogEvent {
  val id: PostId
  val content: PostContent
}
final case class PostAdded(id: PostId, content: PostContent) extends BlogEvent
final case class PostUpdated(id: PostId, content: PostContent) extends BlogEvent


class BlogEntity extends PersistentActor {

  private var state = BlogState()
  override def persistenceId: String = "blog"

  override def receiveCommand: Receive = {
    case GetAllPost => sender() ! state.posts.keys.toList
    case GetPost(id) => sender() ! state(id)
    case AddPost(content) => handleEvent(PostAdded(PostId(), content)) pipeTo sender()
    case UpdatePost(id, content) =>
      state(id) match {
        case response@Left(_) => sender() ! response
        case Right(_) => handleEvent(PostUpdated(id, content)).map(Right(_)) pipeTo sender()
      }
  }

  def handleEvent[E <: BlogEvent](event: =>E): Future[E] = {
    val p = Promise[E]
    persist(event) { e =>
      p.success(e)
      state += e
      if (lastSequenceNr != 0 && lastSequenceNr % 1000 == 0)
        saveSnapshot(state)
    }
    p.future
  }

  override def receiveRecover: Receive = {
    case event: BlogEvent =>
      state += event
    case SnapshotOffer(_, snapshot: BlogState) =>
      state = snapshot
  }
}
