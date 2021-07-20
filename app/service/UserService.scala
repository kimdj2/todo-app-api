package service

import lib.model.User
import lib.model.User._

import lib.persistence.onMySQL.UserRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import ixias.persistence.SlickRepository

object UserService {

  type ReadsUser = json.reads.JsValueUser
  type WritesUser = json.writes.JsValueUser
  val ReadsUser = json.reads.JsValueUser
  val WritesUser = json.writes.JsValueUser

  def add(user: ReadsUser): Future[Long] = {
    UserRepository.add(
      User(
        email = user.email,
        password = user.password,
      )
    )
  }

  def get(email: String): Future[Option[WritesUser]] = {
    for {
      userOpt <- UserRepository.getByEmail(email)
    } yield {
      userOpt match {
        case Some(user) =>
          Some(WritesUser(
            user.id,
            user.v.email,
            user.v.password,
          ))
        case None => None
      }
    }
  }
}
