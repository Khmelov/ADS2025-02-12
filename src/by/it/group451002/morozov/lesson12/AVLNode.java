package by.it.group451002.morozov.lesson12;

public class AVLNode {
    public Integer key;
    public String value;

    public AVLNode parent;
    public AVLNode left = null;
    public AVLNode right = null;

    int height = 0;

    public int calcHeights() {
        return height = Math.max(left == null ? 0 : left.calcHeights() + 1, right == null ? 0 : right.calcHeights() + 1);
    }

    public void checkCoeffs() {
        if (Math.abs(left == null ? 0 : left.height - (right == null ? 0 : right.height)) >= 2) {
            System.out.println("Error: " + Math.abs(left == null ? 0 : left.height - (right == null ? 0 : right.height)));
        } else {
            if (left != null) left.checkCoeffs();
            if (right != null) right.checkCoeffs();
        }
    }

    public AVLNode(Integer key, String value, AVLNode parent) {
        this.key = key;
        this.value = value;
        this.parent = parent;
    }

    public boolean containsValue(Object value) {
        return getNodePtrByValue(value) != null;
    }

    public void appendElemToString(StringBuilder resStr) {
        if (left != null) {
            left.appendElemToString(resStr);
            resStr.append(", ");
        }
        resStr.append(key);
        resStr.append("=");
        resStr.append(value);

        if (right != null) {
            resStr.append(", ");
            right.appendElemToString(resStr);
        }
    }

    public AVLNode getNodePtrByValue(Object value) {
        AVLNode result = null;
        if (value == this.value)
            result = this;
        else
        {
            if (right != null)
                result =  right.getNodePtrByValue(value);
            if (result == null && left != null)
                result = left.getNodePtrByValue(value);
        }
        return result;
    }

    public AVLNode getNodePtrByKey(Integer key) {
        if (key < this.key && left != null)
            return left.getNodePtrByKey(key);
        else if (key > this.key && right != null)
            return right.getNodePtrByKey(key);
        else if (key.equals(this.key)) return this;
        else return null;
    }

    public void RRRotation() {
        AVLNode rNode = right;

        if (rNode == null)
            return;
        right = rNode.right;
        if (right != null)
            right.parent = this;

        rNode.right = rNode.left;
        rNode.left = left;
        if (rNode.left != null)
            rNode.left.parent = rNode;
        left = rNode;

        Integer tempKey = key;
        String tempValue = value;

        key = left.key;
        value = left.value;
        left.value = tempValue;
        left.key = tempKey;

        left.height = Math.max(left.left == null ? 0 : left.left.height + 1, left.right == null ? 0 : left.right.height + 1);
        height = Math.max(left.height + 1, right == null ? 1 : right.height + 1);
    }

    public void LLRotation() {
        AVLNode lNode = left;
        if (lNode == null)
            return;

        left = lNode.left;
        if (left != null)
            left.parent = this;

        lNode.left = lNode.right;
        lNode.right = right;
        if (lNode.right != null)
            lNode.right.parent = lNode;
        right = lNode;

        Integer tempKey = key;
        String tempValue = value;

        key = right.key;
        value = right.value;
        right.value = tempValue;
        right.key = tempKey;

        right.height = Math.max(right.right == null ? 0 : right.right.height + 1, right.left == null ? 0 : right.left.height + 1);
        height = Math.max(right.height + 1, left == null ? 1 : left.height + 1);
    }

    public void LRRotation() {
        left.RRRotation();
        LLRotation();
    }

    public void RLRotation() {
        right.LLRotation();
        RRRotation();
    }

    public int getCoeff() {
        if (left == null) {
            if (right == null)
                return 0;
            else
                return -height;
        }
        else {
            if (right == null)
                return height;
            else
                return left.height - right.height;
        }
    }

    public void balanceTheNode() {
        if (getCoeff() >= 2) {
            if (left.getCoeff() > 0)
                LLRotation();
            else
                LRRotation();
        }
        else {
            if (right.getCoeff() <= -2)
                RRRotation();
            else
                RLRotation();
        }
    }

    public void checkHeightAfterRemove() {
        if (left == null && right == null) {
            height = 0;
            if (parent != null) {
                parent.checkHeightAfterRemove();
                return;
            }
        } else if (left == null) {
            height = right.height + 1;
        } else if (right == null) {
            height = left.height + 1;
        } else {

            height = Math.max(left.height, right.height);
        }
        if (Math.abs(getCoeff()) >= 2)
            balanceTheNode();
        else if (parent != null)
            parent.checkHeightAfterRemove();
    }

    public void checkHeight() {
        if (parent == null) {
            return;
        } else {
            if (parent.left == this) {
                if (parent.right == null || parent.right.height < parent.left.height) {
                    parent.height++;
                    if (Math.abs(parent.getCoeff()) >= 2)
                        parent.balanceTheNode();
                    else
                        parent.checkHeight();
                }
            } else {
                if (parent.left == null || parent.left.height < parent.right.height) {
                    parent.height++;
                    if (Math.abs(parent.getCoeff()) >= 2)
                        parent.balanceTheNode();
                    else
                        parent.checkHeight();
                }
            }
        }
    }
}