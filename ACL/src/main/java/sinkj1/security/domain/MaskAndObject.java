package sinkj1.security.domain;

import java.io.Serializable;

public class MaskAndObject implements Serializable {

    private int objId;
    private int mask;

    public MaskAndObject(int objId, int mask) {
        this.objId = objId;
        this.mask = mask;
    }

    public MaskAndObject() {}

    public int getObjId() {
        return objId;
    }

    public int getMask() {
        return mask;
    }
}
