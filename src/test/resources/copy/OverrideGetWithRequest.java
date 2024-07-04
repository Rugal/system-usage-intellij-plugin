import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/root")
interface Base {

  @GetMapping("/rugal")
  void upload();
}

public class A implements Base {

  @Override
  public void upl<caret>oad() {
  }
}
