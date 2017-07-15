package domain

import domain.BlogState.MaybePost

/**
  * Created by eeugene on 08/07/2017.
  */
object BlogState {
  def apply(): BlogState = BlogState(Map.empty)
  type MaybePost[+A] = Either[PostNotFound, A]
}

case class BlogState(posts: Map[PostId, PostContent]) {
  def +(event: BlogEvent) = {
    BlogState(posts.updated(event.id, event.content))
  }

  def apply(id: PostId): MaybePost[PostContent] = {
    posts.get(id).toRight(PostNotFound(id))
  }

}


