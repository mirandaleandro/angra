package models
import PostgresConnection._
import java.util.Date


class Client_Request(var user_id:User, var ret_date:String, var ret_location:String, var ret_time:String, var comments:String) extends Entity

object Client_Request
{
  def apply(user_id:User,  ret_date:String, ret_location:String, ret_time:String, comments:String) =
  transactional
  {
    new Client_Request(user_id,ret_date,ret_location,ret_time,comments)
  }

  def findById(id: String) = byId[Client_Request](id)

  def findByUser(user_id:User): Option[Client_Request] = transactional
  {
    (select[Client_Request] where(_.user_id :== user_id)).headOption
  }


  def getAll:List[Client_Request] = all[Client_Request]

}
