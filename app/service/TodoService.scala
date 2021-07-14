package service

import lib.model.Todo
import lib.model.Todo._
import lib.model.TodoCategory
import lib.model.TodoCategory._

import lib.persistence.onMySQL.TodoRepository
import lib.persistence.onMySQL.TodoCategoryRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import model.item.TodoList
import model.item.TodoItem
import model.item.TodoCategoryItem

import ixias.persistence.SlickRepository

import forms.TodoForm._

object TodoService {
  def all(): Future[Seq[TodoList]] = {
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
            TodoCategoryItem(
              category.id,
              category.v.name,
              category.v.slug,
              category.v.color
            )
          )
        TodoList(
          TodoItem(
            todo.id,
            todo.v.title,
            todo.v.body,
            todo.v.state,
            todo.v.categoryId
          ),
          category
        )
      }
    }
  }

  def add(form: TodoAdd): Future[Long] = {
    TodoRepository.add(
      Todo(
        title = form.title,
        body = form.body,
        categoryId = TodoCategory.Id(form.categoryId),
        state = Todo.Status.TODO
      )
    )
  }

  def update(id: Long, form: TodoEdit): Future[Option[Long]] = {
    for {
      todoOpt <- TodoRepository.get(Todo.Id(id))
    } yield {
      todoOpt match {
        case Some(todo) => {
          TodoRepository.update(
            new Todo.EmbeddedId(
              todo.v.copy(
                title = form.title,
                body = form.body,
                categoryId = TodoCategory.Id(form.categoryId),
                state = Todo.Status(form.state.toShort)
              )
            )
          )
          todo.v.id
        }
        case None => throw new Exception("データなし")
      }
    }
  }

  def get(id: Long): Future[TodoItem] = {
    for {
      todoOpt <- TodoRepository.get(Todo.Id(id))
    } yield {
      todoOpt match {
        case Some(todo) =>
          TodoItem(
            todo.id,
            todo.v.title,
            todo.v.body,
            todo.v.state,
            todo.v.categoryId
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
