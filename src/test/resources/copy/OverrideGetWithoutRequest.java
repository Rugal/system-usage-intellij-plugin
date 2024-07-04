import org.springframework.web.bind.annotation.GetMapping

interface Base {

  @GetMapping("/rugal")
  void upload();
}

public class A implements Base {

  @Override
  public void upl<caret>oad() {
  }
}
