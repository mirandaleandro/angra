package models
import PostgresConnection._
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

}
