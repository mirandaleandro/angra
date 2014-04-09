package models
import PostgresConnection._
import java.util.Date


class Itinerary(var request_id:Client_Request, comments:String) extends Entity

object Itinerary
{
  def apply(request_id:Client_Request,comments:String) =
    transactional
    {
      new Itinerary(request_id,comments)
    }

  def findById(id: String) = byId[Itinerary](id)

  def findByRequest(request_id:Client_Request): Option[Itinerary] = transactional
  {
    (select[Itinerary] where(_.request_id :== request_id)).headOption
  }


  def getAll:List[Itinerary] = all[Itinerary]

}
