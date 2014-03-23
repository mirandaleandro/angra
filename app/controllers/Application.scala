package controllers

import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._
import play.api.data._
import play.api.data.Forms._
import controllers.Authentication.Secured

object Application extends Controller with Secured {

  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def check(username: String, password: String) = {
    (username == "admin" && password == "1234")
  }

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Application.login).withSession("email" -> user._1)
    )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }



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

