package models
import PostgresConnection._


class ItineraryPlan(var itinerary_id:Itinerary, var ret_date:String, var ret_location:String, var ret_time:String,var ret_flight:String,var ret_seat:String,var ret_airline:String) extends Entity

object ItineraryPlan
{
  def apply(itinerary_id:Itinerary,ret_date:String,ret_location:String,ret_time:String, ret_flight:String, ret_seat:String, ret_airline:String) =
    transactional
    {
      new ItineraryPlan(itinerary_id,ret_date,ret_location,ret_time,ret_flight, ret_seat, ret_airline)
    }

  def findById(id: String) = byId[ItineraryPlan](id)

  def findByRequest(itinerary_id:Itinerary): Option[ItineraryPlan] = transactional
  {
    (select[ItineraryPlan] where(_.itinerary_id :== itinerary_id)).headOption
  }


  def getAll:List[ItineraryPlan] = all[ItineraryPlan]
}
