package org.eos.tof.api;

import jakarta.servlet.http.HttpSession;
import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest controller for creating and pulling on banners. The banners are kept in memory based on the session.
 *
 * @author Eos
 */
@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RequestMapping("/api/v1/banners")
public class BannerController {

    static final String ATTR_BANNER = "banner";

    private final BannerFactory factory;

    /**
     * Create a new {@link BannerController}.
     *
     * @param factory The factory to create new {@link Banner} instances
     */
    public BannerController(final BannerFactory factory) {
        this.factory = factory;
    }

    /**
     * Create a new {@link Banner} object and store it in the session.
     *
     * @param dto     The request body with the options to create the banner with.
     * @param session The session to store the current banner in.
     * @return The current state of the banner.
     */
    @PostMapping
    public BannerDto create(@RequestBody final BannerCreatorDto dto, final HttpSession session) {
        var banner = (Banner) session.getAttribute(ATTR_BANNER);
        if (banner != null) {
            banner.reset();
        }

        session.setAttribute(ATTR_BANNER, null);
        factory.setSpec(dto.getSpec());
        factory.setRate(dto.getMode());
        factory.setTheory(dto.isTheory());

        banner = factory.getObject();
        session.setAttribute(ATTR_BANNER, banner);

        return BannerDto.toDto(banner);
    }

    /**
     * @param amount  The amount to pull from with the banner, {@link Banner#pull(long)}.
     * @param session The session to store get the current banner from.
     * @return The current state of the banner.
     */
    @GetMapping
    public BannerDto pull(@RequestParam(defaultValue = "10") final String amount,
                          final HttpSession session) {
        var banner = (Banner) session.getAttribute(ATTR_BANNER);
        if (banner == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No banner attached to the session");
        }

        try {
            banner.pull(Integer.parseInt(amount));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not parse amount '" + amount + "'!", e);
        }

        return BannerDto.toDto(banner);
    }
}

