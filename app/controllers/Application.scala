package controllers

import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._
import controllers.Authentication.Secured

object Application extends Controller with Secured {

  def index = withAuth { username => implicit request =>
    Ok(views.html.index("Your new application is ready.",User.getAll))
  }

//  def user() = withUser { user => implicit request =>
//    val username = user.username
//    Ok(html.user(user))
//  }


//  def index = Action
//  {
//    transactional
//    {
//      Ok(views.html.index("Your new application is ready.",User.getAll))
//    }
//  }

  def landingPage = withAuth {username => implicit request =>

    transactional
    {
      Ok( views.html.landingpage() )
    }
  }

//  def login = Action
//  {
//    transactional
//    {
//      Ok( views.html.login() )
//    }
//  }

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
