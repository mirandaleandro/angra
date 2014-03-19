package controllers

import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._

object Application extends Controller {

  def index = Action
  {
    transactional
    {
      Ok(views.html.index("Your new application is ready.",User.getAll))
    }
  }

  def landingPage = Action
  {
    transactional
    {
      Ok( views.html.landingpage() )
    }
  }

  def login = Action
  {
    transactional
    {
      Ok( views.html.login() )
    }
  }

  def travelPlanner = Action
  {
    transactional
    {
      Ok( views.html.PlanTravel.travelPlanner() )
    }
  }



}
