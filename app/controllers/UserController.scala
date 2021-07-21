package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.json._
import org.mindrot.jbcrypt.BCrypt
import lib.model.User

import service.UserService
import service.TodoCategoryService
import mvc.auth.UserAuthProfile

import cats.data.{EitherT, OptionT}
import cats.implicits._

import json.reads.JsValueUser
import scala.concurrent._

import ixias.play.api.auth.mvc.AuthExtensionMethods
import ixias.play.api.mvc.BaseExtensionMethods

@Singleton
class UserController @Inject() (
    val controllerComponents: ControllerComponents,
    val authProfile: UserAuthProfile
)(implicit ec: ExecutionContext)
    extends AuthExtensionMethods
    with BaseExtensionMethods
    with BaseController
    with I18nSupport {

  def save() = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueUser]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        user => {
          (for {
            loginUser <- UserService.get(user.email) if loginUser.isEmpty
            uid <- UserService
              .add(user.copy(password = hashPassword(user.password)))
            result <- authProfile.loginSucceeded(
              User.Id(uid),
              { _ =>
                Ok(Json.obj("result" -> "OK!"))
              }
            )
          } yield result) recover { case _: Exception =>
            BadRequest(Json.obj("message" -> "Duplicate Error"))
          }
        }
      )
  }

  def login() = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueUser]
      .fold(
        errors => {
          Future.successful(
            BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          )
        },
        user => {
          (for {
            loginUser <- UserService.get(user.email)
            if !loginUser.isEmpty
            result <- authProfile.loginSucceeded(
              User.Id(loginUser.get.id),
              { _ =>
                Ok(Json.toJson(loginUser.get.copy(password = "")))
              }
            ) if checkPassword(user.password, loginUser.get.password)
          } yield result) recover { case _: Exception =>
            Unauthorized(Json.obj("message" -> "Auth Error"))
          }
        }
      )
  }

  def authCheck() = Authenticated(authProfile) { implicit req =>
    authProfile.loggedIn { user =>
      Ok(Json.toJson(json.writes.JsValueUser(user.id, user.v.email, "")))
    }
  }

  def logout() = Authenticated(authProfile).async { implicit req =>
    authProfile.loggedIn { user =>
      authProfile.logoutSucceeded(
        user.id, {
          Ok(Json.obj("result" -> "OK!"))
        }
      )
    }
  }

  // パスワード機能
  def hashPassword(rawPassword: String): String =
    BCrypt.hashpw(rawPassword, BCrypt.gensalt(8))

  def checkPassword(rawPassword: String, hashedPassword: String): Boolean =
    BCrypt.checkpw(rawPassword, hashedPassword)

}
