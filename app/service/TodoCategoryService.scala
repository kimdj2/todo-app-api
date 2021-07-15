package service

import lib.model.TodoCategory
import lib.model.TodoCategory._

import lib.persistence.onMySQL.TodoRepository
import lib.persistence.onMySQL.TodoCategoryRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TodoCategoryService {

  type ReadsTodo = json.reads.JsValueTodo
  type WritesTodo = json.writes.JsValueTodo
  type ReadsCategory = json.reads.JsValueTodoCategory 
  type WritesCategory = json.writes.JsValueTodoCategory
  val ReadsTodo = json.reads.JsValueTodo
  val WritesTodo = json.writes.JsValueTodo
  val ReadsCategory = json.reads.JsValueTodoCategory
  val WritesCategory = json.writes.JsValueTodoCategory

  def all(): Future[Seq[WritesCategory]] = {
    for {
      todoCategoryList <- TodoCategoryRepository.all
    } yield todoCategoryList.map(category =>
      WritesCategory(
        category.id,
        category.v.name,
        category.v.slug,
        category.v.color.code
      )
    )
  }

  def add(category: ReadsCategory): Future[Long] = {
    TodoCategoryRepository.add(
      TodoCategory(
        name = category.name,
        slug = category.slug,
        color = TodoCategory.Color(category.color)
      )
    )
  }

  def update(id: Long, json: ReadsCategory): Future[Option[Long]] = {
    for {
      categoryOpt <- TodoCategoryRepository.get(TodoCategory.Id(id))
    } yield {
      categoryOpt match {
        case Some(category) => {
          TodoCategoryRepository.update(
            new TodoCategory.EmbeddedId(
              category.v.copy(
                name = json.name,
                slug = json.slug,
                color = TodoCategory.Color(json.color)
              )
            )
          )
          category.v.id
        }
        case None => throw new Exception("データなし")
      }
    }
  }

  def get(id: Long): Future[WritesCategory] = {
    for {
      categoryOpt <- TodoCategoryRepository.get(TodoCategory.Id(id))
    } yield {
      categoryOpt match {
        case Some(category) =>
          WritesCategory(
            category.id,
            category.v.name,
            category.v.slug,
            category.v.color.code
          )
        case None => throw new Exception("データなし")
      }
    }
  }

  def delete(id: Long): Future[Option[Long]] = {
    for {
      categoryOpt <- TodoCategoryRepository.remove(TodoCategory.Id(id))
      _ <- TodoRepository.removeByCategoryId(categoryOpt.get.v.id.get) if !categoryOpt.isEmpty
    } yield {
      categoryOpt match {
        case Some(category) => category.v.id
        case None => throw new Exception("データなし")
      }
    }
  }

}
