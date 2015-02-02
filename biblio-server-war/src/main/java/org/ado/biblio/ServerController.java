package org.ado.biblio;

import org.ado.biblio.server.ServerStatusEnum;
import org.ado.biblio.server.ServerStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andoni del Olmo,
 * @since 06.01.15
 */
@RestController()
@RequestMapping("/server")
@SuppressWarnings("unused")
public class ServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    @RequestMapping(value = "/status", produces = "application/json")
    public ServerStatusMessage getServerStatus() {
        return new ServerStatusMessage(ServerStatusEnum.ONLINE);
    }
}