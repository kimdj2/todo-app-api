/** to do sample project
  */

package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import model.view.ViewValueTodoList
import model.view.ViewValueTodoCreate
import model.view.ViewValueTodoEdit
import model.ViewValueError

import service.TodoService
import service.TodoCategoryService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

@Singleton
class TodoController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController
    with I18nSupport {

  import forms.TodoForm._

  def index() = Action.async { implicit req =>
    for {
      todoList <- TodoService.all
    } yield {
      val vv = ViewValueTodoList(todoList)
      Ok(views.html.todo.List(vv))
    }
  }

  def create() = Action.async { implicit req =>
    for {
      categories <- TodoCategoryService.all
    } yield {
      val vv = ViewValueTodoCreate(categories)
      Ok(views.html.todo.Create(vv, todoAddForm))
    }
  }

  def save() = Action.async { implicit req =>
    todoAddForm
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoAdd]) => {
          for {
            categories <- TodoCategoryService.all
          } yield {
            val vv = ViewValueTodoCreate(categories)
            BadRequest(views.html.todo.Create(vv, formWithErrors))
          }
        },
        (todoData: TodoAdd) => {
          for {
            _ <- TodoService.add(todoData)
          } yield {
            Redirect(routes.TodoController.index())
          }
        }
      )
  }

  def edit(id: Long) = Action.async { implicit req =>
    (for {
      categories <- TodoCategoryService.all
      todo <- TodoService.get(id)
    } yield {
      val vv = ViewValueTodoEdit(categories, todo)
      Ok(
        views.html.todo.Edit(
          vv,
          todoEditForm.fill(
            TodoEdit(
              title = todo.title,
              body = todo.body,
              state = todo.state.code,
              categoryId = todo.categoryId
            )
          )
        )
      )
    }) recover { 
      case _: Exception =>
            NotFound(views.html.error.page404(ViewValueError()))
    }
  }

  def update(id: Long) = Action.async { implicit req =>
    todoEditForm
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoEdit]) => {
          for {
            categories <- TodoCategoryService.all
            todo <- TodoService.get(id)
          } yield {
            val vv = ViewValueTodoEdit(categories, todo)
            Ok(
              views.html.todo.Edit(
                vv,
                formWithErrors
              )
            )
          }
        },
        (todoData: TodoEdit) =>
          (
            for {
              _ <- TodoService.update(id, todoData)
            } yield Redirect(routes.TodoController.index())
          ) recover { case _: Exception =>
            NotFound(views.html.error.page404(ViewValueError()))
          }
      )
  }

  def delete(id: Long) = Action.async { implicit req =>
    (for {
      _ <- TodoService.delete(id)
    } yield Redirect(routes.TodoController.index())) recover {
      case _: Exception =>
        NotFound(views.html.error.page404(ViewValueError()))
    }
  }
}
