package domain

import java.util.UUID

import play.api.libs.json.{Json, Writes}

/**
  * Created by eeugene on 08/07/2017.
  */

case class PostId(val id: UUID) extends AnyVal {
  override def toString: String = id.toString
}
object PostId {
  def apply(): PostId = new PostId(UUID.randomUUID())
  def apply(id: UUID): PostId = new PostId(id)
}

final case class PostContent(title: String, author: String, body: String)
final case class PostNotFound(id: PostId) extends RuntimeException(s"Blog post not found with id $id")
object PostNotFound {
  implicit val PostNotFoundWrites = new Writes[PostNotFound]{

    def writes(pnf: PostNotFound) = Json.obj(
      "message" -> pnf.getMessage
    )
  }
}