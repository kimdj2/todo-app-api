package service

import lib.model.Todo
import lib.model.Todo._
import lib.model.TodoCategory
import lib.model.TodoCategory._

import lib.persistence.onMySQL.TodoRepository
import lib.persistence.onMySQL.TodoCategoryRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import ixias.persistence.SlickRepository

object TodoService {

  type ReadsTodo = json.reads.JsValueTodo
  type WritesTodo = json.writes.JsValueTodo
  type ReadsCategory = json.reads.JsValueTodoCategory 
  type WritesCategory = json.writes.JsValueTodoCategory
  val ReadsTodo = json.reads.JsValueTodo
  val WritesTodo = json.writes.JsValueTodo
  val ReadsCategory = json.reads.JsValueTodoCategory
  val WritesCategory = json.writes.JsValueTodoCategory

  def all(): Future[Seq[WritesTodo]] = {
    for {
      todoList <- TodoRepository.all
      todoCategoryList <- TodoCategoryRepository.all
    } yield {
      for {
        todo <- todoList
      } yield {
        val category = todoCategoryList
          .find(_.id == todo.v.categoryId)
          .map(category =>
            WritesCategory(
              category.id,
              category.v.name,
              category.v.slug,
              category.v.color.code
            )
          )
        WritesTodo(
          todo.id,
          todo.v.title,
          todo.v.body,
          todo.v.state.code,
          category
        )
      }
    }
  }

  def add(todo: ReadsTodo): Future[Long] = {
    TodoRepository.add(
      Todo(
        title = todo.title,
        body = todo.body,
        categoryId = TodoCategory.Id(todo.categoryId),
        state = Todo.Status.TODO
      )
    )
  }

  def update(id: Long, json: ReadsTodo): Future[Option[Long]] = {
    for {
      todoOpt <- TodoRepository.get(Todo.Id(id))
    } yield {
      todoOpt match {
        case Some(todo) => {
          TodoRepository.update(
            new Todo.EmbeddedId(
              todo.v.copy(
                title = json.title,
                body = json.body,
                categoryId = TodoCategory.Id(json.categoryId),
                state = Todo.Status(json.state)
              )
            )
          )
          todo.v.id
        }
        case None => throw new Exception("データなし")
      }
    }
  }

  def get(id: Long): Future[WritesTodo] = {
    for {
      todoOpt <- TodoRepository.get(Todo.Id(id))
      category <- TodoCategoryRepository.get(todoOpt.get.v.categoryId)
      if todoOpt.isDefined
    } yield {
      todoOpt match {
        case Some(todo) =>
          WritesTodo(
            todo.id,
            todo.v.title,
            todo.v.body,
            todo.v.state.code,
            category.map(c =>
              WritesCategory(c.id, c.v.name, c.v.slug, c.v.color.code)
            )
          )
        case None => throw new Exception("データなし")
      }
    }
  }

  def delete(id: Long): Future[Option[Long]] = {
    for {
      todoOpt <- TodoRepository.remove(Todo.Id(id))
    } yield {
      todoOpt match {
        case Some(todo) => todo.v.id
        case None       => throw new Exception("データなし")
      }
    }
  }
}
