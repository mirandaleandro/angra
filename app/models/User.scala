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

class User(var name: String, var email:String, var password:String, var phone:String, var admin:Option[Boolean]) extends Entity

object User
{
  def apply(name:String,  email:String,  password:String, phone:String, admin:Option[Boolean] = None) =
  transactional
  {
    new User(name,email,password,phone,admin)
  }

  def unapply(user: User): Option[(String,String,String,String,Option[Boolean])]
  = Some((user.name, user.email, user.password, user.phone, user.admin))

  def findById(id: String) = byId[User](id)

  def findByEmail(email:String): Option[User] = transactional
  {
    (select[User] where(_.email :== email)).headOption
  }

  def authenticate(email:String,password:String): Option[User] = transactional
  {
    (select[User] where(_.email :== email,_.password :== password)).headOption
  }


  def getAll:List[User] = all[User]




}
