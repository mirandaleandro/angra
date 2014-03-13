package models
import PostgresConnection._
import java.util.Date


class Request(var user_id:User, var ret_date:Date, var ret_location:String, var ret_time:Date, var comments:String) extends Entity

object Request
{
  def apply(user_id:User,  ret_date:java.util.Date, ret_location:String, ret_time:java.util.Date, comments:String) =
  transactional
  {
    new Request(user_id,ret_date,ret_location,ret_time,comments)
  }

  def findById(id: String) = byId[Request](id)

  def findByUser(user_id:User): Option[Request] = transactional
  {
    (select[Request] where(_.user_id :== user_id)).headOption
  }


  def getAll:List[Request] = all[Request]

}
