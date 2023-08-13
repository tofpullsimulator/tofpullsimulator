package org.eos.tof.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eos.tof.common.Banner;

/**
 * Data transfer object for creating a banner.
 *
 * @author Eos
 * @see Banner
 */
@AllArgsConstructor
@Getter
@Setter
public class BannerCreatorDto {

    private Banner.Spec spec;
    private Banner.RateMode mode;
    @JsonProperty("theory")
    private boolean isTheory;
}
