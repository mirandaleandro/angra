package models
import Trip_Plan._
import PostgresConnection._


class ItineraryPlan(var itinerary_id:Itinerary) extends Entity
{
  def tripPlans = Trip_Plan.findByItineraryPlan(this)
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
