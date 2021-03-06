package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BurnerMessage;
import nl.stokpop.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestScopeController {

    private static final Logger log = LoggerFactory.getLogger(RequestScopeController.class);

    private final AfterburnerProperties props;

    private final MyRequestScopedThing myRequestScopedThing;

    public RequestScopeController(final AfterburnerProperties props, MyRequestScopedThing myRequestScopedThing) {
        this.props = props;
        this.myRequestScopedThing = myRequestScopedThing;
    }

    @ApiOperation(value = "Check request scope behaviour")
    @GetMapping(value = "/request-scope", produces = "application/json" )
    public BurnerMessage delay(@RequestParam(value = "duration", defaultValue = "100") String duration) {
        long startTime = System.currentTimeMillis();
        log.info("Before:" + myRequestScopedThing);
        Sleeper.sleep(duration);
        log.info("After:" + myRequestScopedThing);
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage("This was a delay of " + duration, props.getName(), durationMillis);
    }
}
