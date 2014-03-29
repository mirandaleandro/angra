package controllers

import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._
import play.api.data._
import play.api.data.Forms._
import controllers.Authentication.Secured
import models._
import views._

object Application extends Controller with Secured {

  def landingPage = withAuth {username => implicit request =>

    transactional
    {
      Ok( views.html.landingpage() )
    }
  }

  def travelPlanner = Action
  {
    transactional
    {
      Ok( views.html.PlanTravel.travelPlanner() )
    }
  }

  def travelResponse = Action
  {
    transactional
    {
      Ok( views.html.TravelPlanResponse.travelResponse() )
    }
  }

  def dashboard = Action
  {
    transactional
    {
      Ok( views.html.Dashboard.dashboard() )
    }
  }
}

