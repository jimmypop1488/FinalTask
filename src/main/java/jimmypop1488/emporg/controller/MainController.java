package jimmypop1488.emporg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String orglist() {
        return "orglist";
    }
    @RequestMapping("/sl")
    public String sl() {
        return "stafflist";
    }
    @RequestMapping("/ot")
    public String ot() { return "orgtree"; }
    @RequestMapping("/st")
    public String st() { return "stafftree"; }
}
