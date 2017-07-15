package service

import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import domain.BlogState.MaybePost
import domain._

import scala.concurrent.Future
import scala.concurrent.duration._
/**
  * Created by eeugene on 08/07/2017.
  */
class BlogService @Inject() (system: ActorSystem) {

  implicit val duration: Timeout = 15 seconds
  private val blogEntity = system.actorOf(Props[BlogEntity])

  def getPost(id:PostId) = {
    (blogEntity ? GetPost(id)).mapTo[MaybePost[PostContent]]
  }
  def addPost(post: PostContent): Future[PostAdded] = {
    (blogEntity ? AddPost(post)).mapTo[PostAdded]
  }
  def getAllPost: Future[List[PostId]] = {
    (blogEntity ? GetAllPost).mapTo[List[PostId]]
  }
  def updatePost(id: PostId, postContent: PostContent): Future[MaybePost[PostUpdated]] = {
    (blogEntity ? UpdatePost(id,postContent)).mapTo[MaybePost[PostUpdated]]
  }
}