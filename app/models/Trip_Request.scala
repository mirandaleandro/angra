package models
import PostgresConnection._
import java.util.Date


class Trip_Request(var creator:User, var request_id:Client_Request, var depart_date:Date, var depart_location:String, var depart_time:Date, var arrival_location:String, var arrival_time:Date,var airline:String,var rental:Boolean, var taxi:Boolean, var shuttle:Boolean, var hotel_name:String, var hotel_membership:String, var checkout_date:Date) extends Entity

object Trip_Request
{
  def apply(creator:User, request_id:Client_Request,depart_date:Date, depart_location:String,  depart_time:Date,  arrival_location:String,  arrival_time:Date, airline:String, rental:Boolean,  taxi:Boolean,  shuttle:Boolean,  hotel_name:String,  hotel_membership:String,  checkout_date:Date) =
    transactional
    {
      new Trip_Request(creator, request_id,depart_date, depart_location,  depart_time,  arrival_location,  arrival_time, airline, rental,  taxi,  shuttle,  hotel_name,  hotel_membership,  checkout_date)
    }

  def findById(id: String) = byId[Trip_Request](id)

  def findByRequest(request_id:Client_Request): List[Trip_Request] = transactional
  {
    select[Trip_Request] where(_.request_id :== request_id)
  }

  def findByUser(user:User): Option[Trip_Request] = transactional
  {
    (select[Trip_Request] where(_.creator :== user)).headOption
  }

  def getAll:List[Trip_Request] = all[Trip_Request]

}