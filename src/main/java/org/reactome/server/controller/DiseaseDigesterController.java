package org.reactome.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiseaseDigesterController {

    public DiseaseDigesterController() {
    }

    @GetMapping(value = "/test/{echo}")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody String echo(@PathVariable String echo) {
        return echo;
    }
}
