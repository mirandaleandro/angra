package models
import Trip_Plan._
import PostgresConnection._


class ItineraryPlan(var itinerary_id:Itinerary) extends Entity
{
  def tripPlans = {
    val tripPlans = Trip_Plan.findByItineraryPlan(this)

    if (tripPlans.isEmpty)
    {
      List( Trip_Plan(itineraryPlan_id = this) )
    }
    else
    {
      tripPlans
    }

  }
}
object ItineraryPlan
{
  def apply(itinerary_id:Itinerary) =
    transactional
    {
      new ItineraryPlan(itinerary_id)
    }

  def findById(id: String) = byId[ItineraryPlan](id)

  def findByItinerary(itinerary_id:Itinerary): List[ItineraryPlan] = transactional
  {
    (select[ItineraryPlan] where(_.itinerary_id :== itinerary_id))
  }


  def getAll:List[ItineraryPlan] = all[ItineraryPlan]
}
