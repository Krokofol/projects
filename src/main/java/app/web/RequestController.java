package app.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * For Spring. Does not work yet.
 */
@Controller
public class RequestController {

    @RequestMapping("convert")
    @PostMapping
    public ResponseEntity<String> convert(@RequestBody Map<String, String> body) {
        String from = body.get("from");
        String to = body.get("to");

        if (checkInput(from, to))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //в разработке :)

        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    private boolean checkInput(String from, String to) {
        if (from == null || to == null)
            return true;
        from = from.replace(" ", "");
        to = to.replace(" ", "");
        return from.equals("") || to.equals("");
    }


}
