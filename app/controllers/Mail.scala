package controllers

import play.api._
import play.api.mvc._
import com.typesafe.plugin._
import models.PostgresConnection._
import play.api.Play.current
import play.api.data._
import data.Forms._
import play.api.libs.iteratee._
import scala.concurrent.Future
import scala.Some



/**
 * Created by Tahna Black
 */
package object Mail extends Controller {

  //object messageService {

  case class ContactUsForm(var name: String, var email: String, var message: String)
  val contactUsForm = Form[ContactUsForm](
  mapping(
    "name" -> text,
    "email" -> text,
    "message" -> text
  ) (ContactUsForm.apply) (ContactUsForm.unapply)
  )

  /**
   * Handle "Contact Us" message in About Us page
   */
 object MessageService {
  def sendMessage = Action { implicit request =>
    contactUsForm.bindFromRequest.fold(
      FormWithErrors =>
        InternalServerError,
      contactUsForm =>
      {
        try {
          EmailService.sendEmail("New Message for CET Services", "Char Black <cetservicesinc@gmail.com>", "cetservicesinc@gmail.com", contactUsForm.name, contactUsForm.email, contactUsForm.message)
        Ok(views.html.aboutus(
          message = "Thanks for contacting CET Services!"
            )
          )
        } catch {
          case e: Exception => println("exception caught: " + e)
            Ok(views.html.aboutus (
            message = "Sorry, your email could not be sent."
          ))}
      }
    )
  }
  }

  /**
   * Handle all mail.
   */

  object EmailService {
  def sendEmail(subject: String, recipient: String, recipientEmail: String, name: String, fromEmail: String, message: String) = {
    val mailService = use[MailerPlugin].email
    mailService.setSubject(subject)
    mailService.setRecipient(recipient + " <" + recipientEmail + ">", recipientEmail)
    mailService.setFrom(name + " <" + fromEmail + ">")
    mailService.send(message)
  }
  }

}






