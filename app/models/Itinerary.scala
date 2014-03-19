package models
import PostgresConnection._
import java.util.Date


class Itinerary(var request_id:Request, var ret_date:Date, var ret_location:String, var ret_time:Date,var ret_flight:String,var ret_seat:String,var ret_airline:String,var comments:String) extends Entity

object Itinerary
{
  def apply(request_id:Request,ret_date:Date,ret_location:String,ret_time:Date, ret_flight:String, ret_seat:String, ret_airline:String, comments:String) =
    transactional
    {
      new Itinerary(request_id,ret_date,ret_location,ret_time,ret_flight, ret_seat, ret_airline, comments)
    }

  def findById(id: String) = byId[Itinerary](id)

  def findByRequest(request_id:Request): Option[Itinerary] = transactional
  {
    (select[Itinerary] where(_.request_id :== request_id)).headOption
  }


  def getAll:List[Itinerary] = all[Itinerary]

}
