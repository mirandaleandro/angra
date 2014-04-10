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

  def travelPlanner(id:Option[String]) = withAuth{ implicit user => implicit request =>

    transactional
    {

      val clientRequest: Option[Client_Request] = id.flatMap{   cId =>
        val c = Client_Request.findById( cId )
        c
      }

      Ok( views.html.PlanTravel.travelPlanner(clientRequest) )
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

