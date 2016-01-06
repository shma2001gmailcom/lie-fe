package org.misha.repository;

/**
 * author: misha
 * date: 12/27/15 1:38 PM.
 */

public class Monomial {
    private Long nodeId;
    private String leftId;
    private String rightId;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getLeftId() {
        return leftId;
    }

    public void setLeftId(String leftId) {
        this.leftId = leftId;
    }

    public String getRightId() {
        return rightId;
    }

    public void setRightId(String rightId) {
        this.rightId = rightId;
    }
}
