package DAO;

import Main.ISendable;

/**
 * to make generic T made get ID
 * use to classes that keep data
 */
public abstract class AbstractSendableRecord implements ISendable {
    public abstract int getId();
}
