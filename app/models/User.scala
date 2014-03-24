package models
import PostgresConnection._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps



/**
 * Created with IntelliJ IDEA.
 * models.User: leandro
 * Date: 2/28/14
 * Time: 7:12 PM
 */

class User(var name: String, var email:String, var password:String, var phone:String, var admin:Boolean) extends Entity

object User
{
  def apply(name:String,  email:String,  password:String, phone:String, admin:Boolean) =
  transactional
  {
    new User(name,email,password,phone,admin)
  }

  def findById(id: String) = byId[User](id)

  def findByEmail(email:String): Option[User] = transactional
  {
    (select[User] where(_.email :== email)).headOption
  }


  def getAll:List[User] = all[User]




  /**
   * Parse a User from a ResultSet
   */
  val simple = {
    get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[String]("user.phone") ~
      get[Boolean]("user.admin") map {
      case email~name~password~phone~admin => User(email, name, password, phone, admin)
    }
  }




  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where
         email = {email} and password = {password}
        """
      ).on(
          "email" -> email,
          "password" -> password
        ).as(User.simple.singleOpt)
    }
  }



}
