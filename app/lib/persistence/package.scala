/**
  * This is a sample of Todo Application.
  * 
  */

package lib

package object persistence {

  val default = onMySQL
  
  object onMySQL {
    implicit lazy val driver = slick.jdbc.MySQLProfile
    object UserRepository extends UserRepository
    object TodoRepository extends TodoRepository
    object TodoCategoryRepository extends TodoCategoryRepository
    object AuthTokenRepository extends AuthTokenRepository
  }
}
