package models
import PostgresConnection._
import java.util.Date


class Trip_Request(var request_id:Client_Request, var depart_date:String, var depart_location:String, var depart_time:String, var arrival_location:String, var arrival_time:String,var airline:String,var additional_transportation:String, var hotel_name:String, var hotel_membership:String, var checkout_date:String) extends Entity

object Trip_Request
{
  def apply(request_id:Client_Request,depart_date:String, depart_location:String,  depart_time:String,  arrival_location:String,  arrival_time:String, airline:String, additional_transportation:String,  hotel_name:String,  hotel_membership:String,  checkout_date:String) =
    transactional
    {
      new Trip_Request(request_id,depart_date, depart_location,  depart_time,  arrival_location,  arrival_time, airline, additional_transportation,  hotel_name,  hotel_membership,  checkout_date)
    }

  def findById(id: String) = byId[Trip_Request](id)

  def findByRequest(request_id:Client_Request): Option[Trip_Request] = transactional
  {
    (select[Trip_Request] where(_.request_id :== request_id)).headOption
  }


  def getAll:List[Trip_Request] = all[Trip_Request]

}