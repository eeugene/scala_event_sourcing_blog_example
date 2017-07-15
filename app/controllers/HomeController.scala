package controllers


import java.util.UUID
import javax.inject.{Inject, Singleton}

import domain._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.libs.json.Json
import play.api.mvc._
import service.BlogService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, blogService: BlogService)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with Circe {

  def index = Action {
    Ok("Welcome home")
  }

  def getPost(id: String) = Action.async {
    blogService.getPost(PostId(UUID.fromString(id))).map {
      case Right(content) => Ok(content.asJson)
      case Left(error) => InternalServerError(Json.toJson(error))
    }
  }

  def getAllPost = Action.async {
    blogService.getAllPost.map {
      postId => Ok(postId.asJson)
    }
  }

  def addPost = Action.async(circe.json[PostContent]) { request =>
    blogService.addPost(request.body).map { postAdded => Ok(postAdded.asJson) }
  }

  def updatePost(id: String) = Action.async(circe.json[PostContent]) { request =>
    blogService.updatePost(PostId(UUID.fromString(id)), request.body).map {
      case Right(postUpdated) => Ok(postUpdated.asJson)
      case Left(error) => InternalServerError(Json.toJson(error))
    }
  }

}