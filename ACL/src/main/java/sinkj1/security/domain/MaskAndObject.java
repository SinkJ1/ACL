package sinkj1.security.domain;

import java.io.Serializable;

public class MaskAndObject implements Serializable {
    private String objId;
    private int mask;

    public MaskAndObject(String objId, int mask) {
        this.objId = objId;
        this.mask = mask;
    }

    public MaskAndObject() {

    }

    public String getObjId() {
        return objId;
    }

    public int getMask() {
        return mask;
    }
}
