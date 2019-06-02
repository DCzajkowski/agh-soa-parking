package com.gnd.parking.Contracts;

import com.gnd.parking.Models.Region;
import java.util.List;

public interface RegionsRepositoryInterface {
    List<Region> all();
    Region find(Integer id);
    Region update(Region sourceRegion);
    Region save(Region user);
}



