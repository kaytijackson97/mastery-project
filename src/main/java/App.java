import learn.repository.*;
import learn.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) throws DataAccessException {
        ApplicationContext container = new ClassPathXmlApplicationContext("dependency-configuration.xml");
        Controller controller = container.getBean(Controller.class);

        controller.run();
    }
}

