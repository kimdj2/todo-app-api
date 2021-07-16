/** to do sample project
  */

package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.json._
import service.TodoService
import service.TodoCategoryService

import json.reads.JsValueTodo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController
    with I18nSupport {

  def list() = Action.async { implicit req =>
    for {
      todoList <- TodoService.all
    } yield {
      Ok(Json.toJson(todoList))
    }
  }

  def show(id: Long) = Action.async { implicit req =>
    (for {
      todo <- TodoService.get(id)
    } yield {
      Ok(Json.toJson(todo))
    }) recover { case _: Exception =>
      NotFound(Json.obj("error" -> "NotFound Error!!"))
    }
  }

  def save() = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodo]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        todo => {
          for {
            _ <- TodoService.add(todo)
          } yield {
            Ok(Json.obj("result" -> "OK!"))
          }
        }
      )
  }

  def update(id: Long) = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodo]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        todo => {
          for {
            _ <- TodoService.update(id, todo)
          } yield {
            Ok(Json.obj("result" -> "OK!"))
          }
        }
      )
  }

  def delete(id: Long) = Action.async { implicit req =>
    (for {
      _ <- TodoService.delete(id)
    } yield Ok(Json.obj("result" -> "OK!"))) recover { 
      case _: Exception => NotFound(Json.obj("error" -> "NotFound Error!!"))
    }
  }
}
