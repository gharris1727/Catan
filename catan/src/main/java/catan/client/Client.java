package catan.client;

import catan.common.event.EventProcessor;
import catan.common.event.GenericEvent;

/**
 * Created by greg on 10/23/16.
 * Superclass of client-like entities.
 */
public interface Client extends EventProcessor<GenericEvent> {
}
