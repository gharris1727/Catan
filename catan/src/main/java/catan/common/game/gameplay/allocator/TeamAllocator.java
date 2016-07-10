package catan.common.game.gameplay.allocator;

import java.io.Serializable;

/**
 * Created by greg on 6/29/16.
 * A class that allocates users to teams.
 */
public interface TeamAllocator extends Serializable {

    TeamAllocation allocate(long seed);

}
