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

  def landingPage = Action {implicit request =>
    transactional
    {
      Ok( views.html.landingpage() )
    }
  }

  def aboutus = Action {implicit request =>
    transactional
    {
      Ok( views.html.aboutus() )
    }
  }

  def faq = Action {implicit request =>
    transactional
    {
      Ok( views.html.faq() )
    }
  }

  def travelPlanner = withAuth{ implicit user => implicit request =>

    transactional
    {
      Ok( views.html.PlanTravel.travelPlanner() )
    }
  }

  def travelResponse = withAuth{ implicit user => implicit request =>

    transactional
    {
      Ok( views.html.TravelPlanResponse.travelResponse() )
    }
  }

  def dashboard = withAuth{ implicit user => implicit request =>
    transactional
    {
      Ok( views.html.Dashboard.dashboard() )
    }
  }

}

