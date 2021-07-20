/**
  * This is a sample of Todo Application.
  * 
  */

package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.User

// UserTable: Userテーブルへのマッピングを行う
//~~~~~~~~~~~~~~
case class UserTable[P <: JdbcProfile]()(implicit val driver: P)
  extends Table[User, P] {
  import api._

  // Definition of DataSourceName
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave"  -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  // Definition of Query
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // Definition of Table
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Table(tag: Tag) extends BasicTable(tag, "user") {
    import User._
    // Columns
    /* @1 */ def id        = column[Id]            ("id",         O.UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def email     = column[String]        ("email",      O.Utf8Char255)
    /* @3 */ def password  = column[String]        ("password",   O.Utf8Char255)
    /* @4 */ def updatedAt = column[LocalDateTime] ("updated_at", O.TsCurrent)
    /* @5 */ def createdAt = column[LocalDateTime] ("created_at", O.Ts)

    type TableElementTuple = (
      Option[Id], String, String, LocalDateTime, LocalDateTime
    )

    // DB <=> Scala の相互のmapping定義
    def * = (id.?, email, password, updatedAt, createdAt) <> (
      // Tuple(table) => Model
      (t: TableElementTuple) => User(
        t._1, t._2, t._3, t._4, t._5
      ),
      // Model => Tuple(table)
      (v: TableElementType) => User.unapply(v).map { t => (
        t._1, t._2, t._3, LocalDateTime.now(), t._5
      )}
    )
  }
}