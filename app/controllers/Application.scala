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

  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

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
      user => Redirect(routes.Application.dashboard).withSession("email" -> user._2)
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


  def register = Action {
    implicit request =>

      val userForm = Form(
        mapping(
          "name" -> nonEmptyText,
          "email" -> nonEmptyText(8),
          "password" -> nonEmptyText(5),
          "phone" -> nonEmptyText(10),
          "admin" -> optional(boolean)
          )(User.apply)(User.unapply))

      val processedForm = userForm.bindFromRequest
      processedForm.fold(hasErrors => BadRequest("Invalid submission"), success => {
        Ok("Account registered.")
      })
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

