package by.it.group451002.koltsov.lesson12;

import java.util.SortedMap;

enum Color {RED, BLACK};

public class RBNode {
    RBNode parent, left = null, right = null;
    Color color;
    Integer key;
    String value;

    public RBNode(Integer key, String value, RBNode parent, Color color) {
        this.key = key;
        this.value = value;
        this.parent = parent;
        this.color = color;
    }

    public void appendElemToString(StringBuilder resStr) {
        if (left != null) {
            left.appendElemToString(resStr);
            resStr.append(", ");
        }

        resStr.append(key).append("=").append(value);

        if (right != null) {
            resStr.append(", ");
            right.appendElemToString(resStr);
        }
    }

    public void addValuesToHeadMap(SortedMap<Integer, String> HeadMap, Integer toKey)
    {
        if (left != null)
            left.addValuesToHeadMap(HeadMap, toKey);

        if (key < toKey)
            HeadMap.put(key, value);
        else
            return;

        if (right != null)
            right.addValuesToHeadMap(HeadMap, toKey);
    }

    public void addValuesToTailMap(SortedMap<Integer, String> HeadMap, Integer fromKey)
    {
        if (left != null)
            left.addValuesToTailMap(HeadMap, fromKey);

        if (key >= fromKey)
            HeadMap.put(key, value);

        if (right != null)
            right.addValuesToTailMap(HeadMap, fromKey);
    }

    public RBNode getNodePtrByValue(Object value) {
        RBNode result = null;
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
}
