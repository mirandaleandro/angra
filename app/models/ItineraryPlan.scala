package models
import Trip_Plan._
import PostgresConnection._


class ItineraryPlan(var itinerary_id:Itinerary, var plan_number:Int) extends Entity
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
  def apply(itinerary_id:Itinerary, plan_number:Int = 0) =
    transactional
    {
      new ItineraryPlan(itinerary_id, plan_number = plan_number)
    }

  def findById(id: String) = byId[ItineraryPlan](id)

  def findByItinerary(itinerary_id:Itinerary): List[ItineraryPlan] = transactional
  {
    (select[ItineraryPlan] where(_.itinerary_id :== itinerary_id)) sortBy(_.plan_number)
  }

  def getAll:List[ItineraryPlan] = all[ItineraryPlan]
}
