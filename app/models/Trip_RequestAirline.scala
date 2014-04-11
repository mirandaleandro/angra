package models

import models.PostgresConnection._

class Trip_RequestAirline(var tripRequest:Trip_Request, var airline:Airline) extends Entity

object Trip_RequestAirline
{
  def apply(tripRequest:Trip_Request, airline:Airline) =
    transactional
    {
      new Trip_RequestAirline(tripRequest,airline)
    }

  def findById(id: String) = byId[Trip_RequestAirline](id)

  def findByTripRequest(aTripRequest:Trip_Request): List[Trip_RequestAirline] = transactional
  {
    select[Trip_RequestAirline] where(_.tripRequest :== aTripRequest)
  }

  def findByTripRequestAndAirline(aTripRequest:Trip_Request, aAirline:Airline): Option[Trip_RequestAirline] = transactional
  {
    (select[Trip_RequestAirline] where(_.tripRequest :== aTripRequest, _.airline :== aAirline)).headOption
  }

  def getAll:List[Trip_RequestAirline] = all[Trip_RequestAirline]

}