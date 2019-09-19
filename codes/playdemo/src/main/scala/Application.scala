import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok("sbt nmsl")
  }
}