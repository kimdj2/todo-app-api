/** to do sample project
  */

package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.json._

import json.reads.JsValueTodo
import json.reads.JsValueTodoCategory

import service.TodoService
import service.TodoCategoryService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoCategoryController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController
    with I18nSupport {

  def list() = Action.async { implicit req =>
    for {
      categoryList <- TodoCategoryService.all
    } yield {
      Ok(Json.toJson({
        Json.obj("category_list" -> categoryList)
      }))
    }
  }

  def show(id: Long) = Action.async { implicit req =>
    (for {
      category <- TodoService.get(id)
    } yield {
      Ok(Json.toJson(category))
    }) recover { case _: Exception =>
      NotFound(Json.obj("error" -> "NotFound Error!!"))
    }
  }

  def save() = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodoCategory]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        category => {
          for {
            _ <- TodoCategoryService.add(category)
          } yield {
            Ok(Json.obj("result" -> "OK!"))
          }
        }
      )
  }

  def update(id: Long) = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodoCategory]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        category => {
          for {
            _ <- TodoCategoryService.update(id, category)
          } yield {
            Ok(Json.obj("result" -> "OK!"))
          }
        }
      )
  }

  def delete(id: Long) = Action.async { implicit req =>
    (for {
      _ <- TodoCategoryService.delete(id)
    } yield Ok(Json.obj("result" -> "OK!"))) recover { 
      case _: Exception => NotFound(Json.obj("error" -> "NotFound Error!!"))
    }
  }

}
