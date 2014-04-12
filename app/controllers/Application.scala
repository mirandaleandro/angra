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
        Client_Request.findById( cId )
      }

      Ok( views.html.PlanTravel.travelPlanner(clientRequest) )
    }
  }

  def removePlan(id:Option[String]) = withAuth{ implicit user => implicit request =>
    transactional
    {

      val clientRequest: Option[Client_Request] = id.flatMap{   cId =>
        Client_Request.findById( cId )
      }

      clientRequest.map { cr =>

       //if admin or owner
        if (user.exists(_.isAdmin) || user.exists( _ == cr.user_id))
        {
          cr.deleteCascade
        }
      }

      Redirect(routes.Application.dashboard)
    }
  }



  def travelResponse(id:Option[String]) = withAuth{ implicit user => implicit request =>

    transactional
    {
      val clientRequest: Option[Client_Request] = id.flatMap{   cId =>
        Client_Request.findById( cId )
      }

      clientRequest.map { cr =>
        Ok( views.html.TravelPlanResponse.travelResponse(cr) )
      }.getOrElse(Redirect(routes.Application.dashboard))
    }
  }

  def dashboard = withAuth{ implicit user => implicit request =>
    transactional
    {
      Ok( views.html.Dashboard.dashboard() )
    }
  }

}

